package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.hospital.review.ReviewRating
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.screen.feature.review.common.ReviewAnimalTypeInput
import com.petbulance.presentation.screen.feature.review.common.ReviewDetailAnimalTypeInput
import com.petbulance.presentation.screen.feature.review.common.ReviewRatingsSection
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateIntent
import com.petbulance.presentation.screen.feature.review.create.Step2State

@Composable
fun Step2AnimalContent(
    state: Step2State,
    intent: (ReviewCreateIntent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        modifier = Modifier
            .padding(vertical = spacingXL, horizontal = spacingMedium)
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        // 동물종 선택
        ReviewAnimalTypeInput(
            selectedAnimalType = state.animalType,
            onAnimalTypeSelected = { intent(ReviewCreateIntent.OnAnimalTypeChanged(it)) }
        )

        // 세부 동물명 입력
        ReviewDetailAnimalTypeInput(
            detailAnimalType = state.detailAnimalType,
            onDetailAnimalTypeChanged = { intent(ReviewCreateIntent.OnDetailAnimalTypeChanged(it)) }
        )

        // 별점 입력
        ReviewRatingsSection(
            ratings = state.ratings,
            onRatingChanged = { intent(ReviewCreateIntent.OnRatingChanged(it)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewCreateScreenStep2Preview() {
    PetbulanceTheme {
        Step2AnimalContent(
            state = Step2State(
                animalType = AnimalCategory.BIRD,
                detailAnimalType = "앵무새",
                ratings = ReviewRating(4.0, 5.0, 3.0)
            ),
            intent = {}
        )
    }
}