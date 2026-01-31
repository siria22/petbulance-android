package com.petbulance.presentation.screen.feature.review.detail

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.usecase.feature.hospital.review.GetReviewDetailUseCase
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
    private val getReviewDetailUseCase: GetReviewDetailUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<ReviewDetailDataState>(ReviewDetailDataState.Init)
    val dataState: StateFlow<ReviewDetailDataState> = _dataState.asStateFlow()

    private val _screenState =
        MutableStateFlow<ReviewDetailScreenState>(ReviewDetailScreenState.Init)
    val screenState: StateFlow<ReviewDetailScreenState> = _screenState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ReviewDetailEvent>()
    val eventFlow: SharedFlow<ReviewDetailEvent> = _eventFlow

    private val _reviewDetailData = MutableStateFlow(ReviewDetailData.empty)
    val reviewDetailData: StateFlow<ReviewDetailData> = _reviewDetailData.asStateFlow()

    private val reviewId: Long =
        savedStateHandle.get<Long>(ScreenDestinations.Review.Detail.ARG_ID) ?: 0L

    init {
        observeErrorEvent(eventFlow)
        fetchReviewDetail()
    }

    fun onIntent(intent: ReviewDetailIntent) {
        // 추후 좋아요, 신고 등의 인터랙션 처리
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
                        isLiked = detail.liked
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
}