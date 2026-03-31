package com.petbulance.presentation.screen.feature.review.main

import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.domain.model.feature.support.report.ReportParam
import com.petbulance.domain.model.type.ReportType
import com.petbulance.domain.usecase.feature.hospital.review.FilterReviewUseCase
import com.petbulance.domain.usecase.feature.support.report.CreateReportUseCase
import com.petbulance.domain.usecase.feature.user.user.GetMyInfoUseCase
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val filterReviewUseCase: FilterReviewUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val createReportUseCase: CreateReportUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<ReviewState>(ReviewState.Init)
    val state: StateFlow<ReviewState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<ReviewEvent>()
    val event: SharedFlow<ReviewEvent> = _event

    private val _reviewData = MutableStateFlow(ReviewData.empty)
    val reviewData: StateFlow<ReviewData> = _reviewData.asStateFlow()

    private var currentUserNickname: String? = null
    private var currentCursorId: Long? = null
    private var hasNextPage: Boolean = true

    init {
        loadCurrentUserInfo()
        loadReviews(isRefresh = true)
    }

    private fun loadCurrentUserInfo() {
        launch {
            getMyInfoUseCase().onSuccess { userInfo ->
                currentUserNickname = userInfo.nickname
            }
        }
    }

    fun onIntent(intent: ReviewIntent) {
        when (intent) {
            is ReviewIntent.ChangeRegion -> {
                _reviewData.update { it.copy(selectedRegion = intent.region, selectedDistrict = intent.district) }
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.ChangeAnimalType -> {
                _reviewData.update { it.copy(selectedAnimalType = intent.animalType) }
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.ChangeSort -> {
                _reviewData.update { it.copy(selectedSort = intent.sortType) }
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.ToggleReceipt -> {
                _reviewData.update { it.copy(isReceiptVerified = !it.isReceiptVerified) }
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.TogglePhotoReview -> {
                _reviewData.update { it.copy(isPhotoReview = !it.isPhotoReview) }
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.LoadMore -> {
                if (hasNextPage && !_reviewData.value.isLoadingNextPage) {
                    loadReviews(isRefresh = false)
                }
            }

            is ReviewIntent.Refresh -> {
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.ReportReview -> {
                reportReview(intent.reviewId, intent.reason)
            }
        }
    }

    private fun loadReviews(isRefresh: Boolean) {
        val data = _reviewData.value

        launch {
            if (isRefresh) {
                _state.value = ReviewState.Loading
                currentCursorId = null
                hasNextPage = true
                _reviewData.update { it.copy(reviews = emptyList()) }
            } else {
                _reviewData.update { it.copy(isLoadingNextPage = true) }
            }

            val selectedCategory = data.selectedAnimalType
            val animalTypesParam =
                if (selectedCategory == null || selectedCategory == AnimalCategory.ALL) {
                    null
                } else {
                    AnimalSpecies.entries
                        .filter { it.category == selectedCategory }
                        .map { it.name }
                }

            val result = filterReviewUseCase(
                region = data.selectedRegion?.let { region -> "${region.name} ${data.selectedDistrict ?: ""}" },
                animalTypes = animalTypesParam,
                isReceipt = if (data.isReceiptVerified) true else null,
                cursorId = currentCursorId,
                size = DEFAULT_PAGE_SIZE
            )

            result.onSuccess { pagingData ->
                currentCursorId = pagingData.nextCursorId
                hasNextPage = pagingData.hasNext

                var newReviews = pagingData.items.map { item ->
                    toHospitalReview(item, currentUserNickname)
                }

                if (data.isPhotoReview) {
                    newReviews = newReviews.filter { it.imageUrls.isNotEmpty() }
                }

                if (isRefresh) {
                    _reviewData.update { it.copy(reviews = newReviews) }
                    _state.value = ReviewState.Init
                } else {
                    _reviewData.update { it.copy(reviews = it.reviews + newReviews, isLoadingNextPage = false) }
                }
            }.onFailure { e ->
                _state.value = ReviewState.Init
                _reviewData.update { it.copy(isLoadingNextPage = false) }
                _event.emit(
                    ReviewEvent.DataFetch.Error(
                        userMessage = "리뷰를 불러오지 못했어요",
                        exceptionMessage = e.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                )
            }
        }
    }

    private fun reportReview(reviewId: Long, reason: String) {
        launch {
            val param = ReportParam(
                reportType = ReportType.REVIEW,
                reportReason = reason,
                targetId = reviewId
            )
            createReportUseCase(param)
                .onSuccess {
                    _event.emit(ReviewEvent.DataFetch.Success)
                }
                .onFailure { e ->
                    _event.emit(
                        ReviewEvent.DataFetch.Error(
                            userMessage = "신고 접수에 실패했습니다.",
                            exceptionMessage = e.message
                        )
                    )
                }
        }
    }

    private fun toHospitalReview(item: ReviewSearchItem, currentUserNickname: String?): HospitalReview {
        return HospitalReview(
            id = item.id,
            isReceiptVerified = item.receiptCheck,
            treatment = item.treatmentService,
            animalType = item.animalType,
            detailAnimalType = item.detailAnimalType,
            content = item.reviewContent,
            rating = item.totalRating,
            date = item.createDate,
            likeCount = item.likeCount,
            isLiked = item.liked,
            imageUrls = item.images,
            author = item.userNickname,
            price = item.totalPrice,
            hospitalName = item.hospitalName,
            isAuthor = (currentUserNickname == item.userNickname)
        )
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 5
    }
}
