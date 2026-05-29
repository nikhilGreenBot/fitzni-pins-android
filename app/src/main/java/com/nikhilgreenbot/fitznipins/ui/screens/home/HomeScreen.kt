package com.nikhilgreenbot.fitznipins.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikhilgreenbot.fitznipins.ui.components.FitzNiBrandLogo
import com.nikhilgreenbot.fitznipins.ui.components.MagicalBackground
import com.nikhilgreenbot.fitznipins.ui.components.MagicalElevatedCard
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiCardSolid
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiNavyElevated
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiSteelLight
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun HomeScreen(
    onNavigateToPinTastic: () -> Unit,
    onNavigateToIdentify: () -> Unit,
    onNavigateToCollection: () -> Unit,
) {
    MagicalBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            FitzNiBrandLogo(showTagline = true)

            Spacer(Modifier.height(20.dp))

            PinTasticTuesdayCard(onClick = onNavigateToPinTastic)

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = FitzNiGold,
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Collections,
                    label = "My Collection",
                    iconTint = FitzNiGold,
                    onClick = onNavigateToCollection,
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.CameraAlt,
                    label = "Identify Pin",
                    iconTint = FitzNiSteelLight,
                    onClick = onNavigateToIdentify,
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.AutoAwesome,
                    label = "Browse Drops",
                    iconTint = FitzNiGold,
                    onClick = onNavigateToPinTastic,
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Add,
                    label = "Add Pin",
                    iconTint = FitzNiSteelLight,
                    onClick = onNavigateToCollection,
                )
            }

            Spacer(Modifier.height(24.dp))

            MagicalElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        text = "Made with ❤️ for my wife, Katie Fitzsimmons",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.White,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "FitzNi Pins is not affiliated with The Walt Disney Company. " +
                            "Pin data and purchases are handled on disneystore.com.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.88f),
                        lineHeight = 22.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun PinTasticTuesdayCard(onClick: () -> Unit) {
    val daysUntilTuesday = run {
        val today = LocalDate.now()
        val nextTuesday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY))
        java.time.temporal.ChronoUnit.DAYS.between(today, nextTuesday).toInt()
    }

    MagicalElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = 14.dp,
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = FitzNiGold,
                        modifier = Modifier.size(22.dp),
                    )
                    Text(
                        text = "  Pin-Tastic Tuesday",
                        style = MaterialTheme.typography.titleMedium,
                        color = FitzNiGold,
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (daysUntilTuesday == 0) "TODAY! New pins dropping now 🎉"
                    else "In $daysUntilTuesday day${if (daysUntilTuesday == 1) "" else "s"} ✨",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                )
                Text(
                    text = "New Disney pins every Tuesday ~8AM PT",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                )
            }

            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = FitzNiGold.copy(alpha = 0.35f),
                modifier = Modifier.size(64.dp),
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    iconTint: Color,
    onClick: () -> Unit,
) {
    MagicalElevatedCard(
        modifier = modifier.height(104.dp),
        onClick = onClick,
    ) {
        QuickActionCardContent(icon = icon, label = label, iconTint = iconTint)
    }
}

@Composable
private fun ColumnScope.QuickActionCardContent(
    icon: ImageVector,
    label: String,
    iconTint: Color,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(32.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White,
        )
    }
}
