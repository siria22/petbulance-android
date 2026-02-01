package com.petbulance.presentation.screen.feature.review.detail

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class ReviewDetailArgument(
    val intent: (ReviewDetailIntent) -> Unit,
    val dataState: ReviewDetailDataState,
    val screenState: ReviewDetailScreenState,
    val event: SharedFlow<ReviewDetailEvent>
)

sealed class ReviewDetailDataState {
    data object Init : ReviewDetailDataState()
    data object OnProgress : ReviewDetailDataState()
}

sealed class ReviewDetailScreenState {
    data object Init : ReviewDetailScreenState()
}

sealed class ReviewDetailIntent {
    data object DeleteReview : ReviewDetailIntent()
    data class ReportReview(val reason: String) : ReviewDetailIntent()
}

sealed class ReviewDetailEvent {
    sealed class DataFetch : ReviewDetailEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    data object DeleteSuccess : ReviewDetailEvent()
    data object ReportSuccess : ReviewDetailEvent()
}