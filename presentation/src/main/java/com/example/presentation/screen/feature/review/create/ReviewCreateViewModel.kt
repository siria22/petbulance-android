package com.example.presentation.screen.feature.review.create

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.example.domain.model.feature.hospital.review.SaveReviewParam
import com.example.domain.usecase.feature.hospital.review.CreateReviewUseCase
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReviewCreateViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val createReviewUseCase: CreateReviewUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(ReviewCreateState())
    val state: StateFlow<ReviewCreateState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ReviewCreateEvent>()
    val eventFlow: SharedFlow<ReviewCreateEvent> = _eventFlow

    fun onIntent(intent: ReviewCreateIntent) {
        when (intent) {
            is ReviewCreateIntent.OnBackClicked -> handleBack()
            is ReviewCreateIntent.OnCloseClicked -> handleClose()
            is ReviewCreateIntent.OnNextClicked -> handleNext()

            // Step 1
            is ReviewCreateIntent.OnHospitalSelected -> {
                _state.update { it.copy(step1 = it.step1.copy(hospitalInfo = intent.hospital)) }
            }

            is ReviewCreateIntent.OnRatingChanged -> {
                _state.update { it.copy(step1 = it.step1.copy(ratings = intent.rating)) }
            }

            is ReviewCreateIntent.OnReceiptAnalyzed -> {
                applyReceiptResult(intent.result)
            }

            // Step 2
            is ReviewCreateIntent.OnAnimalTypeChanged -> {
                _state.update { it.copy(step2 = it.step2.copy(animalType = intent.value)) }
            }

            is ReviewCreateIntent.OnDetailAnimalTypeChanged -> {
                _state.update { it.copy(step2 = it.step2.copy(detailAnimalType = intent.value)) }
            }

            is ReviewCreateIntent.OnTreatmentChanged -> {
                _state.update { it.copy(step2 = it.step2.copy(treatment = intent.value)) }
            }

            // Step 3
            is ReviewCreateIntent.OnContentChanged -> {
                _state.update { it.copy(step3 = it.step3.copy(content = intent.value)) }
            }

            is ReviewCreateIntent.OnImagesChanged -> {
                _state.update { it.copy(step3 = it.step3.copy(images = intent.images)) }
            }

            is ReviewCreateIntent.OnSubmitClicked -> submitReview()
        }
    }

    private fun handleBack() {
        val currentState = _state.value
        when (currentState.currentStep) {
            ReviewCreateStep.HOSPITAL_AND_RATING -> emitEvent(ReviewCreateEvent.ShowExitDialog)
            ReviewCreateStep.ANIMAL_AND_TREATMENT -> {
                _state.update { it.copy(currentStep = ReviewCreateStep.HOSPITAL_AND_RATING) }
            }

            ReviewCreateStep.REVIEW_CONTENT -> {
                _state.update { it.copy(currentStep = ReviewCreateStep.ANIMAL_AND_TREATMENT) }
            }
        }
    }

    private fun handleClose() {
        emitEvent(ReviewCreateEvent.ShowExitDialog)
    }

    private fun handleNext() {
        val currentState = _state.value
        when (currentState.currentStep) {
            ReviewCreateStep.HOSPITAL_AND_RATING -> {
                if (validateStep1(currentState.step1)) {
                    _state.update { it.copy(currentStep = ReviewCreateStep.ANIMAL_AND_TREATMENT) }
                } else {
                    emitEvent(ReviewCreateEvent.ShowToast("병원과 별점을 모두 입력해주세요."))
                }
            }

            ReviewCreateStep.ANIMAL_AND_TREATMENT -> {
                if (validateStep2(currentState.step2)) {
                    _state.update { it.copy(currentStep = ReviewCreateStep.REVIEW_CONTENT) }
                } else {
                    emitEvent(ReviewCreateEvent.ShowToast("동물 정보와 진료명을 입력해주세요."))
                }
            }

            ReviewCreateStep.REVIEW_CONTENT -> {
                // Submit is handled by OnSubmitClicked
            }
        }
    }

    private fun validateStep1(step1: Step1State): Boolean {
        return step1.hospitalInfo != null &&
                step1.ratings.expertise > 0 &&
                step1.ratings.kindness > 0 &&
                step1.ratings.facility > 0
    }

    private fun validateStep2(step2: Step2State): Boolean {
        return step2.animalType.isNotBlank() &&
                // detailAnimalType is optional or required based on policy? assuming required for now
                step2.detailAnimalType.isNotBlank() &&
                step2.treatment.isNotBlank()
    }

    private fun applyReceiptResult(result: ReceiptAnalysisResult) {
        _state.update {
            it.copy(
                step1 = it.step1.copy(
                    isReceiptVerified = true
                    // Note: Hospital mapping logic can be added here if needed
                ),
                step2 = it.step2.copy(
                    visitDate = result.visitDate,
                    price = result.totalPrice
                )
            )
        }
        emitEvent(ReviewCreateEvent.ShowToast("영수증이 인식되었습니다."))
    }

    private fun submitReview() {
        val currentState = _state.value
        if (currentState.isLoading) return

        if (currentState.step3.content.isBlank()) {
            emitEvent(ReviewCreateEvent.ShowToast("후기 내용을 입력해주세요."))
            return
        }

        launch {
            _state.update { it.copy(isLoading = true) }

            // 1. 파라미터 준비
            val param = SaveReviewParam(
                hospitalId = currentState.step1.hospitalInfo?.id ?: 0L,
                rating = currentState.step1.ratings,
                price = currentState.step2.price,
                animalType = currentState.step2.animalType,
                detailAnimalType = currentState.step2.detailAnimalType,
                treatment = currentState.step2.treatment,
                visitDate = currentState.step2.visitDate.ifBlank { LocalDateTime.now().toString() },
                comment = currentState.step3.content,
                isReceipt = currentState.step1.isReceiptVerified
            )

            // 2. 이미지 변환 (Uri -> ByteArray)
            // 비동기로 변환하여 UI 스레드 차단 방지 (Dispatchers.IO 사용 권장이나 여기선 간단히 처리)
            val imageBytesList = currentState.step3.images.mapNotNull { uriString ->
                uriToByteArray(uriString)
            }

            // 3. UseCase 호출
            createReviewUseCase(param, imageBytesList)
                .onSuccess {
                    emitEvent(ReviewCreateEvent.ShowToast("리뷰가 등록되었습니다."))
                    emitEvent(ReviewCreateEvent.NavigateToHome)
                }
                .onFailure { e ->
                    emitEvent(ReviewCreateEvent.ShowToast("리뷰 등록 실패: ${e.message}"))
                }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun uriToByteArray(uriString: String): ByteArray? {
        return try {
            val uri = Uri.parse(uriString)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun emitEvent(event: ReviewCreateEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}