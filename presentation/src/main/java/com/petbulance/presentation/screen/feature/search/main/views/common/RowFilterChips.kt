package com.petbulance.presentation.screen.feature.search.main.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.molecule.FilterBottomSheetTab
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel

@Composable
fun RowChipFilters(
    uiModel: HospitalSearchQueryUiModel,
    onFilterButtonClicked: (FilterBottomSheetTab) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXXS),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilterBottomSheetTab.entries.forEach { elem ->
            val label = when (elem) {
                FilterBottomSheetTab.REGION -> {
                    val region = uiModel.region
                    val district = uiModel.district
                    if (region != null) {
                        if (district != null && district.contains("전체")) {
                            district
                        } else {
                            "${region.displayName} ${district ?: "전체"}"
                        }
                    } else {
                        elem.toKorean()
                    }
                }

                FilterBottomSheetTab.SPECIES -> {
                    uiModel.species?.korean ?: AnimalCategory.ALL.korean
                }
            }

            ChipFilter(
                text = label,
                onButtonClicked = { onFilterButtonClicked(elem) }
            )
        }
    }
}

@Composable
private fun ChipFilter(
    text: String,
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
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.icon.dark,
        )

        BasicIcon(
            iconResource = IconResource.Vector(Icons.Filled.KeyboardArrowDown),
            contentDescription = "Icon",
            size = iconSizeMS,
            modifier = Modifier.clickable {
                onButtonClicked()
            }
        )
    }
}