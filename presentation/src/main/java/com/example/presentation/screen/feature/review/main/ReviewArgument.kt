package com.example.presentation.screen.feature.review.main

import com.example.domain.model.type.AnimalCategory
import com.example.domain.model.type.Region
import com.example.domain.model.type.ReviewSortType
import kotlinx.coroutines.flow.SharedFlow

data class ReviewArgument(
    val state: ReviewState,
    val intent: (ReviewIntent) -> Unit,
    val event: SharedFlow<ReviewEvent>
)

sealed interface ReviewState {
    data object Init : ReviewState
    data object Loading : ReviewState
}

sealed interface ReviewIntent {
    // 필터 변경
    data class ChangeRegion(val region: Region, val district: String) : ReviewIntent
    data class ChangeAnimalType(val animalType: AnimalCategory) : ReviewIntent
    data class ChangeSort(val sortType: ReviewSortType) : ReviewIntent

    // 토글 필터
    data object ToggleReceipt : ReviewIntent
    data object TogglePhotoReview : ReviewIntent

    // 페이지네이션
    data object LoadMore : ReviewIntent

    // 초기화 및 기타
    data object Refresh : ReviewIntent
}

sealed interface ReviewEvent {
    data class ShowErrorToast(val message: String) : ReviewEvent
}