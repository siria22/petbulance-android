package com.example.presentation.screen.feature.search.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeMs
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXS

enum class HospitalSortType(val korean: String) {
    DISTANCE("가까운순"),
    RATING("별점순"),
    REVIEW("리뷰순")
} /* FIXME : Move to domain */

@Composable
fun RowResultControlChips(
    selectedSortType: HospitalSortType,
    isOpenNowOnly: Boolean,
    onSortTypeClicked: () -> Unit,
    onOpenNowOnlyClicked: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXS),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            horizontal = spacingMedium,
            vertical = spacingXS
        )
    ) {
        SortChip(
            sortType = selectedSortType,
            onButtonClicked = onSortTypeClicked
        )

        OpenNowChip(
            isOpenNowOnly = isOpenNowOnly,
            onButtonClicked = { onOpenNowOnlyClicked(!isOpenNowOnly) }
        )
    }
}

@Composable
private fun SortChip(
    sortType: HospitalSortType,
    onButtonClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXXS),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = colorScheme.bg.frame.default,
                shape = RoundedCornerShape(1000.dp)
            )
            .border(
                width = 1.dp,
                color = colorScheme.border.verySubtle,
                shape = RoundedCornerShape(1000.dp)
            )
            .padding(vertical = spacingXXS, horizontal = spacingSmall)
            .clickable { onButtonClicked() }
    ) {
        BasicIcon(
            iconResource = IconResource.Vector(Icons.AutoMirrored.Filled.Sort),
            contentDescription = "Sort Icon",
            size = iconSizeMs,
            tint = colorScheme.icon.dark
        ) // FIXME : apply design system's icon

        Text(
            text = sortType.korean,
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.icon.dark,
        )
    }
}

@Composable
private fun OpenNowChip(
    isOpenNowOnly: Boolean,
    onButtonClicked: () -> Unit
) {
    val borderColor =
        if (isOpenNowOnly) colorScheme.tag.blue.medium else colorScheme.border.verySubtle
    val textColor = if (isOpenNowOnly) colorScheme.tag.blue.medium else colorScheme.icon.dark

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXXS),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = colorScheme.bg.frame.default,
                shape = RoundedCornerShape(1000.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(1000.dp)
            )
            .padding(vertical = spacingXXS, horizontal = spacingSmall)
            .clickable { onButtonClicked() }
    ) {
        Text(
            text = "진료중",
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RowResultControlChipsPreview() {
    PetbulanceTheme {
        RowResultControlChips(
            selectedSortType = HospitalSortType.DISTANCE,
            isOpenNowOnly = false,
            onSortTypeClicked = {},
            onOpenNowOnlyClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RowResultControlChipsOpenNowSelectedPreview() {
    PetbulanceTheme {
        RowResultControlChips(
            selectedSortType = HospitalSortType.DISTANCE,
            isOpenNowOnly = true,
            onSortTypeClicked = {},
            onOpenNowOnlyClicked = {}
        )
    }
}