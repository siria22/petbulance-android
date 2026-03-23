package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall

@Composable
fun PostAndCommentDeleteBottomSheet(
    deleteText: String,
    editText: String,
    onDeleteOptionClicked: () -> Unit,
    onEditOptionClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.5f))
            .clickable(
                enabled = true,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismissRequest() }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(vertical = spacingMedium, horizontal = spacingLarge)
                .padding(bottom = 60.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(
                    colorScheme.bg.frame.default,
                    RoundedCornerShape(12.dp)
                )
            ) {
                Text(
                    text = deleteText,
                    style = typography.bodyLarge,
                    color = colorScheme.status.error.default,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable { onDeleteOptionClicked() }
                )
                CommonDivider()
                Text(
                    text = editText,
                    style = typography.bodyLarge,
                    color = colorScheme.text.disabled,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable { onEditOptionClicked() }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        colorScheme.bg.frame.default,
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Text(
                    text = "취소",
                    style = typography.bodyLarge,
                    color = colorScheme.text.tertiary,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable { onDismissRequest() }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PostAndCommentDeleteBottomSheetPreview() {
    PetbulanceTheme {
        PostAndCommentDeleteBottomSheet(
            deleteText = "게시글 삭제",
            editText = "수정",
            onDeleteOptionClicked = {},
            onEditOptionClicked = {},
            onDismissRequest = {}
        )
    }
}
