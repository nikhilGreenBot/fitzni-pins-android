package com.nikhilgreenbot.fitznipins.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nikhilgreenbot.fitznipins.R
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiNavy
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiSteel
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiSteelLight
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiTan
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiTanLight

/**
 * Hero brand lockup: beach + conch artwork with magical FitzNi Pins title.
 */
@Composable
fun FitzNiBrandLogo(
    modifier: Modifier = Modifier,
    showTagline: Boolean = true,
    compact: Boolean = false,
) {
    if (compact) {
        CompactBrandLogo(modifier = modifier, showTagline = showTagline)
    } else {
        HeroBrandLogo(modifier = modifier, showTagline = showTagline)
    }
}

@Composable
private fun HeroBrandLogo(
    modifier: Modifier = Modifier,
    showTagline: Boolean,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = FitzNiGold.copy(alpha = 0.25f),
                    spotColor = FitzNiGold.copy(alpha = 0.4f),
                )
                .clip(RoundedCornerShape(24.dp)),
        ) {
            Image(
                painter = painterResource(R.drawable.fitzni_logo_beach),
                contentDescription = "FitzNi Pins — conch shell on the beach",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        }

        if (showTagline) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Your magical Disney pin collection",
                style = MaterialTheme.typography.bodyLarge,
                color = FitzNiSteelLight,
            )
        }
    }
}

@Composable
private fun CompactBrandLogo(
    modifier: Modifier = Modifier,
    showTagline: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = FitzNiGold.copy(alpha = 0.2f),
                spotColor = FitzNiGold.copy(alpha = 0.35f),
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        FitzNiNavy,
                        FitzNiSteel.copy(alpha = 0.35f),
                        FitzNiTan.copy(alpha = 0.45f),
                    ),
                ),
            )
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(R.drawable.fitzni_conch_shell),
                contentDescription = "FitzNi conch shell",
                modifier = Modifier.height(100.dp),
                contentScale = ContentScale.Fit,
            )

            Spacer(Modifier.height(8.dp))

            RowWithSparklesTitle()

            if (showTagline) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Your magical Disney pin collection",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                )
            }
        }
    }
}

@Composable
private fun RowWithSparklesTitle() {
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.AutoAwesome,
            contentDescription = null,
            tint = FitzNiGold,
            modifier = Modifier.height(20.dp),
        )
        Text(
            text = "  FitzNi Pins  ",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(
                    colors = listOf(FitzNiTanLight, FitzNiGold, FitzNiTan),
                ),
            ),
        )
        Icon(
            imageVector = Icons.Filled.AutoAwesome,
            contentDescription = null,
            tint = FitzNiGold,
            modifier = Modifier.height(20.dp),
        )
    }
}
