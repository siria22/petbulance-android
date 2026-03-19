package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall

@Composable
fun CommentDeleteConfirmDialog(
    onConfirmDelete: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.bg.frame.default, RoundedCornerShape(12.dp))
                .padding(spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacingMedium)
        ) {
            Text(
                text = "댓글을 삭제할까요?",
                style = typography.titleSmall.emp(),
                color = colorScheme.text.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "댓글을 삭제하면 작성한 모든 데이터가 삭제되고 다시 볼 수 없어요.",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacingSmall)
            ) {
                Text(
                    text = "취소",
                    style = typography.bodyLarge,
                    color = colorScheme.text.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(colorScheme.bg.frame.subtle, RoundedCornerShape(8.dp))
                        .clickable { onDismissRequest() }
                        .padding(vertical = spacingMedium)
                )

                Text(
                    text = "삭제",
                    style = typography.bodyLarge.emp(),
                    color = colorScheme.bg.frame.default,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(colorScheme.status.error.default, RoundedCornerShape(8.dp))
                        .clickable { onConfirmDelete() }
                        .padding(vertical = spacingMedium)
                )
            }
        }
    }
}
