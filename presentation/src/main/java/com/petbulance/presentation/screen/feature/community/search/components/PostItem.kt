package com.petbulance.presentation.screen.feature.community.search.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.component.ui.spacingXXXS
import com.petbulance.presentation.utils.buildHighlightedText

@Composable
fun PostListItem(
    post: PostSummary,
    onPostClick: () -> Unit,
    onLikeClick: () -> Unit,
    searchKeyword: String = "",
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXS),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingXL, vertical = spacingMedium)
            .clickable { onPostClick() },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacingXS)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacingXXS)
                ) {
                    PostItemChip(
                        text = post.type,
                        type = "category"
                    )
                    PostItemChip(
                        text = post.topic,
                        type = "topic"
                    )
                }

                Text(
                    text = buildHighlightedText(post.title, searchKeyword),
                    style = typography.bodySmall.emp(),
                    color = colorScheme.text.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = buildHighlightedText(post.content, searchKeyword),
                    style = typography.labelLarge,
                    color = colorScheme.text.secondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (post.thumbnailUrl != null) {
                Box(contentAlignment = Alignment.TopStart) {
                    BasicImageBox(
                        size = 90.dp,
                        uri = post.thumbnailUrl?.toUri(),
                        errorImageResource = R.drawable.img_checker,
                        placeholderImageResource = R.drawable.img_checker,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    )
                    if (post.imageCount > 1) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = post.imageCount.toString(),
                                color = Color.White,
                                style = typography.labelMedium
                            )
                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXXXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "익명", // TODO: API에 nickname 필드 추가 필요
                    style = typography.labelSmall,
                    color = colorScheme.text.caption
                )

                Dot(dotColor = colorScheme.icon.veryLight)

                Text(
                    text = post.createdAt,
                    style = typography.labelSmall,
                    color = colorScheme.text.caption
                )

                Dot(dotColor = colorScheme.icon.veryLight)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = colorScheme.icon.veryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = post.viewCount.toString(),
                        style = typography.labelSmall,
                        color = colorScheme.text.caption
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable(onClick = onLikeClick)
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = colorScheme.icon.veryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = post.likeCount.toString(),
                        style = typography.labelSmall,
                        color = colorScheme.text.caption
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = colorScheme.icon.veryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = post.commentCount.toString(),
                        style = typography.labelSmall,
                        color = colorScheme.text.caption
                    )
                }
            }
        }
    }
}

@Composable
private fun PostItemChip(
    text: String,
    type: String = "category"
) {
    val backgroundColor =
        if (type == "category") colorScheme.bg.frame.default else colorScheme.bg.frame.subtle
    val borderColor = colorScheme.border.tertiary
    val cornerShape = RoundedCornerShape(4.dp)

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = cornerShape
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = cornerShape
            )
            .padding(
                vertical = spacingXXXS,
                horizontal = spacingXS
            )
    ) {
        Text(
            text = text,
            style = typography.labelSmall,
            color = colorScheme.text.tertiary
        )
    }
}
