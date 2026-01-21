package com.petbulance.presentation.screen.feature.search.main.views.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.iconSizeXS
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.component.ui.spacingXXXS

@Composable
fun RecenterSearchButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(999.dp)
    val borderColor = colorScheme.action.primary.default
    val contentColor = colorScheme.action.primary.default
    val bgColor = colorScheme.bg.frame.default

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingXXXS),
        modifier = modifier
            .clip(shape)
            .background(bgColor, shape)
            .border(BorderStroke(1.dp, borderColor), shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = spacingXS, vertical = spacingXXS)
    ) {
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.Refresh),
            contentDescription = "Recenter and search",
            size = iconSizeXS,
            tint = contentColor
        )
        Text(
            text = "현 지도에서 검색",
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}

@Composable
fun MapViewToggleButton(
    isToggleToListView: Boolean,
    onClicked: () -> Unit
) {
    val shape = RoundedCornerShape(1000.dp)
    val icon = if (isToggleToListView) Icons.AutoMirrored.Filled.List else Icons.Outlined.Map
    val text = if (isToggleToListView) "목록보기" else "지도보기"
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingXXXS),
        modifier = Modifier
            .clip(shape)
            .background(colorScheme.action.primary.default, shape)
            .clickable { onClicked() }
            .padding(horizontal = spacingSmall, vertical = spacingXS)
    ) {
        BasicIcon(
            iconResource = IconResource.Vector(icon),
            contentDescription = "Switch Map and List View",
            size = iconSizeMS,
            tint = colorScheme.icon.inverse
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = colorScheme.text.inverse
        )
    }
}

@Composable
fun CurrentLocationFab(
    onClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(50))
            .background(colorScheme.bg.frame.default, shape = CircleShape)
            .clickable { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.LocationSearching),
            contentDescription = "Current location",
            tint = colorScheme.icon.basic,
            size = iconSizeMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecenterSearchButtonPreview() {
    PetbulanceTheme {
        Column() {
            RecenterSearchButton(onClick = {})
            MapViewToggleButton(isToggleToListView = false, onClicked = {})
            MapViewToggleButton(isToggleToListView = true, onClicked = {})
        }
    }
}
