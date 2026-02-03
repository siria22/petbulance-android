package com.petbulance.presentation.screen.feature.review.edit

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.hospital.review.ModifyReviewParam
import com.petbulance.domain.model.feature.hospital.review.ReviewImageParam
import com.petbulance.domain.model.feature.hospital.review.ReviewRating
import com.petbulance.domain.usecase.feature.hospital.review.GetReviewDetailUseCase
import com.petbulance.domain.usecase.feature.hospital.review.ModifyReviewUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.nav.ScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReviewEditViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val getReviewDetailUseCase: GetReviewDetailUseCase,
    private val modifyReviewUseCase: ModifyReviewUseCase
) : BaseViewModel() {

    private val reviewId: Long =
        savedStateHandle.get<Long>(ScreenDestinations.Review.Edit.ARG_ID) ?: -1L

    private val _state = MutableStateFlow(ReviewEditState())
    val state: StateFlow<ReviewEditState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ReviewEditEvent>()
    val eventFlow: SharedFlow<ReviewEditEvent> = _eventFlow

    init {
        loadReviewDetail()
    }

    fun onIntent(intent: ReviewEditIntent) {
        when (intent) {
            ReviewEditIntent.OnBackClicked -> emitEvent(ReviewEditEvent.ShowExitDialog)
            is ReviewEditIntent.OnHospitalNameChanged -> _state.update { it.copy(hospitalName = intent.value) }
            is ReviewEditIntent.OnTotalCostChanged -> _state.update { it.copy(totalCost = intent.value) }
            is ReviewEditIntent.OnAnimalTypeChanged -> _state.update { it.copy(animalType = intent.value) }
            is ReviewEditIntent.OnDetailAnimalTypeChanged -> _state.update {
                it.copy(
                    detailAnimalType = intent.value
                )
            }

            is ReviewEditIntent.OnRatingChanged -> _state.update { it.copy(ratings = intent.value) }
            is ReviewEditIntent.OnContentChanged -> _state.update { it.copy(content = intent.value) }
            is ReviewEditIntent.OnNewImagesChanged -> _state.update { it.copy(newImages = intent.images) }
            is ReviewEditIntent.OnRemoveImage -> removeImage(intent.index)
            ReviewEditIntent.OnSubmitClicked -> submitReview()
        }
    }

    private fun loadReviewDetail() {
        if (reviewId == -1L) {
            emitEvent(ReviewEditEvent.ShowToast("잘못된 접근입니다."))
            emitEvent(ReviewEditEvent.NavigateBack)
            return
        }

        launch {
            _state.update { it.copy(isLoading = true) }
            getReviewDetailUseCase(reviewId)
                .onSuccess { review ->
                    _state.update {
                        it.copy(
                            reviewId = review.id,
                            hospitalId = review.hospitalId,
                            hospitalName = review.hospitalName,
                            totalCost = review.totalPrice.toString(),
                            animalType = review.animalType,
                            detailAnimalType = review.detailAnimalType.korean,
                            ratings = ReviewRating(
                                expertise = review.expertiseRating,
                                kindness = review.kindnessRating,
                                facility = review.facilityRating
                            ),
                            existingImages = review.images,
                            content = review.reviewContent,
                            visitDate = review.visitDate,
                            isReceiptVerified = review.receiptCheck,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    emitEvent(ReviewEditEvent.ShowToast("리뷰 정보를 불러오는데 실패했습니다."))
                    emitEvent(ReviewEditEvent.NavigateBack)
                }
        }
    }

    private fun submitReview() {
        val currentState = _state.value
        if (currentState.isLoading) return

        if (currentState.content.isBlank()) {
            emitEvent(ReviewEditEvent.ShowToast("후기 내용을 입력해주세요."))
            return
        }

        launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val newImagesParams = withContext(Dispatchers.IO) {
                    currentState.newImages.mapNotNull { uriString ->
                        uriToByteArray(uriString)?.let { bytes ->
                            ReviewImageParam(
                                filename = "image_${System.currentTimeMillis()}.jpg",
                                contentType = "image/jpeg",
                                content = "",
                                isReceipt = false
                            ) to bytes
                        }
                    }
                }

                val param = ModifyReviewParam(
                    reviewId = currentState.reviewId,
                    hospitalId = currentState.hospitalId,
                    title = currentState.hospitalName,
                    rating = currentState.ratings,
                    price = currentState.totalCost.filter { it.isDigit() }.toLongOrNull() ?: 0L,
                    animalType = currentState.animalType,
                    detailAnimalType = currentState.detailAnimalType,
                    receiptItems = currentState.receiptItems,
                    visitDate = currentState.visitDate,
                    comment = currentState.content,
                    isReceipt = currentState.isReceiptVerified,
                    images = newImagesParams.map { it.first }
                )

                val imageBytes = newImagesParams.map { it.second }

                modifyReviewUseCase(param, imageBytes)
                    .onSuccess {
                        emitEvent(ReviewEditEvent.EditSuccess)
                        emitEvent(ReviewEditEvent.NavigateBack)
                    }
                    .onFailure { e ->
                        emitEvent(ReviewEditEvent.ShowToast("리뷰 수정 실패: ${e.message}"))
                    }

            } catch (e: Exception) {
                e.printStackTrace()
                emitEvent(ReviewEditEvent.ShowToast("오류가 발생했습니다."))
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

    private fun removeImage(index: Int) {
        val currentState = _state.value
        val existingCount = currentState.existingImages.size

        if (index < existingCount) {
            // 기존 이미지 삭제
            val newExisting = currentState.existingImages.toMutableList().apply { removeAt(index) }
            _state.update { it.copy(existingImages = newExisting) }
        } else {
            // 새로 추가된 이미지 삭제
            val newIndex = index - existingCount
            val newImages = currentState.newImages.toMutableList()
            if (newIndex in newImages.indices) {
                newImages.removeAt(newIndex)
                _state.update { it.copy(newImages = newImages) }
            }
        }
    }

    private fun emitEvent(event: ReviewEditEvent) {
        launch { _eventFlow.emit(event) }
    }
}