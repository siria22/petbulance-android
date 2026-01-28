package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.review.common.ReviewHospitalNameInput
import com.petbulance.presentation.screen.feature.review.common.ReviewTotalCostInput
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateIntent
import com.petbulance.presentation.screen.feature.review.common.ReviewInputTextField
import com.petbulance.presentation.screen.feature.review.create.Step1State

@Composable
fun Step1HospitalContent(
    state: Step1State,
    intent: (ReviewCreateIntent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = Modifier
            .padding(vertical = spacingXL, horizontal = spacingMedium)
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        // 병원명 입력
        ReviewHospitalNameInput(
            name = state.hospitalInfo?.name ?: "",
            onNameChanged = { intent(ReviewCreateIntent.OnHospitalSelected(it)) }
        )

        // 총 진료비 입력
        ReviewTotalCostInput(
            cost = state.totalPrice,
            onCostChanged = { intent(ReviewCreateIntent.OnPriceChanged(it)) }
        )

        // 진료 항목 입력
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
        ) {
            Text(
                text = "어떤 진료를 받으셨나요?",
                style = typography.titleSmall,
                color = colorScheme.text.secondary,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXXS),
            ) {
                state.treatments.forEachIndexed { index, treatment ->
                    TreatmentInputItem(
                        treatment = treatment,
                        placeholder = "예: 중성화 수술, 슬개골 탈구, 예방접종",
                        onValueChange = {
                            intent(ReviewCreateIntent.OnTreatmentChanged(index, it))
                        },
                        onDelete = {
                            intent(ReviewCreateIntent.OnTreatmentRemoved(index))
                        },
                        showDeleteButton = state.treatments.size > 1
                    )
                }

                AddTreatmentButton(
                    onAdd = { intent(ReviewCreateIntent.OnTreatmentAdded) }
                )
            }
        }
    }
}

@Composable
private fun AddTreatmentButton(
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = colorScheme.border.subtle
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorScheme.bg.frame.subtle,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onAdd() }
            .padding(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val stroke = Stroke(
                        width = 5.0f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                    )
                    drawRoundRect(
                        color = borderColor,
                        style = stroke,
                        cornerRadius = CornerRadius(5.dp.toPx())
                    )
                }
                .padding(horizontal = 16.dp, vertical = spacingXS),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Default.AddCircle),
                contentDescription = "Add treatment",
                size = iconSizeMedium,
                tint = colorScheme.icon.light
            )
        }
    }
}

@Composable
private fun TreatmentInputItem(
    treatment: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onDelete: () -> Unit,
    showDeleteButton: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReviewInputTextField(
            queryString = treatment,
            placeholder = placeholder,
            onQueryStringChanged = onValueChange,
            trailingIcon = if (showDeleteButton) {
                {
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Close),
                        contentDescription = "Delete treatment",
                        size = 20.dp,
                        tint = colorScheme.icon.medium,
                        modifier = Modifier.clickable { onDelete() }
                    )
                }
            } else null,
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = colorScheme.border.subtle,
                    shape = RoundedCornerShape(6.dp)
                )
        )
    }
}

@Preview
@Composable
fun Step1HospitalContentPreview() {
    PetbulanceTheme {
        Step1HospitalContent(
            state = Step1State(
                hospitalInfo = HospitalInfo(id = 0, name = "펫뷸런스 동물병원"),
                totalPrice = "100000",
                treatments = listOf("중성화 수술", "슬개골 탈구")
            ),
            intent = {}
        )
    }
}