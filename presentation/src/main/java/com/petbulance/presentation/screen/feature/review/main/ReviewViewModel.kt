package com.petbulance.presentation.screen.feature.review.main

import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.domain.model.type.Region
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

    // Data States
    private val _reviews = MutableStateFlow<List<HospitalReview>>(emptyList())
    val reviews = _reviews.asStateFlow()

    private val _selectedRegion = MutableStateFlow<Region?>(null)
    val selectedRegion = _selectedRegion.asStateFlow()

    private val _selectedDistrict = MutableStateFlow<String?>(null)
    val selectedDistrict = _selectedDistrict.asStateFlow()

    private val _selectedAnimalType = MutableStateFlow<AnimalCategory?>(null)
    val selectedAnimalType = _selectedAnimalType.asStateFlow()

    private val _selectedSort = MutableStateFlow(ReviewSortType.LATEST)
    val selectedSort = _selectedSort.asStateFlow()

    private val _isReceiptVerified = MutableStateFlow(false)
    val isReceiptVerified = _isReceiptVerified.asStateFlow()

    private val _isPhotoReview = MutableStateFlow(false)
    val isPhotoReview = _isPhotoReview.asStateFlow()

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage = _isLoadingNextPage.asStateFlow()

    // Current User Info
    private var currentUserNickname: String? = null

    // Pagination Info
    private var currentCursorId: Long? = null
    private var hasNextPage: Boolean = true
    private val pageSize = 5

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
                _selectedRegion.value = intent.region
                _selectedDistrict.value = intent.district
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.ChangeAnimalType -> {
                _selectedAnimalType.value = intent.animalType
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.ChangeSort -> {
                _selectedSort.value = intent.sortType
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.ToggleReceipt -> {
                _isReceiptVerified.value = !_isReceiptVerified.value
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.TogglePhotoReview -> {
                _isPhotoReview.value = !_isPhotoReview.value
                loadReviews(isRefresh = true)
            }

            is ReviewIntent.LoadMore -> {
                if (hasNextPage && !_isLoadingNextPage.value) {
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
        launch {
            if (isRefresh) {
                _state.value = ReviewState.Loading
                currentCursorId = null
                hasNextPage = true
                _reviews.value = emptyList()
            } else {
                _isLoadingNextPage.value = true
            }

            val selectedCategory = _selectedAnimalType.value
            val animalTypesParam =
                if (selectedCategory == null || selectedCategory == AnimalCategory.ALL) {
                    null
                } else {
                    AnimalSpecies.entries
                        .filter { it.category == selectedCategory }
                        .map { it.name }
                }

            val result = filterReviewUseCase(
                region = if (_selectedRegion.value != null) "${_selectedRegion.value!!.name} ${_selectedDistrict.value ?: ""}" else null,
                animalTypes = animalTypesParam, // 수정된 파라미터 전달
                isReceipt = if (_isReceiptVerified.value) true else null,
                cursorId = currentCursorId,
                size = pageSize
            )

            result.onSuccess { pagingData ->
                currentCursorId = pagingData.nextCursorId
                hasNextPage = pagingData.hasNext

                var newReviews = pagingData.items.map { item ->
                    toHospitalReview(item, currentUserNickname)
                }
                
                // 사진 후기 필터 적용
                if (_isPhotoReview.value) {
                    newReviews = newReviews.filter { it.imageUrls.isNotEmpty() }
                }

                if (isRefresh) {
                    _reviews.value = newReviews
                    _state.value = ReviewState.Init
                } else {
                    _reviews.update { it + newReviews }
                    _isLoadingNextPage.value = false
                }
            }.onFailure { e ->
                _state.value = ReviewState.Init
                _isLoadingNextPage.value = false
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
                    _event.emit(
                        ReviewEvent.DataFetch.Success
                    )
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
}