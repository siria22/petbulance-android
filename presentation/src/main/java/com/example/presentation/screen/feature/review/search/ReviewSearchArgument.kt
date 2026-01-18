package com.example.presentation.screen.feature.review.search

import com.example.domain.model.type.AnimalCategory
import com.example.domain.model.type.Region
import com.example.domain.model.type.ReviewSortType
import kotlinx.coroutines.flow.SharedFlow

data class ReviewSearchArgument(
    val state: ReviewSearchState,
    val intent: (ReviewSearchIntent) -> Unit,
    val event: SharedFlow<ReviewSearchEvent>
)

sealed class ReviewSearchState {
    data object Init : ReviewSearchState()
    data object Loading : ReviewSearchState()
}

sealed class ReviewSearchIntent {
    data class UpdateQuery(val query: String) : ReviewSearchIntent()
    data object Search : ReviewSearchIntent()
    data class DeleteRecentKeyword(val keyword: String) : ReviewSearchIntent()
    data object ClearAllRecentKeywords : ReviewSearchIntent()
    data object LoadMore : ReviewSearchIntent()

    data class ChangeRegion(val region: Region, val district: String) : ReviewSearchIntent()
    data class ChangeAnimalType(val animalType: AnimalCategory) : ReviewSearchIntent()
    data class ChangeSort(val sortType: ReviewSortType) : ReviewSearchIntent()
    data object ToggleReceipt : ReviewSearchIntent()
    data object TogglePhotoReview : ReviewSearchIntent()
    data object Refresh : ReviewSearchIntent()
}

sealed class ReviewSearchEvent {
    data class Error(val message: String) : ReviewSearchEvent()
}