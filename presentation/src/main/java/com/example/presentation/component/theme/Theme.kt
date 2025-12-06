package com.example.presentation.component.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.example.domain.model.type.AppTheme

private val DarkColorScheme = darkColorScheme()
private val LightColorScheme = lightColorScheme()

object SiriaTemplateTheme {
    val colorScheme: SiriaTemplateColorScheme
        @Composable
        get() = LocalColorScheme.current
}

@Composable
fun SiriaTemplateTheme(
    appTheme: AppTheme = AppTheme.DEVICE,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (appTheme) {
        AppTheme.DAYLIGHT -> false
        AppTheme.DARK -> true
        AppTheme.DEVICE -> isSystemInDarkTheme()
    }

    val customColorScheme = getColorScheme(useDarkTheme)

    val materialColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalColorScheme provides customColorScheme) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = Typography,
            content = content
        )
    }

}