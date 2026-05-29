package com.nikhilgreenbot.fitznipins.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiCardSolid
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiSteel

/**
 * Card with soft gold glow shadow + subtle border — reads as "magical" depth on starfield backgrounds.
 */
@Composable
fun MagicalElevatedCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 12.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardModifier = modifier.shadow(
        elevation = elevation,
        shape = shape,
        ambientColor = FitzNiGold.copy(alpha = 0.22f),
        spotColor = FitzNiGold.copy(alpha = 0.38f),
    )
    val cardColors = CardDefaults.cardColors(containerColor = FitzNiCardSolid)
    val cardElevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp,
        pressedElevation = 10.dp,
    )
    val cardBorder = BorderStroke(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                FitzNiGold.copy(alpha = 0.45f),
                FitzNiSteel.copy(alpha = 0.25f),
                FitzNiGold.copy(alpha = 0.2f),
            ),
        ),
    )

    if (onClick != null) {
        Card(
            modifier = cardModifier,
            onClick = onClick,
            shape = shape,
            colors = cardColors,
            elevation = cardElevation,
            border = cardBorder,
            content = content,
        )
    } else {
        Card(
            modifier = cardModifier,
            shape = shape,
            colors = cardColors,
            elevation = cardElevation,
            border = cardBorder,
            content = content,
        )
    }
}
