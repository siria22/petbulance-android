package com.petbulance.presentation.screen.feature.community.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.petbulance.presentation.screen.feature.community.detail.views.PostDetailMainView
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay

@Composable
fun PostDetailScreen(
    navController: NavController,
    argument: PostDetailArgument,
    data: PostDetailData
) {
    var showMoreOption by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showReportBottomSheet by remember { mutableStateOf(false) }
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
                        navController.previousBackStackEntry?.savedStateHandle?.set("postDeleted", true)
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

                is PostDetailEvent.CommentCreateError,
                is PostDetailEvent.CommentDeleteError,
                is PostDetailEvent.CommentReportError -> {
                    // ErrorEvent는 BaseViewModel에서 처리
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        argument.intent(PostDetailIntent.LoadComments)
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
            showReportBottomSheet = true
        },
        onDismissMoreOption = { showMoreOption = false },
        showMoreOption = showMoreOption,
        showDeleteConfirmDialog = showDeleteConfirmDialog,
        onConfirmDelete = {
            showDeleteConfirmDialog = false
            argument.intent(PostDetailIntent.DeletePost)
        },
        onDismissDeleteDialog = { showDeleteConfirmDialog = false },
        showReportBottomSheet = showReportBottomSheet,
        onReportBottomSheetClick = {
            showReportBottomSheet = false
            showReportReasonDialog = true
        },
        onDismissReportBottomSheet = { showReportBottomSheet = false },
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
            replyToCommentId = null
            replyToNickname = null
        },
        onCommentSubmit = {
            argument.intent(
                PostDetailIntent.CreateComment(
                    content = commentText,
                    parentId = replyToCommentId,
                    mentionUserNickname = replyToNickname,
                    imageUrl = null,
                    isSecret = isCommentSecret
                )
            )
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
        showCommentMoreOption = showCommentMoreOption,
        isSelectedCommentMine = isSelectedCommentMine,
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
