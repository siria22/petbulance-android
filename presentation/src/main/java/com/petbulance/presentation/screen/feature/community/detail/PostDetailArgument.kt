package com.petbulance.presentation.screen.feature.community.detail

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class PostDetailArgument(
    val intent: (PostDetailIntent) -> Unit,
    val dataState: PostDetailDataState,
    val screenState: PostDetailScreenState,
    val event: SharedFlow<PostDetailEvent>
)

sealed class PostDetailDataState {
    data object Init : PostDetailDataState()
    data object OnProgress : PostDetailDataState()
}

sealed class PostDetailScreenState {
    data object Init : PostDetailScreenState()
}

sealed class PostDetailIntent {
    data object LoadDetail : PostDetailIntent()
    data object DeletePost : PostDetailIntent()
    data class ReportPost(val reason: String) : PostDetailIntent()
    data object ToggleLike : PostDetailIntent()
    
    data object LoadComments : PostDetailIntent()
    data object LoadMoreComments : PostDetailIntent()
    data class CreateComment(
        val content: String,
        val parentId: Long? = null,
        val mentionUserNickname: String? = null,
        val imageUrl: String? = null,
        val isSecret: Boolean = false
    ) : PostDetailIntent()
    data class DeleteComment(val commentId: Long) : PostDetailIntent()
    data class ReportComment(val commentId: Long, val reason: String) : PostDetailIntent()
    data class SelectCommentImage(val uri: android.net.Uri) : PostDetailIntent()
    data object ClearCommentImage : PostDetailIntent()
    data class StartEditComment(val commentId: Long, val content: String?, val imageUrl: String?, val isSecret: Boolean) : PostDetailIntent()
    data object CancelEditComment : PostDetailIntent()
    data class UpdateComment(val commentId: Long, val content: String, val imageUrl: String?, val isSecret: Boolean) : PostDetailIntent()
}

sealed class PostDetailEvent {
    sealed class DataFetch : PostDetailEvent() {
        data class Error(
            override val userMessage: String = "게시글을 불러올 수 없습니다",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    data object DeleteSuccess : PostDetailEvent()
    data object ReportFirstSuccess : PostDetailEvent()
    data object ReportDuplicate : PostDetailEvent()
    
    data object CommentCreateSuccess : PostDetailEvent()
    data class CommentCreateError(
        override val userMessage: String = "댓글 등록에 실패했습니다",
        override val exceptionMessage: String?,
        override val displayType: ErrorDisplayType = ErrorDisplayType.Common
    ) : PostDetailEvent(), ErrorEvent
    
    data object CommentDeleteSuccess : PostDetailEvent()
    data class CommentDeleteError(
        override val userMessage: String = "댓글 삭제에 실패했습니다",
        override val exceptionMessage: String?,
        override val displayType: ErrorDisplayType = ErrorDisplayType.Common
    ) : PostDetailEvent(), ErrorEvent
    
    data object CommentReportSuccess : PostDetailEvent()
    data object CommentReportDuplicate : PostDetailEvent()
    data class CommentReportError(
        override val userMessage: String = "댓글 신고에 실패했습니다",
        override val exceptionMessage: String?,
        override val displayType: ErrorDisplayType = ErrorDisplayType.Common
    ) : PostDetailEvent(), ErrorEvent

    data object CommentUpdateSuccess : PostDetailEvent()

    data class CommentUpdateError(
        override val userMessage: String = "댓글 수정에 실패했습니다",
        override val exceptionMessage: String?,
        override val displayType: ErrorDisplayType = ErrorDisplayType.Common
    ) : PostDetailEvent(), ErrorEvent
}
