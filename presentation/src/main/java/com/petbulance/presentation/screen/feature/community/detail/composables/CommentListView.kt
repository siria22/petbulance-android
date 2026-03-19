package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.community.post.Comment
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall

@Composable
fun CommentListView(
    comments: List<Comment>,
    hasMoreComments: Boolean,
    onLoadMore: () -> Unit,
    onReplyClick: (Long, String) -> Unit,
    onMenuClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem != null && lastVisibleItem.index >= totalItems - 3 && hasMoreComments
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    if (comments.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(spacingMedium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "아직 댓글이 없습니다.",
                style = typography.bodyMedium,
                color = colorScheme.text.disabled
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(vertical = spacingSmall),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            val groupedComments = comments.groupBy { it.parentId }
            val rootComments = groupedComments[0L] ?: emptyList()

            rootComments.forEach { rootComment ->
                item(key = "root_${rootComment.commentId}") {
                    CommentItem(
                        comment = rootComment,
                        onReplyClick = onReplyClick,
                        onMenuClick = onMenuClick,
                        isReply = false
                    )
                }

                val replies = groupedComments[rootComment.commentId] ?: emptyList()
                items(
                    items = replies,
                    key = { reply -> "reply_${reply.commentId}" }
                ) { reply ->
                    CommentItem(
                        comment = reply,
                        onReplyClick = onReplyClick,
                        onMenuClick = onMenuClick,
                        isReply = true
                    )
                }
            }

            if (hasMoreComments) {
                item {
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
