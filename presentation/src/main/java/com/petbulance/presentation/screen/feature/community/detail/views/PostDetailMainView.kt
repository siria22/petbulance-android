package com.petbulance.presentation.screen.feature.community.detail.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.util.Log
import android.content.Intent
import androidx.compose.material.icons.filled.Share
import coil.compose.AsyncImage
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.ThickDivider
import com.petbulance.presentation.component.ui.atom.BasicChip
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeSmall
import com.petbulance.presentation.component.ui.molecule.WarningDialog
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXXS
import com.petbulance.presentation.screen.feature.community.detail.PostDetailData
import com.petbulance.presentation.screen.feature.community.detail.composables.CommentDeleteBottomSheet
import com.petbulance.presentation.screen.feature.community.detail.composables.CommentDeleteConfirmDialog
import com.petbulance.presentation.screen.feature.community.detail.composables.CommentInputArea
import com.petbulance.presentation.screen.feature.community.detail.composables.CommentListView
import com.petbulance.presentation.screen.feature.community.detail.composables.CommentReportBottomSheet
import com.petbulance.presentation.screen.feature.community.detail.composables.PostDeleteBottomSheet
import com.petbulance.presentation.screen.feature.community.detail.composables.PostReportBottomSheet
import com.petbulance.presentation.screen.feature.community.detail.composables.PostReportReasonDialog
import com.petbulance.presentation.utils.nav.safePopBackStack

@Composable
fun PostDetailMainView(
    navController: NavController,
    data: PostDetailData,
    showReportSuccessToast: Boolean,
    reportToastMessage: String,
    showDeleteToast: Boolean,
    onCloseReportToast: () -> Unit,
    onCancelDelete: () -> Unit,
    onKebabMenuClick: () -> Unit,
    onLikeClick: () -> Unit,
    onDeleteOptionClick: () -> Unit,
    onEditOptionClick: () -> Unit,
    onReportOptionClick: () -> Unit,
    onDismissMoreOption: () -> Unit,
    showMoreOption: Boolean,
    showDeleteConfirmDialog: Boolean,
    onConfirmDelete: () -> Unit,
    onDismissDeleteDialog: () -> Unit,
    showReportReasonDialog: Boolean,
    selectedReportReason: String,
    onReasonSelected: (String) -> Unit,
    onSubmitReport: () -> Unit,
    onDismissReportReasonDialog: () -> Unit,
    commentText: String,
    onCommentTextChange: (String) -> Unit,
    isCommentSecret: Boolean,
    onCommentSecretToggle: () -> Unit,
    replyToNickname: String?,
    onCancelReply: () -> Unit,
    onCommentSubmit: () -> Unit,
    onCommentReplyClick: (Long, String) -> Unit,
    onCommentMenuClick: (Long, Boolean) -> Unit,
    onLoadMoreComments: () -> Unit,
    onImageAttachClick: () -> Unit,
    onImageRemoveClick: () -> Unit,
    commentImageUri: android.net.Uri?,
    showCommentMoreOption: Boolean,
    isSelectedCommentMine: Boolean,
    onCommentEditOptionClick: () -> Unit,
    onCommentDeleteOptionClick: () -> Unit,
    onCommentReportOptionClick: () -> Unit,
    onDismissCommentMoreOption: () -> Unit,
    showCommentDeleteDialog: Boolean,
    onConfirmCommentDelete: () -> Unit,
    onDismissCommentDeleteDialog: () -> Unit,
    showCommentReportBottomSheet: Boolean,
    onCommentReportBottomSheetClick: () -> Unit,
    onDismissCommentReportBottomSheet: () -> Unit,
    showCommentReportReasonDialog: Boolean,
    selectedCommentReportReason: String,
    onCommentReasonSelected: (String) -> Unit,
    onSubmitCommentReport: () -> Unit,
    onDismissCommentReportReasonDialog: () -> Unit
) {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "",
                    textAlignment = TopBarAlignment.CENTER,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {
                        navController.safePopBackStack()
                    },
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Default.Share)) {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, data.title)
                                putExtra(Intent.EXTRA_TEXT, "https://petbulance.cloud/posts/${data.postId}")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "게시글 공유"))
                        },
                        Pair(IconResource.Vector(Icons.Default.MoreVert)) {
                            onKebabMenuClick()
                        }
                    )
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.COMMUNITY,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                PostDetailContent(
                    data = data,
                    onLikeClick = onLikeClick
                )

                ThickDivider()

                CommentInputArea(
                    commentText = commentText,
                    onCommentTextChange = onCommentTextChange,
                    isSecret = isCommentSecret,
                    onSecretToggle = onCommentSecretToggle,
                    onImageAttachClick = onImageAttachClick,
                    onMentionClick = { },
                    onSubmitClick = onCommentSubmit,
                    replyToNickname = replyToNickname,
                    onCancelReply = onCancelReply,
                    isEditMode = data.editingCommentId != null,
                    onCancelEdit = onCancelReply,
                    onImageRemoveClick = onImageRemoveClick,
                    commentImageUri = commentImageUri
                )

                ThickDivider()

                CommentListView(
                    comments = data.comments,
                    hasMoreComments = data.hasMoreComments,
                    onLoadMore = onLoadMoreComments,
                    onReplyClick = onCommentReplyClick,
                    onMenuClick = onCommentMenuClick,
                    onEmptyCommentButtonClick = { }
                )
            }

            if (showReportSuccessToast) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(vertical = spacingXL, horizontal = spacingMedium)
                        .background(
                            Color(0xFF222222).copy(alpha = 0.9f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(spacingSmall),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = reportToastMessage,
                        style = typography.bodySmall,
                        color = colorScheme.text.inverse
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Close),
                        contentDescription = "Close toast",
                        size = iconSizeSmall,
                        tint = colorScheme.icon.inverse,
                        modifier = Modifier.clickable { onCloseReportToast() }
                    )
                }
            }

            if (showDeleteToast) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(vertical = spacingXL, horizontal = spacingMedium)
                        .background(
                            Color(0xFF222222).copy(alpha = 0.9f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(spacingSmall),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "게시글이 삭제되었습니다",
                        style = typography.bodySmall,
                        color = colorScheme.text.inverse
                    )
                    Text(
                        text = "취소",
                        style = typography.bodySmall.emp(),
                        color = colorScheme.text.inverse,
                        modifier = Modifier.clickable { onCancelDelete() }
                    )
                }
            }
        }
    }

    if (showMoreOption) {
        if (data.isMine) {
            PostDeleteBottomSheet(
                onDeleteOptionClicked = onDeleteOptionClick,
                onEditOptionClicked = {
                    onDismissMoreOption()
                    onEditOptionClick()
                },
                onDismissRequest = onDismissMoreOption
            )
        } else {
            PostReportBottomSheet(
                onReportOptionClicked = onReportOptionClick,
                onDismissRequest = onDismissMoreOption
            )
        }
    }

    if (showDeleteConfirmDialog) {
        WarningDialog(
            title = "게시글을 삭제할까요?",
            content = "게시글을 삭제하면 모든 데이터가 삭제되고 다시 볼 수 없어요.",
            confirmText = "삭제",
            onDismissRequest = onDismissDeleteDialog,
            onExitButtonClicked = onConfirmDelete
        )
    }

    if (showReportReasonDialog) {
        PostReportReasonDialog(
            selectedReason = selectedReportReason,
            onReasonClicked = onReasonSelected,
            onSubmitClicked = onSubmitReport,
            onDismissRequest = onDismissReportReasonDialog
        )
    }

    if (showCommentMoreOption) {
        if (isSelectedCommentMine) {
            CommentDeleteBottomSheet(
                onEditOptionClicked = onCommentEditOptionClick,
                onDeleteOptionClicked = onCommentDeleteOptionClick,
                onDismissRequest = onDismissCommentMoreOption
            )
        } else {
            CommentReportBottomSheet(
                onReportOptionClicked = onCommentReportOptionClick,
                onDismissRequest = onDismissCommentMoreOption
            )
        }
    }

    if (showCommentDeleteDialog) {
        CommentDeleteConfirmDialog(
            onConfirmDelete = onConfirmCommentDelete,
            onDismissRequest = onDismissCommentDeleteDialog
        )
    }

    if (showCommentReportBottomSheet) {
        CommentReportBottomSheet(
            onReportOptionClicked = onCommentReportBottomSheetClick,
            onDismissRequest = onDismissCommentReportBottomSheet
        )
    }

    if (showCommentReportReasonDialog) {
        PostReportReasonDialog(
            selectedReason = selectedCommentReportReason,
            onReasonClicked = onCommentReasonSelected,
            onSubmitClicked = onSubmitCommentReport,
            onDismissRequest = onDismissCommentReportReasonDialog
        )
    }
}

@Composable
private fun PostDetailContent(
    data: PostDetailData,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacingLarge, horizontal = spacingXL),
        verticalArrangement = Arrangement.spacedBy(spacingLarge)
    ) {
        PostDetailHeader(
            boardName = data.boardName,
            category = data.category,
        )

        PostDetailTitle(title = data.title)

        PostAuthorInfo(
            writerNickname = data.writerNickname,
            profileUrl = data.profileUrl,
            createdAt = data.createdAt
        )

        PostDetailBody(content = data.content)

        PostDetailImages(images = data.images)

        PostDetailStats(
            likeCount = data.likeCount,
            viewCount = data.viewCount,
            commentCount = data.commentCount,
            onLikeClick = onLikeClick
        )
    }
}

@Composable
private fun PostDetailHeader(
    boardName: String,
    category: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicChip(text = boardName)

        BasicChip(text = category, backgroundColor = colorScheme.bg.frame.subtle)
    }
}

@Composable
private fun PostDetailTitle(title: String) {
    Text(
        text = title,
        style = typography.titleSmall.emp(),
        color = colorScheme.text.primary
    )
}

@Composable
private fun PostAuthorInfo(writerNickname: String, profileUrl: String?, createdAt: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicImageBox(
            modifier = Modifier.clip(CircleShape),
            uri = profileUrl?.toUri(),
            size = 40.dp
        )

        Column(verticalArrangement = Arrangement.spacedBy(spacingXXXS)) {
            Text(
                text = writerNickname,
                style = typography.bodySmall.emp(),
                color = colorScheme.text.primary
            )
            Text(
                text = createdAt,
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
        }

    }
}

@Composable
private fun PostDetailImages(images: List<String>) {
    if (images.isNotEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingMedium)
        ) {
            images.forEach { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Composable
private fun PostDetailBody(content: String) {
    Text(
        text = content,
        style = typography.bodyLarge,
        color = colorScheme.text.secondary
    )
}

@Composable
private fun PostDetailStats(
    likeCount: Int,
    viewCount: Int,
    commentCount: Int,
    onLikeClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicIcon(
            modifier = Modifier.clickable{
                onLikeClick()
            },
            iconResource = IconResource.Vector(Icons.Default.Favorite),
            contentDescription = "Fav count",
            size = iconSizeSmall,
            tint = colorScheme.icon.veryLight
        )

        Text(
            text = "$likeCount",
            style = typography.labelLarge,
            color = colorScheme.text.caption
        )

        Dot(dotColor = colorScheme.icon.veryLight)

        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.Visibility),
            contentDescription = "Fav count",
            size = iconSizeSmall,
            tint = colorScheme.icon.veryLight
        )

        Text(
            text = "$viewCount",
            style = typography.labelLarge,
            color = colorScheme.text.caption
        )

        Dot(dotColor = colorScheme.icon.veryLight)

        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.Comment),
            contentDescription = "Fav count",
            size = iconSizeSmall,
            tint = colorScheme.icon.veryLight
        )

        Text(
            text = "$commentCount",
            style = typography.labelLarge,
            color = colorScheme.text.caption
        )
    }
}


@Preview
@Composable
private fun PostDetailMainViewPreview() {
    PetbulanceTheme {
        PostDetailMainView(
            navController = rememberNavController(),
            data = PostDetailData.stub(isMine = false),
            showReportSuccessToast = false,
            reportToastMessage = "",
            showDeleteToast = false,
            onCloseReportToast = {},
            onCancelDelete = {},
            onKebabMenuClick = {},
            onLikeClick = {},
            onDeleteOptionClick = {},
            onEditOptionClick = {},
            onReportOptionClick = {},
            onDismissMoreOption = {},
            showMoreOption = false,
            showDeleteConfirmDialog = false,
            onConfirmDelete = {},
            onDismissDeleteDialog = {},
            showReportReasonDialog = false,
            selectedReportReason = "",
            onReasonSelected = {},
            onSubmitReport = {},
            onDismissReportReasonDialog = {},
            commentText = "",
            onCommentTextChange = {},
            isCommentSecret = false,
            onCommentSecretToggle = {},
            replyToNickname = null,
            onCancelReply = {},
            onCommentSubmit = {},
            onCommentReplyClick = { _, _ -> },
            onCommentMenuClick = { _, _ -> },
            onLoadMoreComments = {},
            onImageAttachClick = {},
            onImageRemoveClick = {},
            commentImageUri = null,
            showCommentMoreOption = false,
            isSelectedCommentMine = false,
            onCommentEditOptionClick = {},
            onCommentDeleteOptionClick = {},
            onCommentReportOptionClick = {},
            onDismissCommentMoreOption = {},
            showCommentDeleteDialog = false,
            onConfirmCommentDelete = {},
            onDismissCommentDeleteDialog = {},
            showCommentReportBottomSheet = false,
            onCommentReportBottomSheetClick = {},
            onDismissCommentReportBottomSheet = {},
            showCommentReportReasonDialog = false,
            selectedCommentReportReason = "",
            onCommentReasonSelected = {},
            onSubmitCommentReport = {},
            onDismissCommentReportReasonDialog = {}
        )
    }
}
