package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateIntent
import com.petbulance.presentation.screen.feature.review.create.ReviewInputTextField
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
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS)
        ) {
            Text(
                text = "병원명",
                style = typography.titleSmall,
                color = colorScheme.text.secondary,
            )
            ReviewInputTextField(
                queryString = state.hospitalInfo?.name ?: "",
                placeholder = "병원명을 입력해주세요.",
                onQueryStringChanged = { name ->
                    intent(ReviewCreateIntent.OnHospitalSelected(name))
                }
            )
        }

        // 총 진료비 입력
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS)
        ) {
            Text(
                text = "총 진료비",
                style = typography.titleSmall,
                color = colorScheme.text.secondary,
            )
            ReviewInputTextField(
                queryString = state.totalPrice,
                placeholder = "진료비를 입력해주세요. (원)",
                onQueryStringChanged = { intent(ReviewCreateIntent.OnPriceChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            ReviewInputTextField(
                                queryString = treatment,
                                placeholder = "예: 중성화 수술, 슬개골 탈구, 예방접종",
                                onQueryStringChanged = {
                                    intent(ReviewCreateIntent.OnTreatmentChanged(index, it))
                                }
                            )
                        }

                        if (state.treatments.size > 1) {
                            BasicIcon(
                                iconResource = IconResource.Vector(Icons.Default.Close),
                                contentDescription = "Delete treatment",
                                size = 24.dp,
                                tint = colorScheme.icon.medium,
                                modifier = Modifier.clickable {
                                    intent(ReviewCreateIntent.OnTreatmentRemoved(index))
                                }
                            )
                        } // FIXME : X 안으로 들어가게
                    }
                }

                // FIXME : 항목 추가 버튼
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { intent(ReviewCreateIntent.OnTreatmentAdded) }
                        .padding(vertical = spacingXS),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Add),
                        contentDescription = "Add treatment",
                        size = 20.dp,
                        tint = colorScheme.action.primary.default
                    )
                    Text(
                        text = "진료 항목 추가",
                        style = typography.bodyMedium.emp(),
                        color = colorScheme.action.primary.default,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
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