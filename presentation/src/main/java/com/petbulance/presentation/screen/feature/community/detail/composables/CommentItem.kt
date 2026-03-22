package com.petbulance.presentation.screen.feature.community.detail.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.petbulance.domain.model.feature.community.post.Comment
import com.petbulance.domain.model.feature.community.post.WriterInfo
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
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
    Log.d(
        "CommentItem",
        "CommentItem rendering: id=${comment.commentId}, visible=${comment.visibleToUser}, deleted=${comment.deleted}, isReply=$isReply, nickname=${comment.writerInfo.nickname}"
    )

    val backgroundColor = when {
        comment.deleted -> colorScheme.bg.frame.default
        comment.isCommentAuthor -> colorScheme.bg.frame.subtle
        else -> colorScheme.bg.frame.default
    }

    val startPadding = if (isReply) spacingXL else spacingMedium

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(
                start = startPadding,
                end = spacingMedium,
                top = spacingSmall,
                bottom = spacingSmall
            ),
        horizontalArrangement = Arrangement.spacedBy(spacingSmall)
    ) {
        if (comment.deleted) {
            Text(
                text = "삭제된 댓글입니다",
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
        } else if (!comment.visibleToUser) {
            Text(
                text = "비밀 댓글입니다.",
                style = typography.labelLarge,
                color = colorScheme.text.primary
            )
            Text(
                text = "${comment.createdAt}",
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
        } else {
            BasicImageBox(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                uri = comment.writerInfo.profileUrl?.toUri(),
                size = 32.dp
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacingSmall)
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
                            text = comment.writerInfo.nickname ?: "",
                            style = typography.bodySmall.emp(),
                            color = colorScheme.text.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (comment.isCommentFromPostAuthor) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        colorScheme.bg.frame.subtle,
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(
                                        horizontal = spacingXXS,
                                        vertical = spacingXXXS
                                    )
                            ) {
                                Text(
                                    text = "작성자",
                                    style = typography.labelMedium,
                                    color = colorScheme.text.caption
                                )
                            }
                        }

                        Text(
                            text = comment.createdAt ?: "",
                            style = typography.labelLarge,
                            color = colorScheme.text.caption
                        )
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
                Text(
                    text = buildAnnotatedString {
                        if (!comment.mentionUserNickname.isNullOrEmpty()) {
                            withStyle(
                                style = SpanStyle(
                                    color = colorScheme.action.primary.default,
                                )
                            ) {
                                append("@${comment.mentionUserNickname}")
                            }
                            append(" ")
                        }
                        append(comment.content ?: "")
                    },
                    style = typography.bodyMedium,
                    color = colorScheme.text.secondary
                )

                comment.imageUrl?.let { imageUrl ->
                    BasicImageBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = spacingXXS),
                        uri = imageUrl.toUri()
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            colorScheme.bg.frame.default,
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = colorScheme.border.subtle
                        )
                        .padding(horizontal = spacingXS, vertical = spacingXXS)
                ) {
                    Text(
                        text = "답글",
                        style = typography.labelMedium,
                        color = colorScheme.text.tertiary,
                        modifier = Modifier.clickable {
                            onReplyClick(comment.commentId, comment.writerInfo.nickname ?: "")
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentItemPreview() {
    PetbulanceTheme {
        Surface {
            CommentItem(
                comment = Comment(
                    isRoot = true,
                    commentId = 1,
                    parentId = 0,
                    writerInfo = WriterInfo(
                        nickname = "닉네임",
                        profileUrl = null
                    ),
                    mentionUserNickname = null,
                    content = "댓글 내용입니다.",
                    isSecret = false,
                    isCommentFromPostAuthor = true,
                    isCommentAuthor = false,
                    deleted = false,
                    hidden = false,
                    imageUrl = null,
                    visibleToUser = true,
                    createdAt = "1시간 전"
                ),
                onReplyClick = { _, _ -> },
                onMenuClick = { _, _ -> }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentItemReplyPreview() {
    PetbulanceTheme {
        Surface {
            CommentItem(
                comment = Comment(
                    isRoot = false,
                    commentId = 2,
                    parentId = 1,
                    writerInfo = WriterInfo(
                        nickname = "답글러",
                        profileUrl = null
                    ),
                    mentionUserNickname = "닉네임",
                    content = "답글 내용입니다.",
                    isSecret = false,
                    isCommentFromPostAuthor = false,
                    isCommentAuthor = true,
                    deleted = false,
                    hidden = false,
                    imageUrl = null,
                    visibleToUser = true,
                    createdAt = "30분 전"
                ),
                isReply = true,
                onReplyClick = { _, _ -> },
                onMenuClick = { _, _ -> }
            )
        }
    }
}
