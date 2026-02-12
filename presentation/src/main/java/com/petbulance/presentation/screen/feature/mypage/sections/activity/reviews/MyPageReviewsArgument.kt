package com.petbulance.presentation.screen.feature.mypage.sections.activity.reviews

import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPageReviewsArgument(
    val intent: (MyPageReviewsIntent) -> Unit,
    val dataState: MyPageReviewsDataState,
    val screenState: MyPageReviewsScreenState,
    val event: SharedFlow<MyPageReviewsEvent>
)

sealed class MyPageReviewsDataState {
    data object Init : MyPageReviewsDataState()
    data object Loading : MyPageReviewsDataState()
    data class Loaded(
        val reviews: List<MyReview>,
        val nextCursorId: Long? = null,
        val hasNext: Boolean = false
    ) : MyPageReviewsDataState()
}

sealed class MyPageReviewsScreenState {
    data object Init : MyPageReviewsScreenState()
    data class Normal(
        val isSelectionMode: Boolean = false,
        val selectedIds: Set<Long> = emptySet()
    ) : MyPageReviewsScreenState()
}

sealed class MyPageReviewsIntent {
    data object LoadData : MyPageReviewsIntent()
    data object LoadMore : MyPageReviewsIntent()
    data object Refresh : MyPageReviewsIntent()

    data class ToggleSelectionMode(val enabled: Boolean) : MyPageReviewsIntent()
    data class ToggleReviewSelection(val reviewId: Long) : MyPageReviewsIntent()
    data object SelectAll : MyPageReviewsIntent()
    data object DeleteSelected : MyPageReviewsIntent()
}

sealed class MyPageReviewsEvent {
    sealed class DataFetch : MyPageReviewsEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    sealed class Review : MyPageReviewsEvent() {
        data object DeleteSuccess : Review()

        data class DeleteFailed(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}