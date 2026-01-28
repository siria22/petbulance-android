package com.petbulance.presentation.screen.feature.review.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.spacingXS

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
            style = MaterialTheme.typography.titleSmall,
            color = PetbulanceTheme.colorScheme.text.primary
        )
        Text(
            text = "지금 작성한 후기는 저장되지 않아요.",
            style = MaterialTheme.typography.bodySmall,
            color = PetbulanceTheme.colorScheme.text.caption
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