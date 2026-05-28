package com.nikhilgreenbot.fitznipins.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── FitzNi Pins brand palette — Disney midnight magic ────────────────────────
val FitzNiGold        = Color(0xFFFFD700)   // Collector's gold
val FitzNiMidnight    = Color(0xFF0A1628)   // Deep navy background
val FitzNiStardust    = Color(0xFFB8D0F8)   // Soft blue highlight
val FitzNiRose        = Color(0xFFFF6B9D)   // Accent rose (wishlist)
val FitzNiGoldLight   = Color(0xFFFFF0A0)   // Gold on dark containers
val FitzNiSurface     = Color(0xFF111827)   // Card surface
val FitzNiSurfaceVar  = Color(0xFF1E2D45)   // Elevated surface

// ─── Single dark colour scheme — FitzNi always runs dark ──────────────────────
// We intentionally skip light theme and dynamic colour.
// Disney-magical means gold on midnight navy, always.
private val FitzNiColorScheme = darkColorScheme(
    primary             = FitzNiGold,
    onPrimary           = FitzNiMidnight,
    primaryContainer    = Color(0xFF1A3057),
    onPrimaryContainer  = FitzNiGoldLight,
    secondary           = FitzNiStardust,
    onSecondary         = FitzNiMidnight,
    secondaryContainer  = Color(0xFF0F2240),
    onSecondaryContainer = FitzNiStardust,
    tertiary            = FitzNiRose,
    onTertiary          = Color.White,
    tertiaryContainer   = Color(0xFF3D0A1E),
    onTertiaryContainer = FitzNiRose,
    background          = FitzNiMidnight,
    onBackground        = Color(0xFFE8EFF8),
    surface             = FitzNiSurface,
    onSurface           = Color(0xFFE8EFF8),
    surfaceVariant      = FitzNiSurfaceVar,
    onSurfaceVariant    = FitzNiStardust,
    outline             = Color(0xFF3A5070),
    outlineVariant      = Color(0xFF1E3050),
    error               = Color(0xFFFF6B6B),
    onError             = Color.White,
    errorContainer      = Color(0xFF5C1A1A),
    onErrorContainer    = Color(0xFFFFB3B3),
    scrim               = Color(0x99000000),
    inverseSurface      = Color(0xFFE8EFF8),
    inverseOnSurface    = FitzNiMidnight,
    inversePrimary      = Color(0xFF0047AB),
)

@Composable
fun FitzNiPinsTheme(
    content: @Composable () -> Unit,
) {
    // Always dark — FitzNi Pins is a night-sky, magical Disney experience.
    // No dynamic colour, no light mode override.
    MaterialTheme(
        colorScheme = FitzNiColorScheme,
        typography  = FitzNiTypography,
        content     = content,
    )
}
