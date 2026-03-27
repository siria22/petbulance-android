package com.petbulance.presentation.screen.feature.mypage.sections.activity.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.spacingSmall

@Composable
fun ActivityUndoToast(
    message: String,
    onUndo: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color(0xFF222222).copy(alpha = 0.9f),
                RoundedCornerShape(4.dp)
            )
            .padding(spacingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            style = typography.bodySmall,
            color = colorScheme.text.inverse
        )
        if (onUndo != null) {
            Text(
                text = "취소",
                style = typography.bodySmall.emp(),
                color = colorScheme.text.inverse,
                modifier = Modifier.clickable { onUndo() }
            )
        }
    }
}
