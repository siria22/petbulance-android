package com.petbulance.presentation.screen.feature.review.create

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.SaveReviewParam
import com.petbulance.domain.usecase.feature.hospital.review.CreateReviewUseCase
import com.petbulance.domain.usecase.feature.hospital.review.FindHospitalIdByNameUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.nav.ScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ReviewCreateViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val createReviewUseCase: CreateReviewUseCase,
    private val findHospitalIdByNameUseCase: FindHospitalIdByNameUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _state = MutableStateFlow(ReviewCreateState())
    val state: StateFlow<ReviewCreateState> = _state.asStateFlow()

    private var searchJob: Job? = null

    private val _eventFlow = MutableSharedFlow<ReviewCreateEvent>()
    val eventFlow: SharedFlow<ReviewCreateEvent> = _eventFlow

    fun onIntent(intent: ReviewCreateIntent) {
        when (intent) {
            is ReviewCreateIntent.OnBackClicked -> handleBack()
            is ReviewCreateIntent.OnCloseClicked -> handleClose()
            is ReviewCreateIntent.OnNextClicked -> handleNext()

            // Step 1: Hospital, Cost & Treatment
            is ReviewCreateIntent.OnHospitalSelected -> {
                _state.update {
                    it.copy(
                        step1 = it.step1.copy(
                            hospitalInfo = HospitalInfo(
                                id = 0,
                                name = intent.hospitalName
                            )
                        )
                    )
                }
                fetchHospitalId(intent.hospitalName)
            }

            is ReviewCreateIntent.OnPriceChanged -> {
                _state.update { it.copy(step1 = it.step1.copy(totalPrice = intent.value)) }
            }

            is ReviewCreateIntent.OnTreatmentChanged -> {
                updateTreatment(intent.index, intent.value)
            }

            is ReviewCreateIntent.OnTreatmentAdded -> {
                addTreatment()
            }

            is ReviewCreateIntent.OnTreatmentRemoved -> {
                removeTreatment(intent.index)
            }

            is ReviewCreateIntent.OnReceiptAnalyzed -> {
                applyReceiptResult(intent.result)
            }

            // Step 2: Animal & Rating
            is ReviewCreateIntent.OnAnimalTypeChanged -> {
                _state.update { it.copy(step2 = it.step2.copy(animalType = intent.value)) }
            }

            is ReviewCreateIntent.OnDetailAnimalTypeChanged -> {
                _state.update { it.copy(step2 = it.step2.copy(detailAnimalType = intent.value)) }
            }

            is ReviewCreateIntent.OnRatingChanged -> {
                _state.update { it.copy(step2 = it.step2.copy(ratings = intent.rating)) }
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

    private fun updateTreatment(index: Int, value: String) {
        _state.update { state ->
            val newTreatments = state.step1.treatments.toMutableList()
            if (index in newTreatments.indices) {
                newTreatments[index] = value
            }
            state.copy(step1 = state.step1.copy(treatments = newTreatments))
        }
    }

    private fun addTreatment() {
        _state.update { state ->
            val newTreatments = state.step1.treatments + ""
            state.copy(step1 = state.step1.copy(treatments = newTreatments))
        }
    }

    private fun removeTreatment(index: Int) {
        _state.update { state ->
            val newTreatments = state.step1.treatments.toMutableList()
            if (index in newTreatments.indices && newTreatments.size > 1) {
                newTreatments.removeAt(index)
            }
            state.copy(step1 = state.step1.copy(treatments = newTreatments))
        }
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
                            totalPrice = analysisResult.totalPrice.toString(),
                            isReceiptVerified = true
                        ),
                        step2 = it.step2.copy(
                            visitDate = analysisResult.visitDate,
                        )
                    )
                }

                savedStateHandle.remove<String>(ScreenDestinations.Review.Create.ARG_DATA)
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
            ReviewCreateStep.HOSPITAL_AND_COST -> emitEvent(ReviewCreateEvent.ShowExitDialog)
            ReviewCreateStep.ANIMAL_AND_RATING -> {
                _state.update { cleanState.copy(currentStep = ReviewCreateStep.HOSPITAL_AND_COST) }
            }

            ReviewCreateStep.REVIEW_CONTENT -> {
                _state.update { cleanState.copy(currentStep = ReviewCreateStep.ANIMAL_AND_RATING) }
            }
        }
    }

    private fun handleClose() {
        emitEvent(ReviewCreateEvent.ShowExitDialog)
    }

    private fun handleNext() {
        val currentState = _state.value
        when (currentState.currentStep) {
            ReviewCreateStep.HOSPITAL_AND_COST -> {
                if (validateStep1(currentState.step1)) {
                    _state.update {
                        it.copy(
                            currentStep = ReviewCreateStep.ANIMAL_AND_RATING,
                            showValidationError = false
                        )
                    }
                } else {
                    _state.update { it.copy(showValidationError = true) }
                    emitEvent(ReviewCreateEvent.ShowToast("병원 정보와 비용, 진료명을 입력해주세요."))
                }
            }

            ReviewCreateStep.ANIMAL_AND_RATING -> {
                if (validateStep2(currentState.step2)) {
                    _state.update {
                        it.copy(
                            currentStep = ReviewCreateStep.REVIEW_CONTENT,
                            showValidationError = false
                        )
                    }
                } else {
                    _state.update { it.copy(showValidationError = true) }
                    emitEvent(ReviewCreateEvent.ShowToast("동물 정보와 별점을 입력해주세요."))
                }
            }

            ReviewCreateStep.REVIEW_CONTENT -> {
                // no-op: Submit is handled by OnSubmitClicked
            }
        }
    }

    private fun validateStep1(step1: Step1State): Boolean {
        return step1.hospitalInfo != null &&
                step1.totalPrice.isNotBlank() &&
                step1.treatments.any { it.isNotBlank() }
    }

    private fun validateStep2(step2: Step2State): Boolean {
        return step2.animalType.isNotBlank() &&
                step2.detailAnimalType.isNotBlank() &&
                step2.ratings.expertise > 0 &&
                step2.ratings.kindness > 0 &&
                step2.ratings.facility > 0
    }

    private fun applyReceiptResult(result: ReceiptAnalysisResult) {
        _state.update {
            it.copy(
                step1 = it.step1.copy(
                    isReceiptVerified = true,
                    hospitalInfo = HospitalInfo(id = 0, name = result.hospitalName),
                    totalPrice = result.totalPrice.toString()
                ),
                step2 = it.step2.copy(
                    visitDate = result.visitDate,
                    receiptItems = result.items
                )
            )
        }
        emitEvent(ReviewCreateEvent.ShowToast("영수증이 인식되었습니다."))
    }

    private fun submitReview() {
        val currentState = _state.value
        if (currentState.isLoading) return

        if (currentState.step3.content.isBlank()) {
            _state.update { it.copy(showValidationError = true) }
            emitEvent(ReviewCreateEvent.ShowToast("후기 내용을 입력해주세요."))
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, showValidationError = false) }

            try {
                val imageBytesList = withContext(Dispatchers.IO) {
                    currentState.step3.images.mapNotNull { uriString ->
                        uriToByteArray(uriString)
                    }
                }

                val priceLong =
                    currentState.step1.totalPrice.filter { it.isDigit() }.toLongOrNull() ?: 0L

                val param = SaveReviewParam(
                    hospitalId = currentState.step1.hospitalInfo?.id ?: 0L,
                    rating = currentState.step2.ratings,
                    price = priceLong,
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

    private fun fetchHospitalId(name: String) {
        if (name.length < 2) return

        // 1. 이전 검색 작업이 있다면 취소
        searchJob?.cancel()

        // 2. 새로운 검색 작업 시작
        searchJob = viewModelScope.launch {
            // 3. 1초 대기 (디바운싱)
            delay(1000L)

            findHospitalIdByNameUseCase(name)
                .onSuccess { hospitals ->
                    // 가장 첫 번째 결과의 ID를 사용
                    val firstMatch = hospitals.firstOrNull()
                    if (firstMatch != null) {
                        _state.update { state ->
                            state.copy(
                                step1 = state.step1.copy(
                                    hospitalInfo = firstMatch
                                )
                            )
                        }
                    }
                }
                .onFailure {
                    // 검색 실패 시 처리 (현재는 별도 처리 없음, ID 0 유지)
                }
        }
    }

    private fun emitEvent(event: ReviewCreateEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}