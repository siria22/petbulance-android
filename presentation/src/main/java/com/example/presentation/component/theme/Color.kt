package com.example.presentation.component.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val LightColorPalette = SiriaTemplateColorScheme(
    primary = Color(0xFF222051),
    lightPrimary = Color(0xFF8B87EA),
    secondary = Color(0xFF637387),
    commonText = Color(0xFF000000),
    descriptionText = Color(0xFF637387),
    warningText = Color(0xFFD32F2F),
    iconTint = Color(0xFF000000),
    inactivatedIconColor = Color(0xFFBCBCBC),
    inactivatedTextColor = Color(0xFF555555),
    bottomNavIconTint = Color(0xFF637387),
    surface = Color(0xFFFEFEFE),
    background = Color(0xFFF3F4F6),
    primaryButtonColor = Color(0xFF222051),
    onPrimaryButtonColor = Color(0xFFFFFFFF),
    secondaryButtonColor = Color(0xFF637387),
    onSecondaryButtonColor = Color(0xFFFFFFFF),
    isDark = false
)

private val DarkColorPalette = SiriaTemplateColorScheme(
    primary = Color(0xFF5A57C9),
    lightPrimary = Color(0xFF8B87EA),
    secondary = Color(0xFFB0BEC5),
    commonText = Color(0xFFF0F0F0),
    descriptionText = Color(0xFF90A4AE),
    warningText = Color(0xFFFF6659),
    iconTint = Color(0xFFF0F0F0),
    inactivatedIconColor = Color(0xFF555555),
    inactivatedTextColor = Color(0xFFBCBCBC),
    bottomNavIconTint = Color(0xFFB0BEC5),
    surface = Color(0xFF2C2C3A),
    background = Color(0xFF1A1A24),
    primaryButtonColor = Color(0xFF5A57C9),
    onPrimaryButtonColor = Color(0xFFF0F0F0),
    secondaryButtonColor = Color(0xFFB0BEC5),
    onSecondaryButtonColor = Color(0xFF000000),
    isDark = true
)

class SiriaTemplateColorScheme(
    var primary: Color,
    var lightPrimary: Color,
    var secondary: Color,
    var commonText: Color,
    var descriptionText: Color,
    var warningText: Color,
    var iconTint: Color,
    var inactivatedIconColor: Color,
    var inactivatedTextColor: Color,
    var bottomNavIconTint: Color,
    var surface: Color,
    var background: Color,
    var primaryButtonColor: Color,
    var onPrimaryButtonColor: Color,
    var secondaryButtonColor: Color,
    var onSecondaryButtonColor: Color,
    val isDark: Boolean
)

val LocalColorScheme = staticCompositionLocalOf { LightColorPalette }

@Composable
fun getColorScheme(darkTheme: Boolean): SiriaTemplateColorScheme {
    return if (darkTheme) DarkColorPalette else LightColorPalette
}

@Preview(name = "Light Color Palette", showBackground = true, widthDp = 300)
@Preview(
    name = "Dark Color Palette",
    showBackground = true,
    widthDp = 300,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ColorPalettePreview() {
    SiriaTemplateTheme {
        val colors = listOf(
            "primary" to SiriaTemplateTheme.colorScheme.primary,
            "lightPrimary" to SiriaTemplateTheme.colorScheme.lightPrimary,
            "secondary" to SiriaTemplateTheme.colorScheme.secondary,
            "commonText" to SiriaTemplateTheme.colorScheme.commonText,
            "descriptionText" to SiriaTemplateTheme.colorScheme.descriptionText,
            "iconTint" to SiriaTemplateTheme.colorScheme.iconTint,
            "inactivatedColor" to SiriaTemplateTheme.colorScheme.inactivatedIconColor,
            "inactivatedTextColor" to SiriaTemplateTheme.colorScheme.inactivatedTextColor,
            "bottomNavIconTint" to SiriaTemplateTheme.colorScheme.bottomNavIconTint,
            "surface" to SiriaTemplateTheme.colorScheme.surface,
            "background" to SiriaTemplateTheme.colorScheme.background,
            "primaryButtonColor" to SiriaTemplateTheme.colorScheme.primaryButtonColor,
            "onPrimaryButtonColor" to SiriaTemplateTheme.colorScheme.onPrimaryButtonColor,
            "secondaryButtonColor" to SiriaTemplateTheme.colorScheme.secondaryButtonColor,
            "onSecondaryButtonColor" to SiriaTemplateTheme.colorScheme.onSecondaryButtonColor,
        )

        val backgroundColor =
            if (SiriaTemplateTheme.colorScheme.isDark) Color.Black else Color.White
        LazyColumn(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            items(colors) { (name, color) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(width = 1.dp, color = Color.Black)
                            .background(color)
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = SiriaTemplateTheme.colorScheme.commonText,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}