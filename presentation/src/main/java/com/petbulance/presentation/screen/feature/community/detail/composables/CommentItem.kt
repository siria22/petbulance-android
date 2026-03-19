package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.petbulance.domain.model.feature.community.post.Comment
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.iconSizeSmall
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.component.ui.spacingXXXS

@Composable
fun CommentItem(
    comment: Comment,
    onReplyClick: (Long, String) -> Unit,
    onMenuClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isReply: Boolean = false
) {
    val backgroundColor = when {
        comment.deleted -> colorScheme.bg.frame.subtle
        comment.isCommentAuthor -> colorScheme.bg.frame.subtle
        else -> colorScheme.bg.frame.default
    }

    val startPadding = if (isReply) spacingXL else spacingMedium

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(start = startPadding, end = spacingMedium, top = spacingSmall, bottom = spacingSmall),
        horizontalArrangement = Arrangement.spacedBy(spacingXS)
    ) {
        BasicImageBox(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            uri = if (comment.visibleToUser && !comment.deleted) {
                comment.writerInfo.profileUrl?.toUri()
            } else null,
            size = 32.dp
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacingXXXS)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingXXS),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (comment.visibleToUser && !comment.deleted) {
                            comment.writerInfo.nickname
                        } else {
                            ""
                        },
                        style = typography.bodySmall.emp(),
                        color = colorScheme.text.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (comment.isCommentFromPostAuthor && comment.visibleToUser && !comment.deleted) {
                        Text(
                            text = "작성자",
                            style = typography.labelSmall,
                            color = colorScheme.action.primary.default
                        )
                    }
                }

                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.MoreVert),
                    contentDescription = "더보기",
                    size = iconSizeSmall,
                    tint = colorScheme.icon.light,
                    modifier = Modifier.clickable {
                        onMenuClick(comment.commentId, comment.isCommentAuthor)
                    }
                )
            }

            if (comment.deleted) {
                Text(
                    text = "삭제된 댓글입니다",
                    style = typography.bodyMedium,
                    color = colorScheme.text.disabled
                )
            } else if (!comment.visibleToUser) {
                Text(
                    text = "비밀 댓글입니다. ${comment.createdAt}",
                    style = typography.bodyMedium,
                    color = colorScheme.text.disabled
                )
            } else {
                if (comment.mentionUserNickname.isNotEmpty()) {
                    Text(
                        text = "@${comment.mentionUserNickname}",
                        style = typography.bodyMedium,
                        color = colorScheme.action.primary.default
                    )
                }

                Text(
                    text = comment.content,
                    style = typography.bodyMedium,
                    color = colorScheme.text.secondary
                )

                if (comment.imageUrl.isNotEmpty()) {
                    BasicImageBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = spacingXXS),
                        uri = comment.imageUrl.toUri()
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.createdAt,
                    style = typography.labelLarge,
                    color = colorScheme.text.caption
                )

                if (!comment.deleted && comment.visibleToUser) {
                    Text(
                        text = "답글",
                        style = typography.labelLarge,
                        color = colorScheme.text.caption,
                        modifier = Modifier.clickable {
                            onReplyClick(comment.commentId, comment.writerInfo.nickname)
                        }
                    )
                }
            }
        }
    }
}
