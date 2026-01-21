package com.petbulance.presentation.component.theme

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
import com.petbulance.domain.model.type.AppTheme
import com.petbulance.presentation.component.theme.color.LocalPetbulanceColorScheme
import com.petbulance.presentation.component.theme.color.PetbulanceColorScheme
import com.petbulance.presentation.component.theme.color.getPetbulanceColorScheme

private val DarkColorScheme = darkColorScheme()
private val LightColorScheme = lightColorScheme()

object PetbulanceTheme {
    val colorScheme: PetbulanceColorScheme
        @Composable
        get() = LocalPetbulanceColorScheme.current
}

@Composable
fun PetbulanceTheme(
    appTheme: AppTheme = AppTheme.DEVICE,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (appTheme) {
        AppTheme.DAYLIGHT -> false
        AppTheme.DARK -> true
        AppTheme.DEVICE -> isSystemInDarkTheme()
    }

    val customColorScheme = getPetbulanceColorScheme(useDarkTheme)

    val materialColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalPetbulanceColorScheme provides customColorScheme) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = PretendardTypography,
            content = content
        )
    }

}