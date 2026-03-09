package com.petbulance.presentation.screen.feature.review.main.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.molecule.FilterBottomSheetTab
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.screen.feature.review.main.ReviewData

@Composable
fun ReviewFilterChips(
    data: ReviewData,
    onFilterClick: (FilterBottomSheetTab) -> Unit,
    onSortClick: () -> Unit,
    onReceiptToggle: () -> Unit,
    onPhotoToggle: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(spacingXS),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.padding(vertical = spacingSmall)
    ) {
        item {
            val label = if (data.selectedRegion != null) {
                if (data.selectedDistrict != null && data.selectedDistrict.contains("전체")) {
                    data.selectedDistrict
                } else {
                    "${data.selectedRegion.displayName} ${data.selectedDistrict ?: "전체"}"
                }
            } else {
                "지역"
            }
            DropdownLikeChip(text = label, onClick = { onFilterClick(FilterBottomSheetTab.REGION) })
        }

        item {
            val label = data.selectedAnimalType?.let { AnimalCategory.toKorean(it) } ?: "동물종"
            DropdownLikeChip(
                text = label,
                onClick = { onFilterClick(FilterBottomSheetTab.SPECIES) })
        }

        item {
            IconTextChip(
                text = data.selectedSort.korean,
                icon = Icons.AutoMirrored.Outlined.Sort,
                isSelected = false,
                onClick = onSortClick
            )
        }

        item {
            IconTextChip(
                text = "영수증인증",
                icon = Icons.Filled.Check,
                isSelected = data.isReceiptVerified,
                onClick = onReceiptToggle
            )
        }

        item {
            IconTextChip(
                text = "사진 후기",
                icon = Icons.Filled.PhotoCamera,
                isSelected = data.isPhotoReview,
                onClick = onPhotoToggle
            )
        }
    }
}

@Composable
private fun DropdownLikeChip(
    text: String,
    onClick: () -> Unit
) {
    BaseChip(onClick = onClick) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.text.secondary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Filled.KeyboardArrowDown),
            contentDescription = "Dropdown",
            size = iconSizeMS,
            tint = colorScheme.icon.basic
        )
    }
}

@Composable
private fun IconTextChip(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = colorScheme.icon.dark
    val borderColor =
        if (isSelected) colorScheme.tag.trust.medium else colorScheme.tag.trust.verysubtle

    BaseChip(
        onClick = onClick,
        borderColor = borderColor
    ) {
        BasicIcon(
            iconResource = IconResource.Vector(icon),
            contentDescription = text,
            size = 16.dp,
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor,
        )
    }
}

@Composable
private fun BaseChip(
    onClick: () -> Unit,
    borderColor: Color = colorScheme.border.verySubtle,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = colorScheme.bg.frame.default,
                shape = RoundedCornerShape(100.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(100.dp)
            )
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable { onClick() }
    ) {
        content()
    }
}