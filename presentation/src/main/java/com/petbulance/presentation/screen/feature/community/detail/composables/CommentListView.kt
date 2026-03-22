package com.petbulance.presentation.screen.feature.community.detail.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.community.post.Comment
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.Space16
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL

@Composable
fun CommentListView(
    comments: List<Comment>,
    hasMoreComments: Boolean,
    onLoadMore: () -> Unit,
    onReplyClick: (Long, String) -> Unit,
    onMenuClick: (Long, Boolean) -> Unit,
    onEmptyCommentButtonClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacingXL, end = spacingXL, top = spacingMedium)
        ) {
            Text(
                text = "댓글",
                style = typography.labelLarge,
                color = colorScheme.text.tertiary
            )

            Text(
                text = "${comments.count { !it.deleted }}개",
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
        }

        if (comments.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(spacingMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacingSmall)
            ) {
                Text(
                    text = "첫 댓글을 남겨보세요.",
                    style = typography.labelLarge,
                    color = colorScheme.text.caption
                )

                Space16()
            }

        } else {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                val groupedComments = comments.groupBy { it.parentId }
                val rootComments = comments.filter { it.isRoot }

                Log.d(
                    "CommentListView",
                    "Processing comments: total=${comments.size}, root=${rootComments.size}"
                )
                rootComments.forEach { rootComment ->
                    Log.d(
                        "CommentListView",
                        "Rendering root comment: id=${rootComment.commentId}, visible=${rootComment.visibleToUser}, deleted=${rootComment.deleted}"
                    )
                    CommentItem(
                        comment = rootComment,
                        onReplyClick = onReplyClick,
                        onMenuClick = onMenuClick,
                        isReply = false
                    )
                    CommonDivider()

                    val replies =
                        groupedComments[rootComment.commentId]?.filter { it.commentId != rootComment.commentId }
                            ?: emptyList()
                    replies.forEach { reply ->
                        Log.d(
                            "CommentListView",
                            "Rendering reply: id=${reply.commentId}, visible=${reply.visibleToUser}, deleted=${reply.deleted}"
                        )
                        CommentItem(
                            comment = reply,
                            onReplyClick = onReplyClick,
                            onMenuClick = onMenuClick,
                            isReply = true
                        )
                        CommonDivider()
                    }
                }

                if (hasMoreComments) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(spacingMedium),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorScheme.action.primary.default
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentListViewPreview() {
    PetbulanceTheme {
        CommentListView(
            comments = Comment.stubs,
            hasMoreComments = true,
            onLoadMore = {},
            onReplyClick = { _, _ -> },
            onMenuClick = { _, _ -> },
            onEmptyCommentButtonClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommentListViewEmptyPreview() {
    PetbulanceTheme {
        CommentListView(
            comments = emptyList(),
            hasMoreComments = false,
            onLoadMore = {},
            onReplyClick = { _, _ -> },
            onMenuClick = { _, _ -> },
            onEmptyCommentButtonClick = {}
        )
    }
}
