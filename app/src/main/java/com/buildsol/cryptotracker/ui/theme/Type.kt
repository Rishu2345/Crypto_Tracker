package com.buildsol.cryptotracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


private val AppFontFamily = FontFamily.Default


val CryptoTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 17.sp
    ),
    labelMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)

// ---------- Tabular-number price/percentage styles ----------
// These are the styles the checklist calls out explicitly: every numeric
// price/% value needs fontFeatureSettings = "tnum" so digits don't jitter or
// misalign when a list row refreshes/paginates (e.g. "$1,234.56" -> "$987.10"
// shouldn't visually shift width mid-column).
//
// NOT part of CryptoTypography/M3's Typography type scale on purpose — M3's
// TextStyle roles are for general text, and tabular figures are a narrow,
// deliberate exception you opt into only for price/% values, not app-wide.

val PriceLarge = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 38.sp,
    fontFeatureSettings = "tnum"
)

val PriceMedium = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    fontFeatureSettings = "tnum"
)

val PricePercentage = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 13.sp,
    lineHeight = 16.sp,
    fontFeatureSettings = "tnum"
)

val PriceStatValue = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    lineHeight = 20.sp,
    fontFeatureSettings = "tnum"
)
