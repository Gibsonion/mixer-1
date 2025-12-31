package com.mixer.one.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Nothing OS Dark Color Scheme (primary theme)
 */
private val NothingDarkColorScheme = darkColorScheme(
    primary = NothingColors.White,
    onPrimary = NothingColors.Black,
    secondary = NothingColors.GreyMedium,
    onSecondary = NothingColors.White,
    background = NothingColors.Black,
    onBackground = NothingColors.White,
    surface = NothingColors.GreyContainer,
    onSurface = NothingColors.White,
    error = NothingColors.Red,
    onError = NothingColors.White
)

/**
 * Mixer (1) Theme - Nothing OS inspired
 */
@Composable
fun MixerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NothingDarkColorScheme,
        typography = NothingTypography,
        content = content
    )
}
