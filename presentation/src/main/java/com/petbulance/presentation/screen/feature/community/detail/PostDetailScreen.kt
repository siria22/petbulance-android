package com.petbulance.presentation.screen.feature.community.detail

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.petbulance.presentation.screen.feature.community.detail.views.PostDetailMainView
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.hooks.PhotoPickerMediaType
import com.petbulance.presentation.utils.hooks.rememberPhotoPickerLauncher
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun PostDetailScreen(
    navController: NavController,
    argument: PostDetailArgument,
    data: PostDetailData
) {
    val postDialogState = rememberPostDialogState()
    val commentDialogState = rememberCommentDialogState()
    val commentInputState = rememberCommentInputState()
    val toastState = rememberToastState()

    val photoPickerLauncher = rememberPhotoPickerLauncher(
        multiple = false,
        maxItems = 1,
        mediaType = PhotoPickerMediaType.IMAGE
    ) { uris ->
        uris.firstOrNull()?.let { uri ->
            argument.intent(PostDetailIntent.SelectCommentImage(uri))
        }
    }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is PostDetailEvent.DataFetch.Error -> {
                    if (event.displayType == ErrorDisplayType.Custom) {
                        navController.safePopBackStack()
                    }
                }

                is PostDetailEvent.NavigateToEditPost -> {
                    navController.safeNavigate(
                        ScreenDestinations.Community.WritePost.createRoute(event.postId)
                    )
                }

                is PostDetailEvent.DeleteSuccess -> {
                    toastState.showDeleteToast = true
                    navController.navigate(ScreenDestinations.Community.route) {
                        popUpTo(ScreenDestinations.Community.route) { inclusive = false }
                    }

                    toastState.pendingDeleteJob = {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "postDeleted",
                            true
                        )
                    }

                    delay(3000)
                    toastState.pendingDeleteJob?.invoke()
                    toastState.pendingDeleteJob = null
                    toastState.showDeleteToast = false
                }

                is PostDetailEvent.ReportFirstSuccess -> {
                    postDialogState.showReportReason = false
                    toastState.showMessage("[신고 완료] 운영자 검토 후 조치 예정입니다")
                }

                is PostDetailEvent.ReportDuplicate -> {
                    postDialogState.showReportReason = false
                    toastState.showMessage("이미 신고한 게시글입니다")
                }

                is PostDetailEvent.CommentCreateSuccess -> {
                    commentInputState.reset()
                    argument.intent(PostDetailIntent.ClearCommentImage)
                }

                is PostDetailEvent.CommentDeleteSuccess -> {
                    commentDialogState.showDeleteDialog = false
                    toastState.showDeleteToast = true
                    toastState.showMessage("댓글을 삭제했어요")
                }

                is PostDetailEvent.CommentReportSuccess -> {
                    commentDialogState.showReportReasonDialog = false
                    toastState.showMessage("[신고 완료] 운영자 검토 후 조치 예정입니다")
                }

                is PostDetailEvent.CommentReportDuplicate -> {
                    commentDialogState.showReportReasonDialog = false
                    toastState.showMessage("이미 신고한 댓글입니다")
                }

                is PostDetailEvent.CommentUpdateSuccess -> {
                    commentInputState.text = ""
                    commentInputState.isSecret = false
                    toastState.showMessage("댓글을 수정했어요")
                }

                is PostDetailEvent.CommentCreateError,
                is PostDetailEvent.CommentDeleteError,
                is PostDetailEvent.CommentReportError,
                is PostDetailEvent.CommentUpdateError -> {
                    // ErrorEvent는 BaseViewModel에서 처리
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        argument.intent(PostDetailIntent.LoadComments)

        val showSnackbar = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>("showPostCreatedSnackbar") ?: false

        if (showSnackbar) {
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<Boolean>("showPostCreatedSnackbar")

            toastState.showMessage("작성이 완료되어 게시했어요")
        }
    }

    LaunchedEffect(data.editingCommentId) {
        if (data.editingCommentId != null) {
            commentInputState.syncFromEditMode(data.editingCommentContent, data.editingCommentIsSecret)
        }
    }

    LaunchedEffect(toastState.showReportToast) {
        if (toastState.showReportToast) {
            delay(3000)
            toastState.showReportToast = false
        }
    }

    PostDetailMainView(
        navController = navController,
        data = data,
        postDialogState = postDialogState,
        commentDialogState = commentDialogState,
        commentInputState = commentInputState,
        toastState = toastState,
        onLikeClick = { argument.intent(PostDetailIntent.ToggleLike) },
        onEditOptionClick = { argument.intent(PostDetailIntent.NavigateToEdit) },
        onConfirmDelete = {
            postDialogState.confirmDelete()
            argument.intent(PostDetailIntent.DeletePost)
        },
        onSubmitReport = {
            postDialogState.submitReport()
            argument.intent(PostDetailIntent.ReportPost(postDialogState.selectedReportReason))
        },
        onCancelDelete = {
            toastState.clearDeleteToast()
            navController.navigate(ScreenDestinations.Community.PostDetail.createRoute(data.postId)) {
                popUpTo(ScreenDestinations.Community.route) { inclusive = false }
            }
        },
        onCancelReply = {
            if (data.editingCommentId != null) {
                argument.intent(PostDetailIntent.CancelEditComment)
                commentInputState.text = ""
                commentInputState.isSecret = false
            } else {
                commentInputState.replyToCommentId = null
                commentInputState.replyToNickname = null
            }
        },
        onCommentSubmit = {
            if (data.editingCommentId != null) {
                argument.intent(
                    PostDetailIntent.UpdateComment(
                        commentId = data.editingCommentId,
                        content = commentInputState.text,
                        imageUrl = data.commentImageUrl,
                        isSecret = commentInputState.isSecret
                    )
                )
            } else {
                argument.intent(
                    PostDetailIntent.CreateComment(
                        content = commentInputState.text,
                        parentId = commentInputState.replyToCommentId,
                        mentionUserNickname = commentInputState.replyToNickname,
                        imageUrl = data.commentImageUrl,
                        isSecret = commentInputState.isSecret
                    )
                )
            }
        },
        onLoadMoreComments = { argument.intent(PostDetailIntent.LoadMoreComments) },
        onImageAttachClick = { photoPickerLauncher() },
        onImageRemoveClick = { argument.intent(PostDetailIntent.ClearCommentImage) },
        onCommentEditOptionClick = {
            commentDialogState.dismissMoreOption()
            val comment = data.comments.find { it.commentId == commentDialogState.selectedCommentId }
            comment?.let {
                argument.intent(
                    PostDetailIntent.StartEditComment(
                        commentId = it.commentId,
                        content = it.content,
                        imageUrl = it.imageUrl,
                        isSecret = it.isSecret
                    )
                )
            }
        },
        onConfirmCommentDelete = {
            commentDialogState.selectedCommentId?.let {
                argument.intent(PostDetailIntent.DeleteComment(it))
            }
        },
        onSubmitCommentReport = {
            commentDialogState.selectedCommentId?.let {
                argument.intent(PostDetailIntent.ReportComment(it, commentDialogState.selectedReportReason))
            }
        }
    )
}
