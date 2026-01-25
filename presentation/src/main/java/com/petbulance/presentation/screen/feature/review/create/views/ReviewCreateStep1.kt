package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
import com.petbulance.domain.model.feature.hospital.review.ReviewRating
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.RatingBar
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateIntent
import com.petbulance.presentation.screen.feature.review.create.ReviewInputTextField
import com.petbulance.presentation.screen.feature.review.create.Step1State

@Composable
fun Step1HospitalContent(
    state: Step1State,
    intent: (ReviewCreateIntent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = Modifier.padding(vertical = spacingXL, horizontal = spacingMedium)
    ) {
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
                    intent(ReviewCreateIntent.OnHospitalSelected(HospitalInfo(id = 0, name = name)))
                },
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXL),
        ) {
            Text(
                text = "솔직한 리뷰를 남겨주세요",
                style = typography.titleMedium.emp(),
                color = colorScheme.text.secondary
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXL),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Step1RatingItems(
                    title = "전문성",
                    desc = "증상과 치료에 대해 자세히 설명했나요?",
                    currentRating = state.ratings.expertise,
                    onRatingChanged = { newRating ->
                        intent(ReviewCreateIntent.OnRatingChanged(state.ratings.copy(expertise = newRating)))
                    }
                )
                Step1RatingItems(
                    title = "친절도",
                    desc = "접수/수납 과정에서 충분한 안내를 받았나요?",
                    currentRating = state.ratings.kindness,
                    onRatingChanged = { newRating ->
                        intent(ReviewCreateIntent.OnRatingChanged(state.ratings.copy(kindness = newRating)))
                    }
                )
                Step1RatingItems(
                    title = "시설/환경",
                    desc = "진료실과 병원 시설이 위생적이었나요?",
                    currentRating = state.ratings.facility,
                    onRatingChanged = { newRating ->
                        intent(ReviewCreateIntent.OnRatingChanged(state.ratings.copy(facility = newRating)))
                    }
                )
            }
        }
    }
}

@Composable
private fun Step1RatingItems(
    title: String,
    desc: String,
    currentRating: Double,
    onRatingChanged: (Double) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = typography.titleSmall.emp(),
                color = colorScheme.text.secondary,
            )
            Text(
                text = desc,
                style = typography.bodySmall,
                color = colorScheme.text.caption
            )
        }

        RatingBar(
            rating = currentRating,
            onRatingChanged = onRatingChanged
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewCreateScreenStep1Preview() {
    PetbulanceTheme {
        Step1HospitalContent(
            state = Step1State(
                hospitalInfo = HospitalInfo(1, "행복 동물병원"),
                ratings = ReviewRating(4.5, 5.0, 4.0)
            ),
            intent = {}
        )
    }
}