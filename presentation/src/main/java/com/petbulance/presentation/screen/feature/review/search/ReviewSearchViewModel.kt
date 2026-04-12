package com.petbulance.presentation.screen.feature.review.search

import androidx.lifecycle.viewModelScope
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.feature.support.report.ReportParam
import com.petbulance.domain.model.type.ReportType
import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.domain.usecase.feature.hospital.review.AddRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.review.DeleteRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.review.GetRecentSearchKeywordsUseCase
import com.petbulance.domain.usecase.feature.hospital.review.SearchReviewUseCase
import com.petbulance.domain.usecase.feature.support.report.CreateReportUseCase
import com.petbulance.domain.usecase.feature.user.user.GetMyInfoUseCase
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class ReviewSearchViewModel @Inject constructor(
    private val searchReviewUseCase: SearchReviewUseCase,
    private val getRecentKeywordsUseCase: GetRecentSearchKeywordsUseCase,
    private val addRecentKeywordUseCase: AddRecentSearchKeywordUseCase,
    private val deleteRecentKeywordUseCase: DeleteRecentSearchKeywordUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val createReportUseCase: CreateReportUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<ReviewSearchState>(ReviewSearchState.Init)
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ReviewSearchEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _searchData = MutableStateFlow(ReviewSearchData.empty)
    val searchData = _searchData.asStateFlow()

    private var currentCursorId: Long? = null
    private var hasNextPage = true
    private var currentUserNickname: String? = null

    val recentKeywords = getRecentKeywordsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadCurrentUserInfo()
    }

    private fun loadCurrentUserInfo() {
        launch {
            getMyInfoUseCase().onSuccess { userInfo ->
                currentUserNickname = userInfo.nickname
            }
        }
    }

    fun onIntent(intent: ReviewSearchIntent) {
        when (intent) {
            is ReviewSearchIntent.UpdateQuery -> {
                _searchData.update { it.copy(searchQueryModel = it.searchQueryModel.copy(query = intent.query)) }
                if (intent.query.isEmpty()) _searchData.update { it.copy(isSearchResultMode = false) }
            }

            is ReviewSearchIntent.Search -> performSearch()

            is ReviewSearchIntent.DeleteRecentKeyword -> {
                launch { deleteRecentKeywordUseCase(intent.keyword) }
            }

            is ReviewSearchIntent.ClearAllRecentKeywords -> {
                // TODO: 전체 삭제 로직 (필요 시 Repository 추가)
            }

            is ReviewSearchIntent.LoadMore -> {
                if (hasNextPage) performSearch(isLoadMore = true)
            }

            is ReviewSearchIntent.UpdateFilter -> {
                _searchData.update { it.copy(searchQueryModel = intent.queryModel) }
                performSearch()
            }

            is ReviewSearchIntent.Refresh -> {
                _searchData.update {
                    it.copy(
                        searchQueryModel = HospitalSearchQueryUiModel.empty,
                        selectedSort = ReviewSortType.LATEST,
                        isReceiptVerified = false,
                        isPhotoReview = false
                    )
                }
                performSearch()
            }

            is ReviewSearchIntent.ToggleReceipt -> {
                _searchData.update { it.copy(isReceiptVerified = !it.isReceiptVerified) }
                performSearch()
            }

            is ReviewSearchIntent.TogglePhotoReview -> {
                _searchData.update { it.copy(isPhotoReview = !it.isPhotoReview) }
                performSearch()
            }

            is ReviewSearchIntent.ReportReview -> reportReview(intent.reviewId, intent.reason)

            is ReviewSearchIntent.ChangeSort -> {
                _searchData.update { it.copy(selectedSort = intent.sortType) }
                performSearch()
            }
        }
    }

    private fun performSearch(isLoadMore: Boolean = false) {
        val currentQueryString = _searchData.value.searchQueryModel.query
        if (currentQueryString.isNullOrBlank()) return

        launch {
            if (!isLoadMore) {
                _state.value = ReviewSearchState.Loading
                currentCursorId = null
                _searchData.update { it.copy(searchResults = emptyList(), isSearchResultMode = true) }
                addRecentKeywordUseCase(currentQueryString)
            }

            searchReviewUseCase(currentQueryString, currentCursorId)
                .onSuccess { pagingData ->
                    val newItems = pagingData.items.map { it.toHospitalReview() }
                    _searchData.update {
                        it.copy(searchResults = if (isLoadMore) it.searchResults + newItems else newItems)
                    }
                    currentCursorId = pagingData.nextCursorId
                    hasNextPage = pagingData.hasNext
                    _state.value = ReviewSearchState.Init
                }
                .onFailure {
                    _state.value = ReviewSearchState.Init
                    _eventFlow.emit(ReviewSearchEvent.Error(it.message ?: "검색 실패"))
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
                    _eventFlow.emit(ReviewSearchEvent.ReportSuccess)
                }
                .onFailure {
                    _eventFlow.emit(ReviewSearchEvent.Error("신고 접수에 실패했습니다."))
                }
        }
    }

    private fun ReviewSearchItem.toHospitalReview() = HospitalReview(
        id = this.id,
        hospitalName = this.hospitalName,
        isReceiptVerified = this.receiptCheck,
        treatment = this.treatmentService,
        animalType = this.animalType,
        detailAnimalType = this.detailAnimalType,
        content = this.reviewContent,
        rating = this.totalRating,
        date = this.createDate,
        likeCount = this.likeCount,
        isLiked = this.liked,
        imageUrls = this.images,
        author = this.userNickname,
        price = this.totalPrice,
        isAuthor = (currentUserNickname == this.userNickname)
    )
}
