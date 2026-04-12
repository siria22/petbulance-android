package com.petbulance.presentation.screen.feature.review.main

import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.Region
import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
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

    // 신고
    data class ReportReview(val reviewId: Long, val reason: String) : ReviewIntent
}

sealed interface ReviewEvent {

    sealed class DataFetch : ReviewEvent {
        data object Success: DataFetch()
        data class Error(
            override val userMessage: String,
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}