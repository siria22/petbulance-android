package com.petbulance.presentation.screen.feature.community.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class PostDialogState {
    var showMoreOption by mutableStateOf(false)
    var showDeleteConfirm by mutableStateOf(false)
    var showReportReason by mutableStateOf(false)
    var selectedReportReason by mutableStateOf("")

    fun openMoreOption() { showMoreOption = true }
    fun dismissMoreOption() { showMoreOption = false }

    fun openDeleteConfirm() {
        showMoreOption = false
        showDeleteConfirm = true
    }
    fun dismissDeleteConfirm() { showDeleteConfirm = false }
    fun confirmDelete() { showDeleteConfirm = false }

    fun openReportReason() {
        showMoreOption = false
        showReportReason = true
    }
    fun dismissReportReason() { showReportReason = false }
    fun submitReport() { showReportReason = false }
}

@Stable
class CommentDialogState {
    var selectedCommentId by mutableStateOf<Long?>(null)
    var isSelectedCommentMine by mutableStateOf(false)
    var showMoreOption by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)
    var showReportBottomSheet by mutableStateOf(false)
    var showReportReasonDialog by mutableStateOf(false)
    var selectedReportReason by mutableStateOf("")

    fun openMoreOption(commentId: Long, isMine: Boolean) {
        selectedCommentId = commentId
        isSelectedCommentMine = isMine
        showMoreOption = true
    }
    fun dismissMoreOption() { showMoreOption = false }

    fun openDeleteDialog() {
        showMoreOption = false
        showDeleteDialog = true
    }
    fun dismissDeleteDialog() { showDeleteDialog = false }

    fun openReportBottomSheet() {
        showMoreOption = false
        showReportBottomSheet = true
    }
    fun dismissReportBottomSheet() { showReportBottomSheet = false }

    fun openReportReasonDialog() {
        showReportBottomSheet = false
        showReportReasonDialog = true
    }
    fun dismissReportReasonDialog() { showReportReasonDialog = false }
}

@Stable
class CommentInputState {
    var text by mutableStateOf("")
    var isSecret by mutableStateOf(false)
    var replyToCommentId by mutableStateOf<Long?>(null)
    var replyToNickname by mutableStateOf<String?>(null)

    fun reset() {
        text = ""
        isSecret = false
        replyToCommentId = null
        replyToNickname = null
    }

    fun setReplyTarget(commentId: Long, nickname: String) {
        replyToCommentId = commentId
        replyToNickname = nickname
    }

    fun syncFromEditMode(content: String, isSecretComment: Boolean) {
        text = content
        isSecret = isSecretComment
        replyToCommentId = null
        replyToNickname = null
    }
}

@Stable
class ToastState {
    var showReportToast by mutableStateOf(false)
    var reportToastMessage by mutableStateOf("")
    var showDeleteToast by mutableStateOf(false)
    var pendingDeleteJob by mutableStateOf<(() -> Unit)?>(null)

    fun showMessage(message: String) {
        reportToastMessage = message
        showReportToast = true
    }

    fun dismissReportToast() { showReportToast = false }

    fun clearDeleteToast() {
        pendingDeleteJob = null
        showDeleteToast = false
    }
}

@Composable
fun rememberPostDialogState() = remember { PostDialogState() }

@Composable
fun rememberCommentDialogState() = remember { CommentDialogState() }

@Composable
fun rememberCommentInputState() = remember { CommentInputState() }

@Composable
fun rememberToastState() = remember { ToastState() }
