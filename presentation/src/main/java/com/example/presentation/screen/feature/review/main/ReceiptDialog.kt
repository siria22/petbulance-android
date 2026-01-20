package com.example.presentation.screen.feature.review.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.spacingLarge
import com.example.presentation.component.ui.spacingXS

@Composable
fun ReceiptDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onConfirmWithoutReceipt: () -> Unit,
) {
    BasicDialog(
        backHandler = onDismissRequest,
        paddingValues = PaddingValues(horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXS),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "후기를 작성하기 전에\n영수증 인증을 진행하시겠어요?",
                    style = MaterialTheme.typography.titleMedium.emp(),
                    textAlign = TextAlign.Center,
                    color = PetbulanceTheme.colorScheme.text.primary
                )
                Text(
                    text = "카드 및 현금으로 결제한 영수증만\n인증 가능합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = PetbulanceTheme.colorScheme.text.secondary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicButton(
                        modifier = Modifier.weight(5f),
                        text = "인증 없이 작성",
                        size = BasicButtonSize.L,
                        buttonType = BasicButtonType.SECONDARY,
                        radius = 28.dp,
                        onClicked = onConfirm
                    )
                    BasicButton(
                        modifier = Modifier.weight(3f),
                        text = "네",
                        size = BasicButtonSize.L,
                        buttonType = BasicButtonType.PRIMARY,
                        radius = 28.dp,
                        onClicked = onConfirmWithoutReceipt
                    )
                }
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun ReceiptDialogPreview() {
    PetbulanceTheme() {
        ReceiptDialog({}, {}, {})
    }
}
