package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall

@Composable
fun CoalitionSuccessDialog(
    onDismiss: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorScheme.bg.frame.default,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacingMedium)
        ) {
            Text(
                text = "문의 제출이 완료되었어요.",
                style = typography.titleMedium,
                color = colorScheme.text.primary
            )

            Text(
                text = "담당자 확인 후 연락드릴게요.",
                style = typography.bodyMedium,
                color = colorScheme.text.caption
            )

            Spacer(modifier = Modifier.height(spacingSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacingSmall)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorScheme.text.secondary
                    )
                ) {
                    Text(
                        text = "닫기",
                        style = typography.bodyMedium
                    )
                }

                Button(
                    onClick = onNavigateToHome,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.action.primary.default
                    )
                ) {
                    Text(
                        text = "홈으로",
                        style = typography.bodyMedium,
                        color = colorScheme.action.primary.text
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CoalitionSuccessDialogPreview() {
    PetbulanceTheme {
        CoalitionSuccessDialog(
            onDismiss = {},
            onNavigateToHome = {}
        )
    }
}
