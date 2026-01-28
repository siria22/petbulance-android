package com.petbulance.presentation.screen.feature.review.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
import com.petbulance.domain.model.feature.hospital.review.ReviewRating
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.RatingStarsBar
import com.petbulance.presentation.component.ui.iconSizeLarge
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXS

@Composable
fun ReviewHospitalNameInput(
    query: String,
    selectedHospitalName: String?,
    candidates: List<HospitalInfoForReview>,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
    onCandidateClicked: (HospitalInfoForReview) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXXS),
        modifier = modifier
    ) {
        Text(
            text = "병원명",
            style = typography.titleSmall,
            color = colorScheme.text.secondary,
        )

        ReviewInputTextField(
            queryString = query,
            placeholder = "병원명을 입력해주세요.",
            onQueryStringChanged = onQueryChanged,
            trailingIcon = if (query.isNotBlank()) {
                {
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Close),
                        contentDescription = "Clear",
                        size = 20.dp,
                        tint = colorScheme.icon.medium,
                        modifier = Modifier.clickable { onClearQuery() }
                    )
                }
            } else null
        )

        if (candidates.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.bg.frame.default, RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = colorScheme.border.verySubtle,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 6.dp)
            ) {
                LazyColumn {
                    items(candidates) { item ->
                        val isSelected = selectedHospitalName == item.name

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 220.dp)
                                .background(
                                    if (isSelected) colorScheme.bg.frame.subtle else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onCandidateClicked(item) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.name,
                                style = typography.bodyLarge,
                                color = colorScheme.text.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewTotalCostInput(
    cost: String,
    onCostChanged: (String) -> Unit,
    title: String = "총 진료비",
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXXS),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = typography.titleSmall,
            color = colorScheme.text.secondary,
        )
        ReviewInputTextField(
            queryString = cost,
            placeholder = "진료비를 입력해주세요. (원)",
            onQueryStringChanged = onCostChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun ReviewAnimalTypeInput(
    selectedAnimalType: AnimalCategory,
    onAnimalTypeSelected: (AnimalCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXXS),
        modifier = modifier
    ) {
        Text(
            text = "동물종",
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(
                    color = colorScheme.bg.frame.default,
                    shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.dp,
                    color = colorScheme.border.verySubtle,
                    shape = RoundedCornerShape(6.dp)
                )
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val selectedCategory =
                    AnimalCategory.entries.find { it == selectedAnimalType }
                val text = selectedCategory?.korean ?: "동물종을 선택해주세요."
                val textColor =
                    if (selectedCategory != null) colorScheme.text.secondary
                    else colorScheme.text.disabled

                Text(
                    text = text,
                    style = typography.bodyLarge,
                    color = textColor
                )
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.KeyboardArrowDown),
                    contentDescription = "Expand",
                    size = iconSizeMS,
                    tint = colorScheme.icon.dark
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(colorScheme.bg.frame.default)
            ) {
                AnimalCategory.entries.filter { it != AnimalCategory.ALL }.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category.korean,
                                style = typography.bodyMedium,
                                color = colorScheme.text.secondary
                            )
                        },
                        onClick = {
                            onAnimalTypeSelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewDetailAnimalTypeInput(
    detailAnimalType: String,
    onDetailAnimalTypeChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXXS),
        modifier = modifier
    ) {
        Text(
            text = "세부 동물명",
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )
        ReviewInputTextField(
            queryString = detailAnimalType,
            placeholder = "예: 골든햄스터, 코뉴어, 코리도라스",
            onQueryStringChanged = onDetailAnimalTypeChanged,
        )
    }
}

@Composable
fun ReviewRatingsSection(
    ratings: ReviewRating,
    onRatingChanged: (ReviewRating) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        modifier = modifier.fillMaxWidth()
    ) {
        ReviewRatingItem(
            title = "전문성",
            desc = "증상과 치료에 대해 자세히 설명했나요?",
            currentRating = ratings.expertise,
            onRatingChanged = {
                onRatingChanged(ratings.copy(expertise = it))
            }
        )

        ReviewRatingItem(
            title = "친절도",
            desc = "접수/수납 과정에서 충분한 안내를 받았나요?",
            currentRating = ratings.kindness,
            onRatingChanged = {
                onRatingChanged(ratings.copy(kindness = it))
            }
        )

        ReviewRatingItem(
            title = "시설/환경",
            desc = "진료실과 병원 시설이 위생적이었나요?",
            currentRating = ratings.facility,
            onRatingChanged = {
                onRatingChanged(ratings.copy(facility = it))
            }
        )
    }
}

@Composable
fun ReviewImageSection(
    images: List<String>,
    onImageAddClicked: () -> Unit,
    onImageDeleteClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        if(images.size < 5) {
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
                    text = "${images.size}/5",
                    color = colorScheme.icon.light,
                    style = typography.labelMedium
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(images) { index, uri ->
                ReviewImageItem(
                    uri = uri,
                    index = index + 1,
                    onDelete = { onImageDeleteClicked(index) }
                )
            }
        }
    }
}

@Composable
fun ReviewContentInput(
    content: String,
    onContentChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXXS),
        modifier = modifier
    ) {
        Text(
            text = "후기 내용",
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )
        ReviewInputTextField(
            modifier = Modifier.height(200.dp),
            queryString = content,
            placeholder = "자세한 진료 및 치료 과정을 작성해주세요.",
            onQueryStringChanged = onContentChanged,
            singleLine = false,
        )
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

@Composable
fun ReviewRatingItem(
    title: String,
    desc: String,
    currentRating: Double,
    onRatingChanged: (Double) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = typography.titleSmall.emp(),
                color = colorScheme.text.secondary,
            )
            Text(
                text = desc,
                style = typography.bodySmall,
                color = colorScheme.text.caption
            )
        }

        RatingStarsBar(
            rating = currentRating,
            onRatingChanged = onRatingChanged,
            isEditable = true
        )
    }
}