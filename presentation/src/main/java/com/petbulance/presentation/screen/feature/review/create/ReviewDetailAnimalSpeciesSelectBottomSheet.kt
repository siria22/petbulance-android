package com.petbulance.presentation.screen.feature.review.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicBottomSheet
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.component.ui.spacingXXS

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReviewDetailAnimalSpeciesSelectBottomSheet(
    category: AnimalCategory,
    selectedDetail: String,
    onDismissRequest: () -> Unit,
    onDetailSelected: (AnimalSpecies) -> Unit
) {
    val speciesList = remember(category) {
        AnimalSpecies.entries.filter { it.category == category }
    }
    var tempSelected by remember(selectedDetail) { mutableStateOf(selectedDetail) }

    BasicBottomSheet(
        showBottomSheet = true,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(spacingXXL)
        ) {
            BottomSheetTab()

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                speciesList.forEach { species ->
                    val isSelected = tempSelected == species.name
                    SpeciesChip(
                        name = species.korean,
                        isSelected = isSelected,
                        onClick = { tempSelected = species.name }
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacingMedium))

            BasicButton(
                modifier = Modifier.fillMaxWidth(),
                text = "선택",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.PRIMARY,
                radius = 16.dp,
                onClicked = {
                    onDetailSelected(AnimalSpecies.fromString(tempSelected))
                    onDismissRequest()
                },
            )
        }
    }
}

@Composable
private fun BottomSheetTab() {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "세부 동물명",
            style = MaterialTheme.typography.bodySmall.emp(),
            color = colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = spacingXXS)
                .fillMaxWidth(0.3f)
        )
        HorizontalDivider(
            thickness = 2.dp,
            color = colorScheme.border.active,
            modifier = Modifier.fillMaxWidth(0.3f)
        )

        CommonDivider()
    }
}

@Composable
private fun SpeciesChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) colorScheme.text.primary
    else colorScheme.text.caption

    Box(
        modifier = Modifier
            .background(
                color = colorScheme.bg.frame.default,
                shape = RoundedCornerShape(1000.dp)
            )
            .border(
                width = 1.dp,
                color = contentColor,
                shape = RoundedCornerShape(1000.dp)
            )
            .clip(RoundedCornerShape(1000.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewDetailAnimalSpeciesSelectBottomSheetPreview() {
    ReviewDetailAnimalSpeciesSelectBottomSheet(
        category = AnimalCategory.AVIAN,
        selectedDetail = AnimalSpecies.PARROT.name,
        onDismissRequest = {},
        onDetailSelected = {}
    )
}