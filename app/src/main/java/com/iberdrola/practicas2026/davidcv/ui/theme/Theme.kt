package com.iberdrola.practicas2026.davidcv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColorScheme(
    primary = EnergyGreen,
    secondary = EnergyGreen,
    background = BackgroundWhite,
    surface = BackgroundWhite,
    onPrimary = White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun IB2026DavidCVTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorPalette,
        typography = Typography,
        content = content
    )
}