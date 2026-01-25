package com.petbulance.presentation.component.ui.atom

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double,
    maxRating: Int = 5,
    starSize: Dp = 24.dp,
    stepSize: Double = 0.1,
    activeColor: Color = colorScheme.icon.rating,
    inactiveColor: Color = colorScheme.icon.disabled,
    onRatingChanged: ((Double) -> Unit)? = null
) {
    val density = LocalDensity.current
    val starSizePx = with(density) { starSize.toPx() }

    Row(
        modifier = modifier.then(
            if (onRatingChanged != null) {
                Modifier
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val newRating =
                                calculateRating(offset.x, starSizePx, maxRating, stepSize)
                            onRatingChanged(newRating)
                        }
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, _ ->
                            val newRating =
                                calculateRating(change.position.x, starSizePx, maxRating, stepSize)
                            onRatingChanged(newRating)
                        }
                    }
            } else Modifier
        )
    ) {
        val fullStars = floor(rating).toInt()
        val partialStarFill = rating - fullStars
        val emptyStars = maxRating - ceil(rating).toInt()

        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = activeColor,
                modifier = Modifier.size(starSize)
            )
        }

        if (partialStarFill > 0) {
            PartialStar(
                fraction = partialStarFill.toFloat(),
                size = starSize,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }

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

private fun calculateRating(x: Float, starSizePx: Float, maxRating: Int, stepSize: Double): Double {
    val rawRating = x / starSizePx
    val rating = round(rawRating / stepSize) * stepSize
    val roundedRating = round(rating * 10) / 10.0

    return max(0.0, min(maxRating.toDouble(), roundedRating))
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

private class FractionalClip(private val fraction: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = size.width * fraction,
                bottom = size.height
            )
        )
    }
}