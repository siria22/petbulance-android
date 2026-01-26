package com.petbulance.presentation.screen.feature.review.create.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.RatingBar
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.review.create.ReviewCreateStep

@Composable
fun ReviewRatingItem(
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
            onRatingChanged = onRatingChanged,
            isEditable = true
        )
    }
}

@Composable
fun ReceiptVerifiedCard() {
    Row(
        modifier = Modifier.padding(horizontal = spacingLarge, vertical = spacingXL),
        horizontalArrangement = Arrangement.spacedBy(spacingXS),
    ){
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.CheckCircle),
            contentDescription = "CircleCheck",
            size = 36.dp,
            tint = colorScheme.tag.trust.medium
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXS),
        ) {
            Text(
                text = "영수증 인증 완료",
                style = typography.titleSmall,
                color = colorScheme.text.primary
            )
            Text(
                text = "이제 후기를 작성할 수 있습니다.",
                style = typography.bodySmall,
                color = colorScheme.text.caption
            )
        }
    }
}

@Composable
fun ExitDialog(
    onDismissRequest: () -> Unit,
    onExitButtonClicked: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest
    ) {
        Text(
            text = "후기 작성을 중단하고 나가시겠어요?",
            style = typography.titleSmall,
            color = colorScheme.text.primary
        )
        Text(
            text = "지금 작성한 후기는 저장되지 않아요.",
            style = typography.bodySmall,
            color = colorScheme.text.caption
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicButton(
                modifier = Modifier.weight(1f),
                text = "취소",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.DEFAULT,
                radius = 28.dp,
                onClicked = onDismissRequest
            )

            BasicButton(
                modifier = Modifier.weight(1f),
                text = "나가기",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.DEFAULT,
                radius = 28.dp,
                onClicked = onExitButtonClicked
            )
        }
    }
}

@Preview
@Composable
fun ReviewProgressBarPreview() {
    ReviewProgressBar(currentStep = ReviewCreateStep.HOSPITAL_AND_COST)
}