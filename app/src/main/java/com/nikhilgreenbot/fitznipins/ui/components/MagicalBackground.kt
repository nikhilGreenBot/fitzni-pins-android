package com.nikhilgreenbot.fitznipins.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nikhilgreenbot.fitznipins.R
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiMagicGlow
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiMidnight
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiTwilight
import kotlin.math.sin
import kotlin.random.Random

private data class Star(
    val x: Float,
    val y: Float,
    val radius: Float,
    val phase: Float,
    val speed: Float,
)

@Composable
fun MagicalBackground(
    modifier: Modifier = Modifier,
    showCastle: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    val stars = remember {
        List(48) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 2f + 0.8f,
                phase = Random.nextFloat() * 6.28f,
                speed = Random.nextFloat() * 0.6f + 0.4f,
            )
        }
    }
    val shimmer = rememberInfiniteTransition(label = "shimmer")
    val twinkle = shimmer.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(4_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "twinkle",
    )

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            FitzNiMidnight,
                            FitzNiTwilight,
                            Color(0xFF2A4062),
                            FitzNiMidnight,
                        ),
                    ),
                ),
        )

        Canvas(Modifier.fillMaxSize()) {
            drawAuroraGlow()
            stars.forEach { star ->
                val alpha = 0.25f + 0.55f * ((sin(twinkle.value * star.speed + star.phase) + 1f) / 2f)
                drawCircle(
                    color = FitzNiGold.copy(alpha = alpha),
                    radius = star.radius,
                    center = Offset(star.x * size.width, star.y * size.height),
                )
            }
        }

        if (showCastle) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.ic_castle_silhouette),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(180.dp)
                    .alpha(0.18f),
                colorFilter = ColorFilter.tint(FitzNiMagicGlow),
            )
        }

        content()
    }
}

private fun DrawScope.drawAuroraGlow() {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0x338B9EB7),
                Color.Transparent,
            ),
            center = Offset(size.width * 0.75f, size.height * 0.15f),
            radius = size.width * 0.55f,
        ),
        radius = size.width * 0.55f,
        center = Offset(size.width * 0.75f, size.height * 0.15f),
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0x33D7BA89),
                Color.Transparent,
            ),
            center = Offset(size.width * 0.2f, size.height * 0.35f),
            radius = size.width * 0.45f,
        ),
        radius = size.width * 0.45f,
        center = Offset(size.width * 0.2f, size.height * 0.35f),
    )
}
