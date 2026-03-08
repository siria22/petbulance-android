package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.create.composables

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
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun QnaCreateSuccessDialog(
    isEditMode: Boolean = false,
    onDismiss: () -> Unit,
    onNavigateToDetail: () -> Unit
) {
    BasicDialog(
        backHandler = onDismiss,
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
                    text = if (isEditMode) "문의 수정이 완료되었어요" else "문의 제출이 완료되었어요",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = PetbulanceTheme.colorScheme.text.primary
                )
                Text(
                    text = "작성한 문의 메뉴에서 진행 상황을 확인할 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = PetbulanceTheme.colorScheme.text.caption
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicButton(
                    modifier = Modifier.weight(1f),
                    text = "닫기",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 28.dp,
                    onClicked = onDismiss
                )
                BasicButton(
                    modifier = Modifier.weight(1f),
                    text = "내 문의 확인",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 28.dp,
                    onClicked = onNavigateToDetail
                )
            }
        }
    }
}