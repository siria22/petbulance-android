package com.petbulance.presentation.screen.feature.review.search

import androidx.lifecycle.viewModelScope
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.domain.usecase.feature.hospital.review.AddRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.review.DeleteRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.review.GetRecentSearchKeywordsUseCase
import com.petbulance.domain.usecase.feature.hospital.review.SearchReviewUseCase
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
    private val deleteRecentKeywordUseCase: DeleteRecentSearchKeywordUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<ReviewSearchState>(ReviewSearchState.Init)
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ReviewSearchEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _isSearchResultMode = MutableStateFlow(false)
    val isSearchResultMode = _isSearchResultMode.asStateFlow()

    private val _searchResults = MutableStateFlow<List<HospitalReview>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private var currentCursorId: Long? = null
    private var hasNextPage = true

    private val _searchQueryModel = MutableStateFlow(HospitalSearchQueryUiModel.empty)
    val searchQueryModel = _searchQueryModel.asStateFlow()

    private val _selectedSort = MutableStateFlow(ReviewSortType.LATEST)
    val selectedSort = _selectedSort.asStateFlow()

    private val _isReceiptVerified = MutableStateFlow(false)
    val isReceiptVerified = _isReceiptVerified.asStateFlow()

    private val _isPhotoReview = MutableStateFlow(false)
    val isPhotoReview = _isPhotoReview.asStateFlow()

    val recentKeywords = getRecentKeywordsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onIntent(intent: ReviewSearchIntent) {
        when (intent) {
            is ReviewSearchIntent.UpdateQuery -> {
                _searchQueryModel.update { it.copy(query = intent.query) }
                if (intent.query.isEmpty()) _isSearchResultMode.value = false
            }

            is ReviewSearchIntent.Search -> {
                performSearch()
            }

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
                _searchQueryModel.value = intent.queryModel
                performSearch()
            }

            is ReviewSearchIntent.Refresh -> {
                _searchQueryModel.value = HospitalSearchQueryUiModel.empty
                _selectedSort.value = ReviewSortType.LATEST
                _isReceiptVerified.value = false
                _isPhotoReview.value = false
                performSearch()
            }

            is ReviewSearchIntent.ToggleReceipt -> {
                _isReceiptVerified.value = !_isReceiptVerified.value
                performSearch()
            }

            is ReviewSearchIntent.TogglePhotoReview -> {
                _isPhotoReview.value = !_isPhotoReview.value
                performSearch()
            }

            else -> {}
        }
    }

    private fun performSearch(isLoadMore: Boolean = false) {
        val currentQueryString = _searchQueryModel.value.query
        if (currentQueryString.isNullOrBlank()) return

        launch {
            if (!isLoadMore) {
                _state.value = ReviewSearchState.Loading
                currentCursorId = null
                _searchResults.value = emptyList()
                _isSearchResultMode.value = true
                addRecentKeywordUseCase(currentQueryString)
            }

            searchReviewUseCase(currentQueryString, currentCursorId)
                .onSuccess { pagingData ->
                    val newItems = pagingData.items.map { it.toHospitalReview() }
                    _searchResults.update { if (isLoadMore) it + newItems else newItems }
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
        price = this.totalPrice
    )
}