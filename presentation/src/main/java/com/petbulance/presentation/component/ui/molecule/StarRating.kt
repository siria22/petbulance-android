package com.petbulance.presentation.component.ui.molecule

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource

@Composable
fun StarRating(rating: Double, maxRating: Int = 5) {
    Row {
        for (i in 1..maxRating) {
            val starIcon = when {
                rating >= i -> Icons.Filled.Star
                rating >= i - 0.5 -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }
            BasicIcon(
                iconResource = IconResource.Vector(starIcon),
                contentDescription = "Star",
                size = 20.dp,
                tint = Color(0xFFFFBF0F)
            )
        }
    }
}