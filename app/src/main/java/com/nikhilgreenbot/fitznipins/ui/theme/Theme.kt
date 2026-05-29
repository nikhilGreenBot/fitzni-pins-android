package com.nikhilgreenbot.fitznipins.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── FitzNi Pins base palette (Katie's three colors) ─────────────────────────
/** Muted gold / tan — primary accent, titles, CTAs */
val FitzNiTan           = Color(0xFFD7BA89)

/** Deep navy — backgrounds, on-primary text */
val FitzNiNavy          = Color(0xFF192A51)

/** Dusty steel blue — body text, icons, secondary accents */
val FitzNiSteel         = Color(0xFF8B9EB7)

// ─── Derived tones (same family, better contrast on navy) ────────────────────
val FitzNiTanLight      = Color(0xFFE8D4B5)
val FitzNiSteelLight    = Color(0xFFA8B8CA)
val FitzNiNavyElevated  = Color(0xFF243A66)
val FitzNiSurface       = Color(0xFF1F3156)
val FitzNiSurfaceVar    = Color(0xFF2A4062)
val FitzNiCardSolid     = Color(0xFF314B6E)

// ─── Semantic aliases (existing screens import these names) ──────────────────
val FitzNiGold          = FitzNiTan
val FitzNiMidnight      = FitzNiNavy
val FitzNiTwilight      = FitzNiNavyElevated
val FitzNiStardust      = FitzNiSteel
val FitzNiMagicGlow     = FitzNiSteel
val FitzNiRose          = FitzNiSteel
val FitzNiBabyPink      = FitzNiSteel
val FitzNiBabyPinkLight = FitzNiSteelLight
val FitzNiGoldLight     = FitzNiTanLight

private val FitzNiColorScheme = darkColorScheme(
    primary             = FitzNiTan,
    onPrimary           = FitzNiNavy,
    primaryContainer    = Color(0xFF3D4F72),
    onPrimaryContainer  = FitzNiTanLight,
    secondary           = FitzNiSteel,
    onSecondary         = FitzNiNavy,
    secondaryContainer  = Color(0xFF2E4468),
    onSecondaryContainer = FitzNiSteelLight,
    tertiary            = FitzNiSteel,
    onTertiary          = FitzNiNavy,
    tertiaryContainer   = Color(0xFF354E70),
    onTertiaryContainer = FitzNiSteelLight,
    background          = FitzNiNavy,
    onBackground        = FitzNiSteelLight,
    surface             = FitzNiSurface,
    onSurface           = FitzNiSteelLight,
    surfaceVariant      = FitzNiSurfaceVar,
    onSurfaceVariant    = FitzNiSteel,
    outline             = Color(0xFF5A7290),
    outlineVariant      = Color(0xFF3A5070),
    error               = Color(0xFFE07A7A),
    onError             = FitzNiNavy,
    errorContainer      = Color(0xFF5C2A2A),
    onErrorContainer    = Color(0xFFFFB3B3),
    scrim               = Color(0x99000000),
    inverseSurface      = FitzNiSteelLight,
    inverseOnSurface    = FitzNiNavy,
    inversePrimary      = Color(0xFF5A7290),
)

@Composable
fun FitzNiPinsTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = FitzNiColorScheme,
        typography  = FitzNiTypography,
        content     = content,
    )
}
