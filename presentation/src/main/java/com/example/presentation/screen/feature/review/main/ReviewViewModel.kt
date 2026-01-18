package com.example.presentation.screen.feature.review.main

import androidx.lifecycle.viewModelScope
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.feature.hospital.review.ReviewSearchItem
import com.example.domain.model.type.AnimalCategory
import com.example.domain.model.type.Region
import com.example.domain.model.type.ReviewSortType
import com.example.domain.repository.feature.hospital.ReviewRepository
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
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


    // Pagination Info
    private var currentCursorId: Long? = null
    private var hasNextPage: Boolean = true
    private val pageSize = 5

    init {
        loadReviews(isRefresh = true)
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
        }
    }

    private fun loadReviews(isRefresh: Boolean) {
        viewModelScope.launch {
            if (isRefresh) {
                _state.value = ReviewState.Loading
                currentCursorId = null
                hasNextPage = true
                _reviews.value = emptyList()
            } else {
                _isLoadingNextPage.value = true
            }

            // API Call
            val result = reviewRepository.filterReview(
                region = if (_selectedRegion.value != null) "${_selectedRegion.value!!.name} ${_selectedDistrict.value ?: ""}" else null,
                animalType = _selectedAnimalType.value?.name,
                isReceipt = if (_isReceiptVerified.value) true else null,
                cursorId = currentCursorId,
                size = pageSize
                // Note: sort and photo filter are not supported by the current API endpoint
            )

            result.onSuccess { pagingData ->
                currentCursorId = pagingData.nextCursorId
                hasNextPage = pagingData.hasNext

                val newReviews = pagingData.items.map { it.toHospitalReviewDummy() }

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
                _event.emit(ReviewEvent.ShowErrorToast(e.message ?: "알 수 없는 오류가 발생했습니다."))
            }
        }
    }

    private fun ReviewSearchItem.toHospitalReviewDummy(): HospitalReview {
        return HospitalReview(
            id = this.id,
            isReceiptVerified = this.isReceiptVerified,
            treatment = this.treatment,
            animalType = this.animalType,
            detailAnimalType = this.animalType,
            content = this.content,
            rating = this.rating,
            date = "2024.01.01",
            likeCount = 0,
            isLiked = false,
            imageUrls = emptyList(),
            author = "익명 사용자",
            price = 0
        )
    }
}