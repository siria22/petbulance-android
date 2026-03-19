package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
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
    modifier: Modifier = Modifier,
    maxLength: Int = 200
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.bg.frame.default)
    ) {
        if (replyToNickname != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.bg.frame.subtle)
                    .padding(horizontal = spacingMedium, vertical = spacingXS),
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingMedium, vertical = spacingSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacingXS)
        ) {
            BasicIcon(
                iconResource = IconResource.Vector(
                    if (isSecret) Icons.Outlined.Lock else Icons.Outlined.LockOpen
                ),
                contentDescription = if (isSecret) "비밀댓글" else "공개댓글",
                size = iconSizeMS,
                tint = if (isSecret) colorScheme.action.primary.default else colorScheme.icon.light,
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

            val scrollState = rememberScrollState()
            var textFieldHeight by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current
            val maxLines = 4
            val singleLineHeight = with(density) { typography.bodyMedium.lineHeight.toDp() }
            val maxHeight = singleLineHeight * maxLines + spacingSmall * 2

            BasicTextField(
                value = commentText,
                onValueChange = { newText ->
                    if (newText.length <= maxLength) {
                        onCommentTextChange(newText)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        colorScheme.bg.frame.subtle,
                        RoundedCornerShape(16.dp)
                    )
                    .onSizeChanged { size ->
                        textFieldHeight = with(density) { size.height.toDp() }
                    },
                textStyle = typography.bodyMedium.copy(color = colorScheme.text.primary),
                cursorBrush = SolidColor(colorScheme.action.primary.default),
                maxLines = maxLines,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = spacingMedium, vertical = spacingSmall)
                            .let {
                                if (textFieldHeight > maxHeight) {
                                    it.verticalScroll(scrollState)
                                } else {
                                    it
                                }
                            },
                        contentAlignment = Alignment.CenterStart
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

            Text(
                text = "등록",
                style = typography.bodyMedium,
                color = if (commentText.isNotBlank()) colorScheme.action.primary.default else colorScheme.text.disabled,
                modifier = Modifier.clickable(enabled = commentText.isNotBlank()) {
                    onSubmitClick()
                }
            )
        }
    }
}
