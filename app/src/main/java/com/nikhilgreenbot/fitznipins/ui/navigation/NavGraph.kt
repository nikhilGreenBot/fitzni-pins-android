package com.nikhilgreenbot.fitznipins.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nikhilgreenbot.fitznipins.ui.screens.collection.CollectionScreen
import com.nikhilgreenbot.fitznipins.ui.screens.home.HomeScreen
import com.nikhilgreenbot.fitznipins.ui.screens.identify.IdentifyScreen
import com.nikhilgreenbot.fitznipins.ui.screens.onboarding.OnboardingScreen
import com.nikhilgreenbot.fitznipins.ui.screens.pintastic.PinDetailScreen
import com.nikhilgreenbot.fitznipins.ui.screens.pintastic.PinTasticScreen
import com.nikhilgreenbot.fitznipins.ui.screens.profile.ProfileScreen
import com.nikhilgreenbot.fitznipins.ui.screens.splash.SplashScreen

private val topLevelRoutes = bottomNavItems.map { it.route.path }

@Composable
fun FitzNiNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route.Splash.path,
) {
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStack?.destination
    val showBottomBar = currentDestination?.route in topLevelRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                FitzNiBottomBar(
                    navController = navController,
                    currentDestination = currentDestination,
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController     = navController,
            startDestination  = startDestination,
            modifier          = Modifier.padding(innerPadding),
            enterTransition   = {
                fadeIn(animationSpec = tween(220)) +
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(220))
            },
            exitTransition    = { fadeOut(animationSpec = tween(180)) },
            popEnterTransition = { fadeIn(animationSpec = tween(220)) },
            popExitTransition  = {
                fadeOut(animationSpec = tween(180)) +
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(180))
            },
        ) {
            composable(Route.Splash.path) {
                SplashScreen(
                    onNavigateToOnboarding = {
                        navController.navigate(Route.Onboarding.path) {
                            popUpTo(Route.Splash.path) { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Route.Home.path) {
                            popUpTo(Route.Splash.path) { inclusive = true }
                        }
                    }
                )
            }

            composable(Route.Onboarding.path) {
                OnboardingScreen(
                    onComplete = {
                        navController.navigate(Route.Home.path) {
                            popUpTo(Route.Onboarding.path) { inclusive = true }
                        }
                    }
                )
            }

            composable(Route.Home.path) {
                HomeScreen(
                    onNavigateToPinTastic = { navController.navigate(Route.PinTastic.path) },
                    onNavigateToIdentify  = { navController.navigate(Route.Identify.path) },
                    onNavigateToCollection = { navController.navigate(Route.Collection.path) },
                )
            }

            composable(Route.Collection.path) {
                CollectionScreen(
                    onNavigateToPinDetail = { pinId ->
                        navController.navigate(Route.PinDetail.withId(pinId))
                    },
                    onAddPin = {
                        navController.navigate(Route.AddEditPin.withId(null))
                    },
                )
            }

            composable(Route.PinTastic.path) {
                PinTasticScreen(
                    onNavigateToDetail = { productId ->
                        navController.navigate(Route.PinTasticDetail.withId(productId))
                    },
                )
            }

            composable(Route.PinTasticDetail.path) { backStack ->
                val productId = backStack.arguments?.getString("productId") ?: return@composable
                PinDetailScreen(
                    productId = productId,
                    onBack = { navController.popBackStack() },
                )
            }

            composable(Route.Identify.path) {
                IdentifyScreen()
            }

            composable(Route.Profile.path) {
                ProfileScreen()
            }
        }
    }
}

@Composable
private fun FitzNiBottomBar(
    navController: NavHostController,
    currentDestination: androidx.navigation.NavDestination?,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route.path } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route.path) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.contentDescription,
                    )
                },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
            )
        }
    }
}
