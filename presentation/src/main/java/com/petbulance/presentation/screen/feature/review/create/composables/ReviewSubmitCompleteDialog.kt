package com.petbulance.presentation.screen.feature.review.create.composables

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
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun ReviewSubmitCompleteDialog(
    onDismissRequest: () -> Unit,
    onNavigateToReview: () -> Unit
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
                    text = "후기가 등록되었습니다.",
                    style = MaterialTheme.typography.titleMedium.emp(),
                    textAlign = TextAlign.Center,
                    color = PetbulanceTheme.colorScheme.text.primary
                )
                Text(
                    text = "작성해주신 후기는 검수 완료 후 공개됩니다.",
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
                    text = "닫기",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 12.dp,
                    onClicked = onDismissRequest
                )
                BasicButton(
                    modifier = Modifier.weight(1f),
                    text = "작성한 후기 보기",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 12.dp,
                    onClicked = onNavigateToReview
                )
            }
        }
    }   
}