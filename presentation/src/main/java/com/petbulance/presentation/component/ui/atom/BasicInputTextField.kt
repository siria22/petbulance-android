package com.petbulance.presentation.component.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingMedium

/**
 * A basic input text field component that provides a text input field with a placeholder and
 * a clear button.
 *
 * @param value The current value of the text field.
 * @param onValueChange The callback to be triggered when the text field value changes.
 * @param modifier (Optional) The modifier to be applied to the text field.
 * @param textStyle (Optional) The style to be applied to the text in the text field.
 * @param placeholder (Optional) The placeholder text to be displayed when the text field is stub.
 * @param singleLine (Optional) Whether the text field should be a single line or not.
 * @param keyboardOptions (Optional) The options for the keyboard to be displayed for the text field.
 */
@Composable
fun BasicInputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    placeholder: String = "placeholder",
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onSearchButtonClicked: () -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        textStyle = textStyle.copy(color = colorScheme.text.secondary),
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        cursorBrush = SolidColor(colorScheme.text.primary),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle.copy(color = colorScheme.text.disabled)
                        )
                    }
                    innerTextField()
                }
                if (value.isNotBlank()) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = "Clear all",
                        tint = colorScheme.icon.light,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                onValueChange("")
                            }
                            .rotate(45f)
                            .padding(end = spacingMedium)
                    )
                }

                val iconTint =
                    if (value.isEmpty()) colorScheme.text.disabled else colorScheme.text.secondary
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Filled.Search),
                    contentDescription = "Search icon",
                    size = iconSizeMS,
                    tint = iconTint,
                    modifier = Modifier.clickable {
                        onSearchButtonClicked()
                    }
                )
            }
        }
    )
}

@Preview(apiLevel = 34)
@Composable
private fun BasicInputTextFieldPreview() {
    BasicInputTextField(
        value = "",
        onValueChange = {},
        placeholder = "Placeholder",
        onSearchButtonClicked = {},
    )
}
