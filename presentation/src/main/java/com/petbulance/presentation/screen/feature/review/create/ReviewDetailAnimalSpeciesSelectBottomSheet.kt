package com.petbulance.presentation.screen.feature.review.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.presentation.component.ui.atom.BasicBottomSheet
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailAnimalSpeciesSelectBottomSheet(
    category: AnimalCategory,
    selectedDetail: String,
    onDismissRequest: () -> Unit,
    onDetailSelected: (String) -> Unit
) {
    // 해당 카테고리의 Species 필터링
    val speciesList = remember(category) {
        AnimalSpecies.entries.filter { it.category == category }
    }
    var tempSelected by remember(selectedDetail) { mutableStateOf(selectedDetail) }

    BasicBottomSheet(
        showBottomSheet = true,
        onDismissRequest = onDismissRequest
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
            // Header (Title + Close)
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "세부 동물명",
                    style = typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
                /* 닫기 아이콘 등... */
            }

            // Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                speciesList.forEach { species ->
                    val isSelected = tempSelected == species.korean
                    /* Chip UI 구현 (Selected/Unselected 스타일 적용) */
                    // 클릭 시 tempSelected = species.korean
                }
            }

            BasicButton(
                text = "선택",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.PRIMARY,
                radius = 16.dp,
                onClicked = {
                    onDetailSelected(tempSelected)
                    onDismissRequest()
                },
            )
        }
    }
}