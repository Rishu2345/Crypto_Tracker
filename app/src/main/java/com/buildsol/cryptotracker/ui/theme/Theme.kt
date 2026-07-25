package com.buildsol.cryptotracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class PriceColorScheme(
    val positive: Color,
    val positiveContainer: Color,
    val negative: Color,
    val negativeContainer: Color,
    val chartLinePositive: Color = positive,
    val chartLineNegative: Color = negative
)

private val DarkPriceColors = PriceColorScheme(
    positive = PositiveDark,
    positiveContainer = PositiveContainerDark,
    negative = NegativeDark,
    negativeContainer = NegativeContainerDark
)

private val LightPriceColors = PriceColorScheme(
    positive = PositiveLight,
    positiveContainer = PositiveContainerLight,
    negative = NegativeLight,
    negativeContainer = NegativeContainerLight
)

val LocalPriceColors = staticCompositionLocalOf { DarkPriceColors }

private val DarkColors = darkColorScheme(
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    outline = OutlineDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    onBackground = OnSurfaceDark
)

private val LightColors = lightColorScheme(
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    outline = OutlineLight,
    onSurface = OnSurfaceLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    onBackground = OnSurfaceLight
)

@Composable
fun CryptoTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val priceColors = if (darkTheme) DarkPriceColors else LightPriceColors

    CompositionLocalProvider(LocalPriceColors provides priceColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = CryptoTypography,
            content = content
        )
    }
}


val MaterialTheme.priceColors: PriceColorScheme
    @Composable
    get() = LocalPriceColors.current