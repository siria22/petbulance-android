package com.petbulance.presentation.screen.feature.mypage.main

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
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXL

@Composable
fun LoginRequiredDialog(
    onDismiss: () -> Unit,
    onLoginButtonClicked: () -> Unit,
) {
    BasicDialog(
        backHandler = onDismiss,
        paddingValues = PaddingValues(horizontal = spacingXL, vertical = spacingXXL)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingLarge),
        ) {
            Text(
                text = "알림",
                style = MaterialTheme.typography.bodyMedium.emp(),
                color = PetbulanceTheme.colorScheme.text.caption,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "로그인이 필요한 서비스에요.\n로그인하시겠어요?",
                style = MaterialTheme.typography.titleSmall,
                color = PetbulanceTheme.colorScheme.text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicButton(
                    text = "그냥 볼래요",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 28.dp,
                    onClicked = onDismiss,
                    modifier = Modifier.weight(1f)
                )
                BasicButton(
                    text = "로그인",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 28.dp,
                    onClicked = onLoginButtonClicked,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}