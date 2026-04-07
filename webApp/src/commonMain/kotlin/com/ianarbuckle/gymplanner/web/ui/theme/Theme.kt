package com.ianarbuckle.gymplanner.web.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF0D0D0D)
val White = Color(0xFFFFFFFF)
val OffWhite = Color(0xFFF5F5F7)
val LightGrey = Color(0xFFE8E8E8)
val MediumGrey = Color(0xFF9E9E9E)
val DarkGrey = Color(0xFF424242)

val GymPlannerColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = Black,
    onPrimaryContainer = White,
    secondary = DarkGrey,
    onSecondary = White,
    background = OffWhite,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    onSurfaceVariant = MediumGrey,
    outline = LightGrey,
    outlineVariant = LightGrey,
)
