package com.buildsol.cryptotracker.ui.theme

import androidx.compose.ui.graphics.Color

// ---------- Dark theme (primary target) ----------
val BackgroundDark = Color(0xFF0D0F14)
val SurfaceDark = Color(0xFF161920)
val SurfaceVariantDark = Color(0xFF1F232C)
val PrimaryDark = Color(0xFF6C5CE7)
val OnPrimaryDark = Color(0xFFFFFFFF)
val OutlineDark = Color(0xFF2A2E38)
val OnSurfaceDark = Color(0xFFE8E9ED)
val OnSurfaceVariantDark = Color(0xFF9BA1AC)

// ---------- Light theme (secondary, lower priority) ----------
val BackgroundLight = Color(0xFFF7F8FA)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFEFF1F4)
val PrimaryLight = Color(0xFF5B4BD6)
val OnPrimaryLight = Color(0xFFFFFFFF)
val OutlineLight = Color(0xFFDDE0E5)
val OnSurfaceLight = Color(0xFF1A1C20)
val OnSurfaceVariantLight = Color(0xFF5C6370)

// ---------- Semantic price colors ----------
// Deliberately NOT mapped to Material's error/tertiary roles — those get reused
// for form validation / other unrelated states elsewhere in M3, and coupling
// "price went down" to "error" is the kind of thing that bites you later
// (e.g. a red price chip suddenly styled like a form error banner).
val PositiveDark = Color(0xFF2ECC71)
val PositiveContainerDark = Color(0xFF1A2E22)
val NegativeDark = Color(0xFFFF5C5C)
val NegativeContainerDark = Color(0xFF2E1A1A)

val PositiveLight = Color(0xFF1E9E52)
val PositiveContainerLight = Color(0xFFDFF5E6)
val NegativeLight = Color(0xFFE0453F)
val NegativeContainerLight = Color(0xFFFBE2E1)

// Chart line color: reuse the same semantic tokens as the price chip so a
// green/red trend line always visually agrees with the % chip next to it.
// If you'd rather have a single neutral line regardless of trend direction,
// swap ChartLineColor() below to always return primary instead.