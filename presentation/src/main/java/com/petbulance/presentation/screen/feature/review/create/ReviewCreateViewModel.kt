package com.petbulance.presentation.screen.feature.review.create

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
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
            is ReviewCreateIntent.OnPreviousStep -> moveToPreviousStep()

            // Step 1: Hospital, Cost & Treatment
            is ReviewCreateIntent.OnHospitalQueryChanged -> {
                val q = intent.query
                _state.update {
                    it.copy(
                        step1 = it.step1.copy(
                            hospitalQuery = q,
                            hospitalInfoForReview = null,
                            hospitalCandidates = emptyList()
                        )
                    )
                }
                fetchHospitalCandidates(q)
            }

            is ReviewCreateIntent.OnHospitalCandidateSelected -> {
                _state.update {
                    it.copy(
                        step1 = it.step1.copy(
                            hospitalInfoForReview = intent.hospital,
                            hospitalQuery = intent.hospital.name,
                            hospitalCandidates = emptyList()
                        )
                    )
                }
            }

            is ReviewCreateIntent.OnHospitalClearClicked -> {
                _state.update {
                    it.copy(
                        step1 = it.step1.copy(
                            hospitalQuery = "",
                            hospitalInfoForReview = null,
                            hospitalCandidates = emptyList()
                        )
                    )
                }
            }

            is ReviewCreateIntent.OnPriceChanged -> {
                _state.update { it.copy(step1 = it.step1.copy(totalPrice = intent.value)) }
            }

            is ReviewCreateIntent.OnReceiptAnalyzed -> {
                applyReceiptResult(intent.result)
            }

            // Step 2: Animal & Rating
            is ReviewCreateIntent.OnAnimalTypeChanged -> {
                _state.update { it.copy(step1 = it.step1.copy(animalType = intent.value)) }
            }

            is ReviewCreateIntent.OnDetailAnimalTypeChanged -> {
                _state.update { it.copy(step1 = it.step1.copy(detailAnimalType = intent.value)) }
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

    private fun moveToPreviousStep() {
        when (_state.value.currentStep) {
            ReviewCreateStep.HOSPITAL_AND_COST -> {
                /* nop */
            }

            ReviewCreateStep.ANIMAL_AND_RATING -> {
                _state.update {
                    it.copy(currentStep = ReviewCreateStep.HOSPITAL_AND_COST)
                }
            }

            ReviewCreateStep.REVIEW_CONTENT -> {
                _state.update {
                    it.copy(currentStep = ReviewCreateStep.ANIMAL_AND_RATING)
                }
            }
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
                            hospitalQuery = analysisResult.hospitalName,
                            hospitalInfoForReview = null,
                            isReceiptVerified = true,
                            totalPrice = analysisResult.totalPrice.toString()
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
                    emitEvent(ReviewCreateEvent.ShowToast("병원 정보와 비용, 동물종을 입력해주세요."))
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
        return step1.hospitalInfoForReview != null &&
                step1.totalPrice.isNotBlank() &&
                step1.detailAnimalType.isNotBlank()
    }

    private fun validateStep2(step2: Step2State): Boolean {
        return step2.ratings.expertise > 0 &&
                step2.ratings.kindness > 0 &&
                step2.ratings.facility > 0
    }

    private fun applyReceiptResult(result: ReceiptAnalysisResult) {
        _state.update {
            it.copy(
                step1 = it.step1.copy(
                    isReceiptVerified = true,
                    hospitalInfoForReview = HospitalInfoForReview(
                        id = result.hospitalId,
                        name = result.hospitalName
                    ),
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
                    hospitalId = currentState.step1.hospitalInfoForReview?.id ?: 0L,
                    rating = currentState.step2.ratings,
                    price = priceLong,
                    animalType = currentState.step1.animalType,
                    detailAnimalType = currentState.step1.detailAnimalType,
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

    private fun fetchHospitalCandidates(query: String) {
        if (query.length < 2) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300L)

            findHospitalIdByNameUseCase(query)
                .onSuccess { hospitals ->
                    _state.update { state ->
                        state.copy(
                            step1 = state.step1.copy(
                                hospitalCandidates = hospitals
                            )
                        )
                    }
                }
                .onFailure {
                    _state.update { state ->
                        state.copy(step1 = state.step1.copy(hospitalCandidates = emptyList()))
                    }
                }
        }
    }

    private fun emitEvent(event: ReviewCreateEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}