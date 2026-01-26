package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateIntent
import com.petbulance.presentation.screen.feature.review.create.ReviewInputTextField
import com.petbulance.presentation.screen.feature.review.create.Step3State

@Composable
fun Step3ReviewContent(
    state: Step3State,
    intent: (ReviewCreateIntent) -> Unit,
    onImageAddClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        modifier = Modifier.padding(vertical = spacingXL, horizontal = spacingMedium)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .size(72.dp)
                    .background(colorScheme.bg.frame.default, RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp),
                        color = colorScheme.border.verySubtle
                    )
                    .clickable {
                        onImageAddClicked()
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.CameraAlt),
                    contentDescription = "Add image",
                    size = iconSizeLarge,
                    tint = colorScheme.icon.light
                )
                Text(
                    text = "${state.images.size}/10",
                    color = colorScheme.icon.light,
                    style = typography.labelMedium
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(state.images) { index, uri ->
                    ReviewImageItem(
                        uri = uri,
                        index = index + 1,
                        onDelete = {
                            val newList = state.images.toMutableList().apply { removeAt(index) }
                            intent(ReviewCreateIntent.OnImagesChanged(newList))
                        }
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS)
        ) {
            Text(
                text = "후기 내용",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
            ReviewInputTextField(
                modifier = Modifier.height(200.dp),
                queryString = state.content,
                placeholder = "자세한 진료 및 치료 과정을 작성해주세요.",
                onQueryStringChanged = { intent(ReviewCreateIntent.OnContentChanged(it)) },
                singleLine = false,
            )
        }
    }
}

@Composable
private fun ReviewImageItem(
    uri: String,
    index: Int,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier.size(72.dp)
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, colorScheme.border.verySubtle, RoundedCornerShape(8.dp))
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(topStart = 8.dp, bottomEnd = 6.dp)
                )
                .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
            Text(
                text = "$index",
                style = typography.labelSmall,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp)
                .size(18.dp)
                .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete Image",
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewCreateScreenStep3Preview() {
    PetbulanceTheme {
        Step3ReviewContent(
            state = Step3State(
                content = "선생님이 정말 친절하시고 설명도 잘 해주셨어요. 수술 경과도 좋아서 만족합니다."
            ),
            intent = {},
            onImageAddClicked = {}
        )
    }
}