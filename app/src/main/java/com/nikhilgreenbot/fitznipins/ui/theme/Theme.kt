package com.nikhilgreenbot.fitznipins.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ─── FitzNi Pins brand palette ────────────────────────────────────────────────
// Inspired by Disney's magical blue + gold + midnight navy

val FitzNiBlue        = Color(0xFF0047AB)   // Disney royal blue
val FitzNiGold        = Color(0xFFFFD700)   // Collector's gold
val FitzNiMidnight    = Color(0xFF0A1628)   // Deep navy background
val FitzNiStardust    = Color(0xFFB8D0F8)   // Soft blue highlight
val FitzNiRose        = Color(0xFFFF6B9D)   // Accent rose (for wishlist)
val FitzNiGoldLight   = Color(0xFFFFF0A0)
val FitzNiSurface     = Color(0xFF111827)
val FitzNiSurfaceVar  = Color(0xFF1E2D45)

private val DarkColorScheme = darkColorScheme(
    primary          = FitzNiGold,
    onPrimary        = FitzNiMidnight,
    primaryContainer = Color(0xFF1A3057),
    onPrimaryContainer = FitzNiGoldLight,
    secondary        = FitzNiStardust,
    onSecondary      = FitzNiMidnight,
    tertiary         = FitzNiRose,
    onTertiary       = Color.White,
    background       = FitzNiMidnight,
    onBackground     = Color(0xFFE8EFF8),
    surface          = FitzNiSurface,
    onSurface        = Color(0xFFE8EFF8),
    surfaceVariant   = FitzNiSurfaceVar,
    onSurfaceVariant = FitzNiStardust,
    outline          = Color(0xFF3A5070),
    error            = Color(0xFFFF6B6B),
    onError          = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary          = FitzNiBlue,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = Color(0xFF001A60),
    secondary        = Color(0xFF535F8C),
    onSecondary      = Color.White,
    tertiary         = Color(0xFFD41876),
    onTertiary       = Color.White,
    background       = Color(0xFFF8F9FF),
    onBackground     = Color(0xFF1A1C2A),
    surface          = Color.White,
    onSurface        = Color(0xFF1A1C2A),
    surfaceVariant   = Color(0xFFE2E7F5),
    onSurfaceVariant = Color(0xFF44496A),
    outline          = Color(0xFF7479A4),
    error            = Color(0xFFBA1A1A),
    onError          = Color.White,
)

@Composable
fun FitzNiPinsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme  -> DarkColorScheme
        else       -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = FitzNiTypography,
        content     = content,
    )
}
