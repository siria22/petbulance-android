package com.petbulance.presentation.screen.feature.review.search

import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
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

    data class UpdateFilter(val queryModel: HospitalSearchQueryUiModel) : ReviewSearchIntent()

    data class ChangeSort(val sortType: ReviewSortType) : ReviewSearchIntent()
    data object ToggleReceipt : ReviewSearchIntent()
    data object TogglePhotoReview : ReviewSearchIntent()
    data object Refresh : ReviewSearchIntent()
    data class ReportReview(val reviewId: Long, val reason: String) : ReviewSearchIntent()
    data class DeleteReview(val reviewId: Long) : ReviewSearchIntent()
}

sealed class ReviewSearchEvent {
    data class Error(val message: String) : ReviewSearchEvent()
    data object ReportSuccess : ReviewSearchEvent()
    data object DeleteSuccess : ReviewSearchEvent()
}