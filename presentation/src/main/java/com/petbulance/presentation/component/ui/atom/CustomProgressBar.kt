package com.petbulance.presentation.component.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme

@Composable
fun CustomGreenLoader(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    val progressColor = colorScheme.action.primary.default
    val trackColor = Color(0xFFE0F2F1)

    CircularProgressIndicator(
        modifier = modifier.size(size),
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

@Composable
fun OnContentLoadingUi(text: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
    ) {
        CustomGreenLoader(size = 48.dp)

        Text(
            text = text,
            color = colorScheme.text.inverse,
            style = typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
@Preview(apiLevel = 34)
@Composable
private fun CustomGreenLoaderPreview() {
    PetbulanceTheme {
        CustomGreenLoader()
    }
}