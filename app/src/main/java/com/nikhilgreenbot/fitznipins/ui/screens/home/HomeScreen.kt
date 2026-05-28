package com.nikhilgreenbot.fitznipins.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiMidnight
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiRose
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun HomeScreen(
    onNavigateToPinTastic: () -> Unit,
    onNavigateToIdentify: () -> Unit,
    onNavigateToCollection: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
    ) {
        // Header
        Text(
            text = "FitzNi Pins ✨",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = FitzNiGold,
        )
        Text(
            text = "Your magical Disney pin collection",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(24.dp))

        // Pin-Tastic Tuesday countdown card
        PinTasticTuesdayCard(onClick = onNavigateToPinTastic)

        Spacer(Modifier.height(20.dp))

        // Quick actions
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Collections,
                label = "My Collection",
                tint = FitzNiGold,
                onClick = onNavigateToCollection,
            )
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.CameraAlt,
                label = "Identify Pin",
                tint = FitzNiRose,
                onClick = onNavigateToIdentify,
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.AutoAwesome,
                label = "Browse Drops",
                tint = MaterialTheme.colorScheme.tertiary,
                onClick = onNavigateToPinTastic,
            )
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Add,
                label = "Add Pin",
                tint = MaterialTheme.colorScheme.secondary,
                onClick = onNavigateToCollection,
            )
        }

        Spacer(Modifier.height(24.dp))

        // About / branding footer
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Made with ❤️ for my wife, Katie Fitzsimmons",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = FitzNiRose,
                )
                Text(
                    text = "FitzNi Pins is not affiliated with The Walt Disney Company. " +
                           "Pin data and purchases are handled on shopDisney.com.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun PinTasticTuesdayCard(onClick: () -> Unit) {
    val daysUntilTuesday = run {
        val today = LocalDate.now()
        val nextTuesday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY))
        val days = java.time.temporal.ChronoUnit.DAYS.between(today, nextTuesday).toInt()
        days
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(FitzNiMidnight, MaterialTheme.colorScheme.primaryContainer)
                )
            )
            .clickable(onClick = onClick)
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = FitzNiGold,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = "  Pin-Tastic Tuesday",
                        style = MaterialTheme.typography.labelLarge,
                        color = FitzNiGold,
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = if (daysUntilTuesday == 0) "TODAY! New pins dropping now 🎉"
                    else "In $daysUntilTuesday day${if (daysUntilTuesday == 1) "" else "s"} ✨",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "New Disney pins every Tuesday ~8AM PT",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = FitzNiGold.copy(alpha = 0.3f),
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
    tint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .height(88.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(6.dp))
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
