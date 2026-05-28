package com.nikhilgreenbot.fitznipins.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiMidnight
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiRose
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    val iconTint: androidx.compose.ui.graphics.Color,
    val title: String,
    val subtitle: String,
)

private val pages = listOf(
    OnboardingPage(
        icon = Icons.Filled.Collections,
        iconTint = FitzNiGold,
        title = "Your Pin Collection",
        subtitle = "Catalog every Disney trading pin you own — photos, notes, acquisition dates, and trade status all in one magical place.",
    ),
    OnboardingPage(
        icon = Icons.Filled.AutoAwesome,
        iconTint = FitzNiRose,
        title = "Pin-Tastic Drops",
        subtitle = "Never miss a Pin-Tastic Tuesday again. Browse official Disney Store releases and save pins to your wishlist instantly.",
    ),
    OnboardingPage(
        icon = Icons.Filled.CameraAlt,
        iconTint = FitzNiGold,
        title = "Identify Any Pin",
        subtitle = "Snap a photo and FitzNi Pins will match it against our catalog with confidence scores. Finding mystery pins just got magical.\n\n⚠️ Suggestions only — always verify before trading.",
    ),
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(FitzNiMidnight, FitzNiMidnight))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Skip
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (currentPage < pages.lastIndex) {
                    TextButton(onClick = {
                        scope.launch {
                            viewModel.completeOnboarding()
                            onComplete()
                        }
                    }) {
                        Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Page content with animation
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    (slideInHorizontally(tween(300)) { it / 3 } + fadeIn(tween(300))) togetherWith
                    (slideOutHorizontally(tween(300)) { -it / 3 } + fadeOut(tween(200)))
                },
                label = "onboarding_page",
            ) { page ->
                val data = pages[page]
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = data.icon,
                            contentDescription = null,
                            tint = data.iconTint,
                            modifier = Modifier.size(60.dp),
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = FitzNiGold,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = data.subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Page dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 32.dp),
            ) {
                pages.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentPage) 24.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == currentPage) FitzNiGold
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                    )
                }
            }

            // CTA button
            Button(
                onClick = {
                    if (currentPage < pages.lastIndex) {
                        currentPage++
                    } else {
                        scope.launch {
                            viewModel.completeOnboarding()
                            onComplete()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitzNiGold),
            ) {
                Text(
                    text = if (currentPage < pages.lastIndex) "Next" else "Let's Go! ✨",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = FitzNiMidnight,
                )
            }
        }
    }
}
