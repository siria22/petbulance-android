package com.petbulance.presentation.screen.feature.review.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme

@Composable
fun ReviewInputTextField(
    modifier: Modifier = Modifier,
    queryString: String,
    placeholder: String,
    onQueryStringChanged: (String) -> Unit,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    BasicTextField(
        value = queryString,
        onValueChange = onQueryStringChanged,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorScheme.bg.frame.default,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = colorScheme.border.verySubtle,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.text.secondary),
        singleLine = singleLine,
        cursorBrush = SolidColor(colorScheme.text.primary),
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
                ) {
                    if (queryString.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.text.disabled)
                        )
                    }
                    innerTextField()
                }
                trailingIcon?.invoke()
            }
        }
    )
}