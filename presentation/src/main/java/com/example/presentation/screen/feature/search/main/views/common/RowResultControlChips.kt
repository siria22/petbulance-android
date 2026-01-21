package com.example.presentation.screen.feature.search.main.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.type.HospitalSortType
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeMS
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXXS

@Composable
fun RowResultControlChips(
    selectedSortType: HospitalSortType,
    isOpenNowOnly: Boolean,
    onSortTypeClicked: () -> Unit,
    onOpenNowOnlyClicked: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXXS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SortChip(
            sortType = selectedSortType,
            onButtonClicked = onSortTypeClicked
        )

        OpenNowChip(
            isOpenNowOnly = isOpenNowOnly,
            onButtonClicked = { onOpenNowOnlyClicked(!isOpenNowOnly) },
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
            iconResource = IconResource.Drawable(R.drawable.ic_sort_desc),
            contentDescription = "Sort Icon",
            size = iconSizeMS,
            tint = colorScheme.icon.dark
        )
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
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(iconSizeMS)
                .wrapContentHeight(Alignment.CenterVertically)
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