package com.petbulance.presentation.screen.feature.community.detail

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import android.util.Log
import com.petbulance.presentation.screen.feature.community.detail.views.PostDetailMainView
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.hooks.PhotoPickerMediaType
import com.petbulance.presentation.utils.hooks.rememberPhotoPickerLauncher
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun PostDetailScreen(
    navController: NavController,
    argument: PostDetailArgument,
    data: PostDetailData
) {
    Log.d("PostDetailScreen", "PostDetailScreen recomposed with ${data.comments.size} comments")
    
    var showMoreOption by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showReportReasonDialog by remember { mutableStateOf(false) }
    var selectedReportReason by remember { mutableStateOf("") }

    var showReportSuccessToast by remember { mutableStateOf(false) }
    var reportToastMessage by remember { mutableStateOf("") }

    var pendingDeleteJob by remember { mutableStateOf<(() -> Unit)?>(null) }
    var showDeleteToast by remember { mutableStateOf(false) }

    var commentText by remember { mutableStateOf("") }
    var isCommentSecret by remember { mutableStateOf(false) }
    var replyToCommentId by remember { mutableStateOf<Long?>(null) }
    var replyToNickname by remember { mutableStateOf<String?>(null) }

    var selectedCommentId by remember { mutableStateOf<Long?>(null) }
    var isSelectedCommentMine by remember { mutableStateOf(false) }
    var showCommentMoreOption by remember { mutableStateOf(false) }
    var showCommentDeleteDialog by remember { mutableStateOf(false) }
    var showCommentReportBottomSheet by remember { mutableStateOf(false) }
    var showCommentReportReasonDialog by remember { mutableStateOf(false) }
    var selectedCommentReportReason by remember { mutableStateOf("") }

    val context = LocalContext.current
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

                is PostDetailEvent.DeleteSuccess -> {
                    showDeleteToast = true
                    navController.navigate(ScreenDestinations.Community.route) {
                        popUpTo(ScreenDestinations.Community.route) { inclusive = false }
                    }

                    pendingDeleteJob = {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "postDeleted",
                            true
                        )
                    }

                    delay(3000)
                    pendingDeleteJob?.invoke()
                    pendingDeleteJob = null
                    showDeleteToast = false
                }

                is PostDetailEvent.ReportFirstSuccess -> {
                    showReportReasonDialog = false
                    reportToastMessage = "[신고 완료] 운영자 검토 후 조치 예정입니다"
                    showReportSuccessToast = true
                }

                is PostDetailEvent.ReportDuplicate -> {
                    showReportReasonDialog = false
                    reportToastMessage = "이미 신고한 게시글입니다"
                    showReportSuccessToast = true
                }

                is PostDetailEvent.CommentCreateSuccess -> {
                    commentText = ""
                    isCommentSecret = false
                    replyToCommentId = null
                    replyToNickname = null
                    argument.intent(PostDetailIntent.ClearCommentImage)
                }

                is PostDetailEvent.CommentDeleteSuccess -> {
                    showCommentDeleteDialog = false
                    showDeleteToast = true
                    reportToastMessage = "댓글을 삭제했어요"
                    showReportSuccessToast = true
                }

                is PostDetailEvent.CommentReportSuccess -> {
                    showCommentReportReasonDialog = false
                    reportToastMessage = "[신고 완료] 운영자 검토 후 조치 예정입니다"
                    showReportSuccessToast = true
                }

                is PostDetailEvent.CommentReportDuplicate -> {
                    showCommentReportReasonDialog = false
                    reportToastMessage = "이미 신고한 댓글입니다"
                    showReportSuccessToast = true
                }

                is PostDetailEvent.CommentUpdateSuccess -> {
                    reportToastMessage = "댓글을 수정했어요"
                    showReportSuccessToast = true
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
    }

    LaunchedEffect(data.editingCommentId) {
        if (data.editingCommentId != null) {
            commentText = data.editingCommentContent
            isCommentSecret = data.editingCommentIsSecret
            replyToCommentId = null
            replyToNickname = null
        }
    }

    LaunchedEffect(showReportSuccessToast) {
        if (showReportSuccessToast) {
            delay(3000)
            showReportSuccessToast = false
        }
    }

    PostDetailMainView(
        navController = navController,
        data = data,
        showReportSuccessToast = showReportSuccessToast,
        reportToastMessage = reportToastMessage,
        showDeleteToast = showDeleteToast,
        onCloseReportToast = { showReportSuccessToast = false },
        onCancelDelete = {
            pendingDeleteJob = null
            showDeleteToast = false
            navController.navigate(ScreenDestinations.Community.PostDetail.createRoute(data.postId)) {
                popUpTo(ScreenDestinations.Community.route) { inclusive = false }
            }
        },
        onKebabMenuClick = { showMoreOption = true },
        onLikeClick = { argument.intent(PostDetailIntent.ToggleLike) },
        onDeleteOptionClick = {
            showMoreOption = false
            showDeleteConfirmDialog = true
        },
        onReportOptionClick = {
            showMoreOption = false
            showReportReasonDialog = true
        },
        onDismissMoreOption = { showMoreOption = false },
        showMoreOption = showMoreOption,
        showDeleteConfirmDialog = showDeleteConfirmDialog,
        onConfirmDelete = {
            showDeleteConfirmDialog = false
            argument.intent(PostDetailIntent.DeletePost)
        },
        onDismissDeleteDialog = { showDeleteConfirmDialog = false },
        showReportReasonDialog = showReportReasonDialog,
        selectedReportReason = selectedReportReason,
        onReasonSelected = { selectedReportReason = it },
        onSubmitReport = {
            showReportReasonDialog = false
            argument.intent(PostDetailIntent.ReportPost(selectedReportReason))
        },
        onDismissReportReasonDialog = { showReportReasonDialog = false },
        commentText = commentText,
        onCommentTextChange = { commentText = it },
        isCommentSecret = isCommentSecret,
        onCommentSecretToggle = { isCommentSecret = !isCommentSecret },
        replyToNickname = replyToNickname,
        onCancelReply = {
            if (data.editingCommentId != null) {
                argument.intent(PostDetailIntent.CancelEditComment)
                commentText = ""
                isCommentSecret = false
            } else {
                replyToCommentId = null
                replyToNickname = null
            }
        },
        onCommentSubmit = {
            if (data.editingCommentId != null) {
                argument.intent(
                    PostDetailIntent.UpdateComment(
                        commentId = data.editingCommentId,
                        content = commentText,
                        imageUrl = data.commentImageUrl,
                        isSecret = isCommentSecret
                    )
                )
            } else {
                argument.intent(
                    PostDetailIntent.CreateComment(
                        content = commentText,
                        parentId = replyToCommentId,
                        mentionUserNickname = replyToNickname,
                        imageUrl = data.commentImageUrl,
                        isSecret = isCommentSecret
                    )
                )
            }
        },
        onCommentReplyClick = { commentId, nickname ->
            replyToCommentId = commentId
            replyToNickname = nickname
        },
        onCommentMenuClick = { commentId, isMine ->
            selectedCommentId = commentId
            isSelectedCommentMine = isMine
            showCommentMoreOption = true
        },
        onLoadMoreComments = { argument.intent(PostDetailIntent.LoadMoreComments) },
        onImageAttachClick = { photoPickerLauncher() },
        onImageRemoveClick = { argument.intent(PostDetailIntent.ClearCommentImage) },
        commentImageUri = data.commentImageUri,
        showCommentMoreOption = showCommentMoreOption,
        isSelectedCommentMine = isSelectedCommentMine,
        onCommentEditOptionClick = {
            showCommentMoreOption = false
            val comment = data.comments.find { it.commentId == selectedCommentId }
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
        onCommentDeleteOptionClick = {
            showCommentMoreOption = false
            showCommentDeleteDialog = true
        },
        onCommentReportOptionClick = {
            showCommentMoreOption = false
            showCommentReportBottomSheet = true
        },
        onDismissCommentMoreOption = { showCommentMoreOption = false },
        showCommentDeleteDialog = showCommentDeleteDialog,
        onConfirmCommentDelete = {
            selectedCommentId?.let { argument.intent(PostDetailIntent.DeleteComment(it)) }
        },
        onDismissCommentDeleteDialog = { showCommentDeleteDialog = false },
        showCommentReportBottomSheet = showCommentReportBottomSheet,
        onCommentReportBottomSheetClick = {
            showCommentReportBottomSheet = false
            showCommentReportReasonDialog = true
        },
        onDismissCommentReportBottomSheet = { showCommentReportBottomSheet = false },
        showCommentReportReasonDialog = showCommentReportReasonDialog,
        selectedCommentReportReason = selectedCommentReportReason,
        onCommentReasonSelected = { selectedCommentReportReason = it },
        onSubmitCommentReport = {
            selectedCommentId?.let {
                argument.intent(PostDetailIntent.ReportComment(it, selectedCommentReportReason))
            }
        },
        onDismissCommentReportReasonDialog = { showCommentReportReasonDialog = false }
    )
}
