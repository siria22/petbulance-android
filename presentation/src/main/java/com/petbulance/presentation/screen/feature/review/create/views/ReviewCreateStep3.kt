package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.screen.feature.review.common.ReviewContentInput
import com.petbulance.presentation.screen.feature.review.common.ReviewImageSection
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateIntent
import com.petbulance.presentation.screen.feature.review.create.Step3State

@Composable
fun Step3ReviewContent(
    state: Step3State,
    intent: (ReviewCreateIntent) -> Unit,
    onImageAddClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        modifier = Modifier.padding(vertical = spacingXL, horizontal = spacingMedium)
    ) {
        // 사진 첨부
        ReviewImageSection(
            images = state.images,
            onImageAddClicked = onImageAddClicked,
            onImageDeleteClicked = { index ->
                val newList = state.images.toMutableList().apply { removeAt(index) }
                intent(ReviewCreateIntent.OnImagesChanged(newList))
            }
        )

        // 후기 내용 입력
        ReviewContentInput(
            content = state.content,
            onContentChanged = { intent(ReviewCreateIntent.OnContentChanged(it)) }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ReviewCreateScreenStep3Preview() {
    PetbulanceTheme {
        Step3ReviewContent(
            state = Step3State(
                content = "선생님이 정말 친절하시고 설명도 잘 해주셨어요. 수술 경과도 좋아서 만족합니다."
            ),
            intent = {},
            onImageAddClicked = {}
        )
    }
}