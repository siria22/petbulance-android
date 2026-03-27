package com.petbulance.presentation.screen.feature.mypage.sections.activity.comments

import com.petbulance.domain.model.feature.community.comment.MyCommentListRes
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPageCommentsArgument(
    val intent: (MyPageCommentsIntent) -> Unit,
    val dataState: MyPageCommentsDataState,
    val screenState: MyPageCommentsScreenState,
    val event: SharedFlow<MyPageCommentsEvent>
)

sealed class MyPageCommentsDataState {
    data object Init : MyPageCommentsDataState()
    data object Loading : MyPageCommentsDataState()
    data class Loaded(
        val comments: List<MyCommentListRes>,
        val hasNext: Boolean = false
    ) : MyPageCommentsDataState()
}

sealed class MyPageCommentsScreenState {
    data object Init : MyPageCommentsScreenState()
    data class Normal(
        val isSelectionMode: Boolean = false,
        val selectedIds: Set<Long> = emptySet()
    ) : MyPageCommentsScreenState()
}

sealed class MyPageCommentsIntent {
    data object LoadData : MyPageCommentsIntent()
    data object LoadMore : MyPageCommentsIntent()
    data object Refresh : MyPageCommentsIntent()

    data class ToggleSelectionMode(val enabled: Boolean) : MyPageCommentsIntent()
    data class ToggleCommentSelection(val commentId: Long) : MyPageCommentsIntent()
    data object SelectAll : MyPageCommentsIntent()
    data object DeleteSelected : MyPageCommentsIntent()
}

sealed class MyPageCommentsEvent {
    sealed class DataFetch : MyPageCommentsEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    sealed class Comment : MyPageCommentsEvent() {
        data object DeleteSuccess : Comment()

        data class DeleteFailed(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : Comment(), ErrorEvent
    }
}
