package com.example.presentation.component.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Spacer
@Composable
fun Space8() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun Space16() {
    Spacer(modifier = Modifier.height(16.dp))
}

// RoundedCornerShape
val SmallRoundedCorner = RoundedCornerShape(4.dp)
val DefaultRoundedCorner = RoundedCornerShape(12.dp)
val LargeRoundedCorner = RoundedCornerShape(16.dp)

val RoundedCorner = RoundedCornerShape(1000.dp)


// padding

val spacingXXXS = 2.dp
val spacingXXS = 4.dp
val spacingXS = 8.dp
val spacingSmall = 12.dp
val spacingMedium = 16.dp
val spacingLarge = 20.dp
val spacingXL = 24.dp
val spacingXXL = 32.dp

// ui size

val iconSizeXS = 12.dp
val iconSizeSmall = 16.dp
val iconSizeMs = 20.dp
val iconSizeMedium = 24.dp
val iconSizeLarge = 28.dp
