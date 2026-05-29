package com.nikhilgreenbot.fitznipins.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikhilgreenbot.fitznipins.ui.components.FitzNiBrandLogo
import com.nikhilgreenbot.fitznipins.ui.components.MagicalBackground
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiSteelLight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val isOnboardingComplete by viewModel.isOnboardingComplete.collectAsState(initial = null)

    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(700, easing = EaseOutBack))
        alpha.animateTo(1f, animationSpec = tween(500))
        delay(100)
        taglineAlpha.animateTo(1f, animationSpec = tween(500))
        delay(1_400)

        isOnboardingComplete?.let { complete ->
            if (complete) onNavigateToHome() else onNavigateToOnboarding()
        }
    }

    LaunchedEffect(isOnboardingComplete) {
        if (isOnboardingComplete != null && taglineAlpha.value >= 1f) {
            delay(600)
            if (isOnboardingComplete == true) onNavigateToHome()
            else onNavigateToOnboarding()
        }
    }

    MagicalBackground(showCastle = true) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .scale(scale.value)
                .alpha(alpha.value),
        ) {
            FitzNiBrandLogo(
                modifier = Modifier.fillMaxWidth(),
                showTagline = false,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Your Disney pin collection, magical ✨",
                style = MaterialTheme.typography.bodyLarge,
                color = FitzNiSteelLight,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value),
            )
        }
    }
}
