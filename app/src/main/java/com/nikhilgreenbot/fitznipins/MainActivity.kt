package com.nikhilgreenbot.fitznipins

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import com.nikhilgreenbot.fitznipins.ui.navigation.FitzNiNavGraph
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiPinsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Keep system splash on screen while DataStore initializes
        // In full implementation: set keepOn based on a viewmodel flag
        // splashScreen.setKeepOnScreenCondition { !viewModel.isReady }

        setContent {
            FitzNiPinsTheme {
                FitzNiNavGraph()
            }
        }
    }
}
