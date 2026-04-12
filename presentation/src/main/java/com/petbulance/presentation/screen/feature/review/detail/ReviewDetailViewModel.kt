package com.petbulance.presentation.screen.feature.review.detail

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.support.report.ReportParam
import com.petbulance.domain.model.type.ReportType
import com.petbulance.domain.usecase.feature.hospital.review.DeleteReviewUseCase
import com.petbulance.domain.usecase.feature.hospital.review.GetReviewDetailUseCase
import com.petbulance.domain.usecase.feature.hospital.review.LikeReviewUseCase
import com.petbulance.domain.usecase.feature.hospital.review.UnlikeReviewUseCase
import com.petbulance.domain.usecase.feature.support.report.CreateReportUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.nav.ScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReviewDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getReviewDetailUseCase: GetReviewDetailUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val createReportUseCase: CreateReportUseCase,
    private val likeReviewUseCase: LikeReviewUseCase,
    private val unlikeReviewUseCase: UnlikeReviewUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<ReviewDetailDataState>(ReviewDetailDataState.Init)
    val dataState: StateFlow<ReviewDetailDataState> = _dataState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ReviewDetailEvent>()
    val eventFlow: SharedFlow<ReviewDetailEvent> = _eventFlow

    private val _reviewDetailData = MutableStateFlow(ReviewDetailData.empty)
    val reviewDetailData: StateFlow<ReviewDetailData> = _reviewDetailData.asStateFlow()

    private val reviewId: Long =
        savedStateHandle.get<Long>(ScreenDestinations.Review.Detail.ARG_ID) ?: 0L

    private var lastLikeToggleTime = 0L
    private var isLikeProcessing = false

    init {
        observeErrorEvent(eventFlow)
        fetchReviewDetail()
    }

    fun onIntent(intent: ReviewDetailIntent) {
        when (intent) {
            is ReviewDetailIntent.DeleteReview -> deleteReview()
            is ReviewDetailIntent.ReportReview -> reportReview(intent.reason)
            is ReviewDetailIntent.ToggleLike -> toggleLike()
        }
    }

    private fun deleteReview() {
        launch {
            if (reviewId == 0L) return@launch

            _dataState.value = ReviewDetailDataState.OnProgress

            deleteReviewUseCase(reviewId)
                .onSuccess {
                    _eventFlow.emit(ReviewDetailEvent.DeleteSuccess)
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        ReviewDetailEvent.DataFetch.Error(
                            userMessage = "리뷰 삭제에 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                }

            _dataState.value = ReviewDetailDataState.Init
        }
    }

    private fun reportReview(reason: String) {
        launch {
            if (reviewId == 0L) return@launch

            _dataState.value = ReviewDetailDataState.OnProgress

            val param = ReportParam(
                reportType = ReportType.REVIEW,
                reportReason = reason,
                targetId = reviewId
            )

            createReportUseCase(param)
                .onSuccess {
                    _eventFlow.emit(ReviewDetailEvent.ReportSuccess)
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        ReviewDetailEvent.DataFetch.Error(
                            userMessage = "신고 접수에 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                }

            _dataState.value = ReviewDetailDataState.Init
        }
    }

    private fun fetchReviewDetail() {
        launch {
            if (reviewId == 0L) {
                _eventFlow.emit(
                    ReviewDetailEvent.DataFetch.Error(
                        userMessage = "유효하지 않은 리뷰 접근입니다.",
                        exceptionMessage = "Some Exception Message",
                        displayType = ErrorDisplayType.Common
                    )
                )
                return@launch
            }

            _dataState.value = ReviewDetailDataState.OnProgress

            getReviewDetailUseCase(reviewId)
                .onSuccess { detail ->
                    _reviewDetailData.value = ReviewDetailData(
                        id = detail.id,
                        userNickname = detail.userNickname,
                        visitDate = detail.visitDate,
                        hospitalName = detail.hospitalName,
                        animalType = detail.animalType,
                        detailAnimalType = detail.detailAnimalType,
                        rating = (detail.facilityRating + detail.expertiseRating + detail.kindnessRating) / 3.0,
                        price = detail.totalPrice,
                        isReceiptVerified = detail.receiptCheck,
                        content = detail.reviewContent,
                        images = detail.images,
                        likeCount = detail.likeCount,
                        isLiked = detail.liked,
                        isAuthor = detail.isAuthor
                    )
                    _dataState.value = ReviewDetailDataState.Init
                }
                .onFailure { exception ->
                    _dataState.value = ReviewDetailDataState.Init
                    _eventFlow.emit(
                        ReviewDetailEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Custom,
                            userMessage = "리뷰 정보를 불러오는데 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                }
        }
    }

    private fun toggleLike() {
        launch {
            if (reviewId == 0L) return@launch

            val currentTime = System.currentTimeMillis()
            if (currentTime - lastLikeToggleTime < LIKE_DEBOUNCE_MILLIS) {
                return@launch
            }
            lastLikeToggleTime = currentTime

            if (isLikeProcessing) return@launch
            isLikeProcessing = true

            val currentData = _reviewDetailData.value
            val wasLiked = currentData.isLiked
            val previousLikeCount = currentData.likeCount

            _reviewDetailData.value = currentData.copy(
                isLiked = !wasLiked,
                likeCount = if (wasLiked) previousLikeCount - 1 else previousLikeCount + 1
            )

            val result = if (wasLiked) {
                unlikeReviewUseCase(reviewId)
            } else {
                likeReviewUseCase(reviewId)
            }

            result.onFailure { exception ->
                _reviewDetailData.value = currentData.copy(
                    isLiked = wasLiked,
                    likeCount = previousLikeCount
                )

                _eventFlow.emit(
                    ReviewDetailEvent.DataFetch.Error(
                        userMessage = "좋아요 처리에 실패했습니다.",
                        exceptionMessage = exception.message
                    )
                )
            }

            isLikeProcessing = false
        }
    }

    companion object {
        private const val LIKE_DEBOUNCE_MILLIS = 200L
    }
}