package com.petbulance.presentation.screen.feature.review.create

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.ReviewImageParam
import com.petbulance.domain.model.feature.hospital.review.SaveReviewParam
import com.petbulance.domain.usecase.feature.hospital.review.CreateReviewUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.nav.ScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import javax.inject.Inject
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class ReviewCreateViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val createReviewUseCase: CreateReviewUseCase,
    private val savedStateHandle: SavedStateHandle
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

    init {
        checkReceiptAnalysisResult()
    }

    private fun checkReceiptAnalysisResult() {
        val jsonString = savedStateHandle.get<String>(ScreenDestinations.Review.Create.ARG_DATA)

        if (!jsonString.isNullOrBlank()) {
            try {
                val analysisResult = Json.decodeFromString<ReceiptAnalysisResultUiModel>(jsonString)

                _state.update {
                    it.copy(
                        step1 = it.step1.copy(
                            hospitalInfo = HospitalInfo(id = 0, name = analysisResult.hospitalName),
                            isReceiptVerified = true
                        ),
                        step2 = it.step2.copy(
                            visitDate = analysisResult.visitDate,
                            price = analysisResult.totalPrice,
                        )
                    )
                }

                savedStateHandle.remove<String>(ScreenDestinations.Review.Create.ARG_DATA)
                // TODO: 다이어로그 띄우고 다음 스텝으로 이동
                emitEvent(ReviewCreateEvent.ShowToast("영수증 정보가 적용되었습니다."))

            } catch (e: Exception) {
                e.printStackTrace()
                emitEvent(ReviewCreateEvent.ShowToast("데이터 불러오기 실패"))
            }
        }
    }

    private fun handleBack() {
        val currentState = _state.value
        val cleanState = currentState.copy(showValidationError = false)

        when (currentState.currentStep) {
            ReviewCreateStep.HOSPITAL_AND_RATING -> emitEvent(ReviewCreateEvent.ShowExitDialog)
            ReviewCreateStep.ANIMAL_AND_TREATMENT -> {
                _state.update { cleanState.copy(currentStep = ReviewCreateStep.HOSPITAL_AND_RATING) }
            }

            ReviewCreateStep.REVIEW_CONTENT -> {
                _state.update { cleanState.copy(currentStep = ReviewCreateStep.ANIMAL_AND_TREATMENT) }
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
                    _state.update {
                        it.copy(
                            currentStep = ReviewCreateStep.ANIMAL_AND_TREATMENT,
                            showValidationError = false
                        )
                    }
                } else {
                    _state.update { it.copy(showValidationError = true) }
                    emitEvent(ReviewCreateEvent.ShowToast("병원과 별점을 모두 입력해주세요."))
                }
            }

            ReviewCreateStep.ANIMAL_AND_TREATMENT -> {
                if (validateStep2(currentState.step2)) {
                    _state.update {
                        it.copy(
                            currentStep = ReviewCreateStep.REVIEW_CONTENT,
                            showValidationError = false
                        )
                    }
                } else {
                    _state.update { it.copy(showValidationError = true) }
                    emitEvent(ReviewCreateEvent.ShowToast("동물 정보와 진료명을 입력해주세요."))
                }
            }

            ReviewCreateStep.REVIEW_CONTENT -> {
                // no-op: Submit is handled by OnSubmitClicked
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
                step2.detailAnimalType.isNotBlank() &&
                step2.treatment.isNotBlank()
    }

    private fun applyReceiptResult(result: ReceiptAnalysisResult) {
        _state.update {
            it.copy(
                step1 = it.step1.copy(
                    isReceiptVerified = true
                ),
                step2 = it.step2.copy(
                    visitDate = result.visitDate,
                    price = result.totalPrice,
                    receiptItems = result.items
                )
            )
        }
        // TODO: 다이어로그 띄우고 다음 스텝으로 이동
        emitEvent(ReviewCreateEvent.ShowToast("영수증이 인식되었습니다."))
    }

    private fun submitReview() {
        val currentState = _state.value
        if (currentState.isLoading) return

        // 1. 최종 검증 (내용 확인)
        if (currentState.step3.content.isBlank()) {
            _state.update { it.copy(showValidationError = true) }
            emitEvent(ReviewCreateEvent.ShowToast("후기 내용을 입력해주세요."))
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, showValidationError = false) }

            try {
                // 2. [비동기] 이미지 URI를 ByteArray로 변환 (Dispatchers.IO 사용 필수)
                // ContentResolver를 통해 실제 파일 데이터를 읽어옴
                val imageBytesList = withContext(Dispatchers.IO) {
                    currentState.step3.images.mapNotNull { uriString ->
                        try {
                            context.contentResolver.openInputStream(Uri.parse(uriString))?.use {
                                it.readBytes()
                            }
                        } catch (e: Exception) {
                            null // 개별 이미지 로드 실패 시 무시하거나 에러 처리 정책에 따름
                        }
                    }
                }

                // 3. 파라미터 구성 (SaveReviewParam에서 images 제거됨)
                val param = SaveReviewParam(
                    hospitalId = currentState.step1.hospitalInfo?.id ?: 0L,
                    rating = currentState.step1.ratings,
                    price = currentState.step2.price,
                    animalType = currentState.step2.animalType,
                    detailAnimalType = currentState.step2.detailAnimalType,
                    receiptItems = currentState.step2.receiptItems,
                    visitDate = currentState.step2.visitDate,
                    comment = currentState.step3.content,
                    isReceipt = currentState.step1.isReceiptVerified
                )

                // 4. UseCase 호출
                createReviewUseCase(param, imageBytesList)
                    .onSuccess {
                        emitEvent(ReviewCreateEvent.ShowToast("리뷰가 성공적으로 등록되었습니다."))
                        emitEvent(ReviewCreateEvent.NavigateToHome)
                    }
                    .onFailure { e ->
                        emitEvent(ReviewCreateEvent.ShowToast("리뷰 등록 실패: ${e.message}"))
                    }

            } catch (e: Exception) {
                e.printStackTrace()
                emitEvent(ReviewCreateEvent.ShowToast("처리 중 오류가 발생했습니다."))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun uriToByteArray(uriString: String): ByteArray? {
        return try {
            val uri = uriString.toUri()
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