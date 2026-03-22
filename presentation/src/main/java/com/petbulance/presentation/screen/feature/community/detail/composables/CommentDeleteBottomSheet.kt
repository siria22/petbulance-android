package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentDeleteBottomSheet(
    onEditOptionClicked: () -> Unit,
    onDeleteOptionClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = colorScheme.bg.frame.default
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacingSmall)
        ) {
            Text(
                text = "댓글 수정",
                style = typography.bodyLarge,
                color = colorScheme.text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEditOptionClicked() }
                    .padding(vertical = spacingMedium)
            )

            Text(
                text = "댓글 삭제",
                style = typography.bodyLarge,
                color = colorScheme.status.error.default,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDeleteOptionClicked() }
                    .padding(vertical = spacingMedium)
            )
        }
    }
}
