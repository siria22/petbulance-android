package com.petbulance.presentation.screen.feature.mypage.sections.help.terms.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL

@Composable
fun RequiredTermsWithdrawDialog(
    onDismiss: () -> Unit,
    onWithdrawConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorScheme.bg.frame.default,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = spacingXL, horizontal = spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacingXL)
        ) {
            Text(
                text = "필수 이용약관을 철회하시면\n펫뷸런스 서비스를 탈퇴해야 합니다",
                style = typography.titleSmall,
                color = colorScheme.text.primary,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacingSmall)
            ) {
                BasicButton(
                    modifier = Modifier.weight(1f),
                    text = "약관 철회",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 28.dp
                ) { onWithdrawConfirm() }

                BasicButton(
                    modifier = Modifier.weight(1f),
                    text = "닫기",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.DEFAULT,
                    radius = 28.dp
                ) { onDismiss() }
            }
        }
    }
}

@Preview
@Composable
private fun RequiredTermsWithdrawDialogPreview() {
    PetbulanceTheme {
        RequiredTermsWithdrawDialog(
            onDismiss = {},
            onWithdrawConfirm = {}
        )
    }
}
