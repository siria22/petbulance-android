package com.petbulance.presentation.component.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme

@Composable
fun BasicFabIcon(
    iconResource: IconResource,
    size: Dp = 56.dp,
    iconSize: Dp = 36.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(colorScheme.action.primary.default, RoundedCornerShape(1000.dp))
            .clickable(onClick = onClick)
    ) {
        BasicIcon(
            iconResource = iconResource,
            size = iconSize,
            contentDescription = "FAB Icon",
            tint = colorScheme.icon.inverse,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}