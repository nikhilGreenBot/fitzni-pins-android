package com.nikhilgreenbot.fitznipins.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

// ─── Sealed route hierarchy ─────────────────────────────────────────────────

sealed class Route(val path: String) {
    // Splash / Onboarding (not in bottom nav)
    data object Splash     : Route("splash")
    data object Onboarding : Route("onboarding")

    // Top-level nav destinations
    data object Home       : Route("home")
    data object Collection : Route("collection")
    data object PinTastic  : Route("pintastic")
    data object Identify   : Route("identify")
    data object Profile    : Route("profile")

    // Nested
    data object PinDetail    : Route("collection/{pinId}") {
        fun withId(id: String) = "collection/$id"
    }
    data object AddEditPin   : Route("collection/edit/{pinId}") {
        fun withId(id: String?)  = "collection/edit/${id ?: "new"}"
    }
    data object PinTasticDetail : Route("pintastic/{productId}") {
        fun withId(id: String) = "pintastic/$id"
    }
    data object IdentifyResult : Route("identify/result")
}

// ─── Bottom nav items ────────────────────────────────────────────────────────

data class BottomNavItem(
    val route: Route,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val contentDescription: String,
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Route.Home,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        contentDescription = "Home tab",
    ),
    BottomNavItem(
        route = Route.Collection,
        label = "Collection",
        selectedIcon = Icons.Filled.Collections,
        unselectedIcon = Icons.Outlined.Collections,
        contentDescription = "My collection",
    ),
    BottomNavItem(
        route = Route.PinTastic,
        label = "Pin-Tastic",
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome,
        contentDescription = "Pin-Tastic drops",
    ),
    BottomNavItem(
        route = Route.Identify,
        label = "Identify",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        contentDescription = "Identify a pin",
    ),
    BottomNavItem(
        route = Route.Profile,
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        contentDescription = "Profile and settings",
    ),
)
