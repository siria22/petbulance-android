package com.petbulance.presentation.component.ui.atom

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme

@Composable
fun BasicToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    width: Dp = 44.dp,
    height: Dp = 26.dp,
    thumbSize: Dp = 22.dp,
    borderWidth: Dp = 1.dp,
) {
    val shape = RoundedCornerShape(percent = 50)

    val trackColor = if (checked) {
        PetbulanceTheme.colorScheme.action.primary.default
    } else {
        PetbulanceTheme.colorScheme.bg.frame.medium
    }

    val borderColor = if (checked) {
        PetbulanceTheme.colorScheme.action.primary.default
    } else {
        PetbulanceTheme.colorScheme.bg.frame.medium
    }

    val thumbColor = PetbulanceTheme.colorScheme.bg.frame.default

    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f,
        label = "toggleAlpha"
    )

    val maxOffset = width - thumbSize - 2.dp
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) maxOffset else 2.dp,
        label = "thumbOffset"
    )

    Box(
        modifier = modifier
            .size(width = width, height = height)
            .alpha(alpha)
            .background(trackColor, shape)
            .border(borderWidth, borderColor, shape)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .background(thumbColor, CircleShape)
        )
    }
}