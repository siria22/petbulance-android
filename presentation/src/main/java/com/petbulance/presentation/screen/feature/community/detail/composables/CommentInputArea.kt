package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun CommentInputArea(
    commentText: String,
    onCommentTextChange: (String) -> Unit,
    isSecret: Boolean,
    onSecretToggle: () -> Unit,
    onImageAttachClick: () -> Unit,
    onMentionClick: () -> Unit,
    onSubmitClick: () -> Unit,
    replyToNickname: String? = null,
    onCancelReply: () -> Unit = {},
    isEditMode: Boolean = false,
    onCancelEdit: () -> Unit = {},
    onImageRemoveClick: () -> Unit = {},
    commentImageUri: android.net.Uri? = null,
    modifier: Modifier = Modifier,
    maxLength: Int = 200
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.bg.frame.default)
    ) {
        if (isEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingXL, vertical = spacingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "댓글 수정 중",
                    style = typography.bodySmall,
                    color = colorScheme.text.caption
                )
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.Close),
                    contentDescription = "수정 취소",
                    size = iconSizeMS,
                    tint = colorScheme.icon.light,
                    modifier = Modifier.clickable { onCancelEdit() }
                )
            }
        } else if (replyToNickname != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.bg.frame.subtle)
                    .padding(horizontal = spacingXL, vertical = spacingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${replyToNickname}에게 답글을 남기는 중",
                    style = typography.bodySmall,
                    color = colorScheme.text.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.Close),
                    contentDescription = "답글 취소",
                    size = iconSizeMS,
                    tint = colorScheme.icon.light,
                    modifier = Modifier.clickable { onCancelReply() }
                )
            }
        }

        if (commentImageUri != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingXL, vertical = spacingSmall),
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    AsyncImage(
                        model = commentImageUri,
                        contentDescription = "첨부된 이미지",
                        modifier = Modifier
                            .size(80.dp)
                            .background(colorScheme.bg.frame.subtle, RoundedCornerShape(8.dp))
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Close),
                        contentDescription = "이미지 삭제",
                        size = iconSizeMS,
                        tint = colorScheme.icon.inverse,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .clickable { onImageRemoveClick() }
                    )
                }
            }
        }

        val density = LocalDensity.current
        val maxLines = 4
        val minLines = 3
        val singleLineHeight = with(density) { typography.bodyMedium.lineHeight.toDp() }
        val minHeight = singleLineHeight * minLines

        BasicTextField(
            value = commentText,
            onValueChange = { newText ->
                if (newText.length <= maxLength) {
                    onCommentTextChange(newText)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingXL, vertical = spacingSmall)
                .defaultMinSize(minHeight = minHeight),
            textStyle = typography.bodyMedium.copy(color = colorScheme.text.primary),
            cursorBrush = SolidColor(colorScheme.action.primary.default),
            maxLines = maxLines,
            minLines = minLines,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = minHeight),
                    contentAlignment = Alignment.TopStart
                ) {
                    if (commentText.isEmpty()) {
                        Text(
                            text = "댓글 남기기",
                            style = typography.bodyMedium,
                            color = colorScheme.text.disabled
                        )
                    }
                    innerTextField()
                }
            }
        )

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingXL, vertical = spacingSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(
                        if (isSecret) Icons.Outlined.Lock else Icons.Outlined.LockOpen
                    ),
                    contentDescription = "비밀댓글",
                    size = iconSizeMS,
                    tint = colorScheme.icon.light,
                    modifier = Modifier.clickable { onSecretToggle() }
                )

                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Outlined.Image),
                    contentDescription = "이미지 첨부",
                    size = iconSizeMS,
                    tint = colorScheme.icon.light,
                    modifier = Modifier.clickable { onImageAttachClick() }
                )

                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Outlined.AlternateEmail),
                    contentDescription = "멘션",
                    size = iconSizeMS,
                    tint = colorScheme.icon.light,
                    modifier = Modifier.clickable { onMentionClick() }
                )
            }

            Text(
                text = if (isEditMode) "수정" else "등록",
                style = typography.bodyMedium,
                color = if (commentText.isNotEmpty()) {
                    colorScheme.action.primary.default
                } else {
                    colorScheme.text.disabled
                },
                modifier = Modifier.clickable(
                    enabled = commentText.isNotEmpty()
                ) {
                    onSubmitClick()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentInputAreaPreview() {
    CommentInputArea(
        commentText = "",
        onCommentTextChange = {},
        isSecret = false,
        onSecretToggle = {},
        onImageAttachClick = {},
        onMentionClick = {},
        onSubmitClick = {},
        commentImageUri = null,
        onImageRemoveClick = {},
        onCancelReply = {},
        isEditMode = false
    )
}
