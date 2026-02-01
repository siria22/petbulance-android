package com.petbulance.presentation.component.ui.atom

import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme

@Composable
fun CustomRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    color: Color = colorScheme.action.primary.default,
    size: Dp
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        colors = RadioButtonDefaults.colors(
            selectedColor = color,
            unselectedColor = color
        ),
        modifier = Modifier.size(size)
    )
}