package com.petbulance.presentation.component.ui.molecule

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
fun WarningDialog(
    title: String,
    content: String,
    cancelText: String = "취소",
    confirmText: String = "나가기",
    onDismissRequest: () -> Unit,
    onExitButtonClicked: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = PetbulanceTheme.colorScheme.text.primary
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = PetbulanceTheme.colorScheme.text.caption
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicButton(
                modifier = Modifier.weight(1f),
                text = cancelText,
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.DEFAULT,
                radius = 28.dp,
                onClicked = onDismissRequest
            )

            BasicButton(
                modifier = Modifier.weight(1f),
                text = confirmText,
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.WARNING,
                radius = 28.dp,
                onClicked = onExitButtonClicked
            )
        }
    }
}