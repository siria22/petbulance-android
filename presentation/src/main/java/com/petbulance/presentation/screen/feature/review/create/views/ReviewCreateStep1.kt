package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.screen.feature.review.common.ReviewAnimalTypeInput
import com.petbulance.presentation.screen.feature.review.common.ReviewDetailAnimalTypeInput
import com.petbulance.presentation.screen.feature.review.common.ReviewHospitalNameInput
import com.petbulance.presentation.screen.feature.review.common.ReviewTotalCostInput
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateIntent
import com.petbulance.presentation.screen.feature.review.create.Step1State

@Composable
fun Step1HospitalContent(
    state: Step1State,
    intent: (ReviewCreateIntent) -> Unit,
    onDetailAnimalInputClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        modifier = Modifier
            .padding(vertical = spacingXL, horizontal = spacingMedium)
            .padding(bottom = 80.dp)
    ) {
        // 병원명 입력
        ReviewHospitalNameInput(
            query = state.hospitalQuery,
            selectedHospitalName = state.hospitalInfoForReview?.name,
            candidates = state.hospitalCandidates,
            onQueryChanged = { intent(ReviewCreateIntent.OnHospitalQueryChanged(it)) },
            onClearQuery = { intent(ReviewCreateIntent.OnHospitalClearClicked) },
            onCandidateClicked = { intent(ReviewCreateIntent.OnHospitalCandidateSelected(it)) }
        )

        // 총 진료비 입력
        ReviewTotalCostInput(
            cost = state.totalPrice,
            onCostChanged = { intent(ReviewCreateIntent.OnPriceChanged(it)) }
        )

        // 동물종
        ReviewAnimalTypeInput(
            selectedAnimalType = state.animalType,
            onAnimalTypeSelected = { intent(ReviewCreateIntent.OnAnimalTypeChanged(it)) }
        )

        val displayDetailAnimalType = if (state.detailAnimalType.isNotBlank()) {
            AnimalSpecies.fromString(state.detailAnimalType).korean
        } else {
            ""
        }

        ReviewDetailAnimalTypeInput(
            detailAnimalType = displayDetailAnimalType,
            onInputClicked = onDetailAnimalInputClicked
        )
    }
}

@Preview
@Composable
fun Step1HospitalContentPreview() {
    PetbulanceTheme {
        Step1HospitalContent(
            state = Step1State(
                hospitalInfoForReview = HospitalInfoForReview(id = 0, name = "펫뷸런스 동물병원"),
                totalPrice = "100000",
            ),
            intent = {},
            onDetailAnimalInputClicked = {}
        )
    }
}