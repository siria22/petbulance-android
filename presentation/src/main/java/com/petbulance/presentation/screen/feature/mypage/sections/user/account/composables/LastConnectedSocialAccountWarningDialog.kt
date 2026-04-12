package com.petbulance.presentation.screen.feature.mypage.sections.user.account.composables

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
fun LastConnectedSocialAccountWarningDialog(
    onDismissRequest: () -> Unit,
    onNavigateToSupport: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest
    ) {
        Text(
            text = "소셜 로그인은 최소 1개 이상 연결되어 있어야 해요",
            style = typography.titleSmall,
            color = colorScheme.text.primary
        )

        Text(
            text = "서비스 탈퇴를 원하시면 마이페이지>문의및고객센터>문의 작성에서 탈퇴 요청글을 작성해주세요.",
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
                text = "탈퇴 문의 작성",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.SECONDARY,
                radius = 28.dp,
                onClicked = onNavigateToSupport
            )
        }
    }
}