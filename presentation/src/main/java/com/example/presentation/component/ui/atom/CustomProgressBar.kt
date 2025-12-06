package com.example.presentation.component.ui.atom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.SiriaTemplateTheme
import com.example.presentation.component.theme.SiriaTemplateTheme.colorScheme

@Composable
fun CustomGreenLoader(
    modifier: Modifier = Modifier
) {
    val progressColor = colorScheme.primary
    val trackColor = Color(0xFFE0F2F1)

    CircularProgressIndicator(
        modifier = modifier.size(24.dp),
        color = progressColor,
        trackColor = trackColor,
        strokeWidth = 4.dp,
        strokeCap = StrokeCap.Round
    )
}

@Composable
fun ContentPlaceholder() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CustomGreenLoader()
    }
}

@Preview(apiLevel = 34)
@Composable
private fun CustomGreenLoaderPreview() {
    SiriaTemplateTheme {
        CustomGreenLoader()
    }
}