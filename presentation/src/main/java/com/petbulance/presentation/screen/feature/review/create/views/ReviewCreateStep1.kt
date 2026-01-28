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
import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.review.common.ReviewAnimalTypeInput
import com.petbulance.presentation.screen.feature.review.common.ReviewDetailAnimalTypeInput
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

        // 세부 동물명
        ReviewDetailAnimalTypeInput(
            detailAnimalType = state.detailAnimalType,
            onDetailAnimalTypeChanged = { intent(ReviewCreateIntent.OnDetailAnimalTypeChanged(it)) }
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
            intent = {}
        )
    }
}