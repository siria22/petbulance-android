package com.example.presentation.screen.feature.review.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.type.ReviewSortType
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeMedium
import com.example.presentation.component.ui.spacingLarge
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXXL

@Composable
fun ReviewSortTypeDialog(
    selectedSortType: ReviewSortType,
    onDismissRequest: () -> Unit,
    onSortTypeSelected: (ReviewSortType) -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest,
        position = Alignment.BottomCenter,
        paddingValues = PaddingValues(horizontal = spacingXL, vertical = spacingXXL + 36.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXL),
        ) {
            Text(
                text = "어떤 순서로 정렬할까요?",
                style = MaterialTheme.typography.titleMedium.emp(),
                color = colorScheme.text.primary,
                textAlign = TextAlign.Center
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ReviewSortType.entries.forEach {
                    SortTypeDialogItem(
                        sortType = it,
                        isSelected = it == selectedSortType,
                        onItemClicked = onSortTypeSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun SortTypeDialogItem(
    sortType: ReviewSortType,
    isSelected: Boolean,
    onItemClicked: (ReviewSortType) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked(sortType)
            }
    ) {
        Text(
            text = sortType.korean,
            style = MaterialTheme.typography.bodyLarge.emp(),
            color = colorScheme.text.tertiary,
        )

        if (isSelected) {
            BasicIcon(
                iconResource = IconResource.Drawable(R.drawable.ic_bottomsheet_checked),
                contentDescription = "Check button",
                size = iconSizeMedium,
                tint = colorScheme.action.primary.default,
            )
        }
    }
}


@Preview(apiLevel = 34)
@Composable
private fun SelectSortTypeDialogPreview() {
    PetbulanceTheme() {
        ReviewSortTypeDialog(
            selectedSortType = ReviewSortType.LATEST,
            onDismissRequest = {},
            onSortTypeSelected = {}
        )
    }
}