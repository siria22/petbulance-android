package com.petbulance.presentation.screen.feature.review.camera

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
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun ReceiptAnalysisFailDialog(
    onDismissRequest: () -> Unit,
    onRetry: () -> Unit,
    onGalleryClick: () -> Unit,
    onManualInputClick: () -> Unit
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
                    text = "영수증을 인식하지 못했어요.",
                    style = MaterialTheme.typography.titleMedium.emp(),
                    textAlign = TextAlign.Center,
                    color = PetbulanceTheme.colorScheme.text.primary
                )
                Text(
                    text = "이미지가 흐리거나 빛 반사가 있을 수 있어요.\n영수증을 다시 촬영하거나 직접 입력해주세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = PetbulanceTheme.colorScheme.text.secondary
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicButton(
                    modifier = Modifier.weight(1f),
                    text = "앨범에서 선택",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 12.dp,
                    onClicked = onGalleryClick
                )
                BasicButton(
                    modifier = Modifier.weight(1f),
                    text = "다시 시도",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 12.dp,
                    onClicked = onRetry
                )
            }
        }
    }
}