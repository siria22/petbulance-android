package com.example.presentation.component.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.SiriaTemplateTheme

@Composable
fun BasicButton(
    text: String,
    type: ButtonType,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = when (type) {
        ButtonType.PRIMARY -> SiriaTemplateTheme.colorScheme.primaryButtonColor
        ButtonType.SECONDARY -> SiriaTemplateTheme.colorScheme.secondaryButtonColor
        ButtonType.DEFAULT -> SiriaTemplateTheme.colorScheme.surface
    }

    val textColor = when (type) {
        ButtonType.PRIMARY -> SiriaTemplateTheme.colorScheme.onPrimaryButtonColor
        ButtonType.SECONDARY -> SiriaTemplateTheme.colorScheme.onSecondaryButtonColor
        ButtonType.DEFAULT -> SiriaTemplateTheme.colorScheme.commonText
    }

    Box(
        modifier = modifier
            .clickable { onClicked() }
            .background(buttonColor)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

enum class ButtonType {
    PRIMARY, SECONDARY, DEFAULT
}

@Preview
@Composable
private fun AppButtonPreview() {
    SiriaTemplateTheme {
        BasicButton(
            text = "Button",
            onClicked = {},
            type = ButtonType.PRIMARY
        )
    }
}