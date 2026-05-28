package com.nikhilgreenbot.fitznipins.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiMidnight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val isOnboardingComplete by viewModel.isOnboardingComplete.collectAsState(initial = null)

    // Animate the logo in
    val scale  = remember { Animatable(0.4f) }
    val alpha  = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Icon pop-in
        scale.animateTo(1f, animationSpec = tween(600, easing = EaseOutBack))
        alpha.animateTo(1f, animationSpec = tween(400))
        // Title fade in
        delay(100)
        titleAlpha.animateTo(1f, animationSpec = tween(500))
        // Wait for init
        delay(1_200)

        isOnboardingComplete?.let { complete ->
            if (complete) onNavigateToHome() else onNavigateToOnboarding()
        }
    }

    // Respond once init resolves during animation
    LaunchedEffect(isOnboardingComplete) {
        if (isOnboardingComplete != null && titleAlpha.value >= 1f) {
            delay(600)
            if (isOnboardingComplete == true) onNavigateToHome()
            else onNavigateToOnboarding()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(FitzNiMidnight, FitzNiMidnight.copy(alpha = 0.85f))
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Magic pin icon
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = "FitzNi Pins logo",
                tint = FitzNiGold,
                modifier = Modifier
                    .size(96.dp)
                    .scale(scale.value)
                    .alpha(alpha.value),
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "FitzNi Pins",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = FitzNiGold,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha.value),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Your Disney pin collection, magical ✨",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha.value),
            )
        }
    }
}
