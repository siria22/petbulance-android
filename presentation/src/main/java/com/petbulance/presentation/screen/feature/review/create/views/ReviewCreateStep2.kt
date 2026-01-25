package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateIntent
import com.petbulance.presentation.screen.feature.review.create.ReviewInputTextField
import com.petbulance.presentation.screen.feature.review.create.Step2State

@Composable
fun Step2AnimalContent(
    state: Step2State,
    intent: (ReviewCreateIntent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        modifier = Modifier.padding(vertical = spacingXL, horizontal = spacingMedium)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
        ) {
            Text(
                text = "동물종",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
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
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val selectedCategory =
                        AnimalCategory.entries.find { it.name == state.animalType }
                    val text = selectedCategory?.korean ?: "동물종을 선택해주세요."
                    val textColor =
                        if (selectedCategory != null) colorScheme.text.secondary else colorScheme.text.disabled

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
                                intent(ReviewCreateIntent.OnAnimalTypeChanged(category.name))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // 세부 동물명 입력
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
        ) {
            Text(
                text = "세부 동물명",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
            ReviewInputTextField(
                queryString = state.detailAnimalType,
                placeholder = "예: 골든햄스터, 코뉴어, 코리도라스",
                onQueryStringChanged = { intent(ReviewCreateIntent.OnDetailAnimalTypeChanged(it)) },
            )
        }

        // 진료명 입력
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
        ) {
            Text(
                text = "진료명",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
            ReviewInputTextField(
                queryString = state.treatment,
                placeholder = "예: 골절, 발톱정리, 종양수술",
                onQueryStringChanged = { intent(ReviewCreateIntent.OnTreatmentChanged(it)) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewCreateScreenStep2Preview() {
    PetbulanceTheme {
        Step2AnimalContent(
            state = Step2State(
                animalType = "강아지",
                detailAnimalType = "말티즈",
                treatment = "슬개골 탈구 수술"
            ),
            intent = {}
        )
    }
}