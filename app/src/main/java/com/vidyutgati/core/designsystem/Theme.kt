package com.vidyutgati.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// High-Contrast Daylight Palette for Outdoor E-Rickshaw Visibility
val ElectricAmber = Color(0xFFF59E0B)
val ElectricAmberDark = Color(0xFFD97706)
val DeepObsidian = Color(0xFF0F172A)
val SurfaceSlate = Color(0xFF1E293B)
val BorderSlate = Color(0xFF334155)

val EmeraldProfit = Color(0xFF10B981)
val EmeraldDark = Color(0xFF059669)
val CyanRoute = Color(0xFF06B6D4)
val CoralAlert = Color(0xFFEF4444)

val TextHighContrast = Color(0xFFFFFFFF)
val TextMuted = Color(0xFF94A3B8)
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)

private val DarkColorScheme = darkColorScheme(
    primary = ElectricAmber,
    onPrimary = DeepObsidian,
    primaryContainer = ElectricAmberDark,
    onPrimaryContainer = TextHighContrast,
    secondary = CyanRoute,
    onSecondary = DeepObsidian,
    background = DeepObsidian,
    onBackground = TextHighContrast,
    surface = SurfaceSlate,
    onSurface = TextHighContrast,
    surfaceVariant = BorderSlate,
    onSurfaceVariant = TextMuted,
    error = CoralAlert
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricAmberDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7),
    onPrimaryContainer = Color(0xFF78350F),
    secondary = CyanRoute,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = Color(0xFF0F172A),
    surface = LightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    error = CoralAlert
)

@Composable
fun VidyutGatiTheme(
    darkTheme: Boolean = true, // Default to Dark High-Contrast mode for battery efficiency & glare reduction
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
