package com.petbulance.presentation.screen.feature.review.edit.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun ReviewEditSuccessDialog(
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest
    ) {
        Text(
            text = "후기가 수정되었습니다.",
            style = typography.titleSmall,
            color = colorScheme.text.primary
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicButton(
                text = "닫기",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.DEFAULT,
                radius = 28.dp,
                onClicked = onDismissRequest,
                modifier = Modifier.weight(1f)
            )
            BasicButton(
                text = "작성한 후기 확인",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.PRIMARY,
                radius = 28.dp,
                onClicked = onConfirmClick,
                modifier = Modifier.weight(2f)
            )
        }
    }
}