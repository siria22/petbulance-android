package com.example.presentation.component.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.SmallRoundedCorner
import com.example.presentation.component.ui.Space8


@Composable
fun BasicCheckBox(
    checkState: Boolean,
    onCheckedChange: () -> Unit,
    isChangeable: Boolean = true,
) {
    val borderColor = if (isChangeable) {
        PetbulanceTheme.colorScheme.icon.basic
    } else {
        PetbulanceTheme.colorScheme.action.link.pressed
    }

    val backgroundColor = if (isChangeable) {
        if (checkState) {
            PetbulanceTheme.colorScheme.bg.icon.pressed
        } else {
            Color.Transparent
        }
    } else {
        if (checkState) {
            PetbulanceTheme.colorScheme.icon.basic
        } else {
            Color.Transparent
        }
    }

    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = SmallRoundedCorner
            )
            .background(
                color = backgroundColor,
                shape = SmallRoundedCorner
            )
            .size(18.dp)
            .clickable {
                if (isChangeable) {
                    onCheckedChange()
                }
            }
    ) {
        if (checkState) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Check",
                tint = PetbulanceTheme.colorScheme.icon.gnb.selected
            )
        }
    }
}

@Preview
@Composable
private fun AppCheckBoxPreview() {
    PetbulanceTheme {
        Column {
            BasicCheckBox(checkState = true, onCheckedChange = {})
            BasicCheckBox(checkState = false, onCheckedChange = {})
            Space8()
            BasicCheckBox(checkState = true, onCheckedChange = {}, isChangeable = false)
            BasicCheckBox(checkState = false, onCheckedChange = {}, isChangeable = false)
        }
    }
}