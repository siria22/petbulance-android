package com.example.presentation.component.ui.atom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double,
    maxRating: Int = 5,
    starSize: Dp = 24.dp,
    activeColor: Color = colorScheme.icon.rating,
    inactiveColor: Color = colorScheme.icon.disabled
) {
    Row(modifier = modifier) {
        val fullStars = floor(rating).toInt()
        val partialStarFill = rating - fullStars
        val emptyStars = maxRating - ceil(rating).toInt()

        // Full stars
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = activeColor,
                modifier = Modifier.size(starSize)
            )
        }

        // Partial star
        if (partialStarFill > 0) {
            PartialStar(
                fraction = partialStarFill.toFloat(),
                size = starSize,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }

        // Empty stars
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = inactiveColor,
                modifier = Modifier.size(starSize)
            )
        }
    }
}

@Composable
private fun PartialStar(
    fraction: Float,
    size: Dp,
    activeColor: Color,
    inactiveColor: Color
) {
    Box(modifier = Modifier.size(size)) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = inactiveColor,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(FractionalClip(fraction))
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = activeColor,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

private class FractionalClip(private val fraction: Float) : androidx.compose.ui.graphics.Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): androidx.compose.ui.graphics.Outline {
        return androidx.compose.ui.graphics.Outline.Rectangle(
            rect = androidx.compose.ui.geometry.Rect(
                left = 0f,
                top = 0f,
                right = size.width * fraction,
                bottom = size.height
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun RatingBarPreview() {
    PetbulanceTheme {
        Column {
            RatingBar(rating = 4.8)
            RatingBar(rating = 3.5)
            RatingBar(rating = 2.2)
            RatingBar(rating = 1.0)
            RatingBar(rating = 0.4)
        }
    }
}