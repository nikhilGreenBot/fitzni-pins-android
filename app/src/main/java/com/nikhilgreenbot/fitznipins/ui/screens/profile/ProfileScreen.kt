package com.nikhilgreenbot.fitznipins.ui.screens.profile

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nikhilgreenbot.fitznipins.ui.components.MagicalElevatedCard
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiSteelLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = FitzNiGold,
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(16.dp))

            MagicalElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = FitzNiGold,
                        modifier = Modifier.size(48.dp),
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "FitzNi Pins",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = FitzNiGold,
                    )
                    Text(
                        "v1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.75f),
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Named after Fitzsimmons + Nikhil — a magical app for Disney pin collectors.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, null, tint = FitzNiGold, modifier = Modifier.size(16.dp))
                        Text(
                            " Made with love for my wife, Katie Fitzsimmons",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            SettingsSection(title = "About") {
                SettingsRow(icon = Icons.Filled.Info, label = "GitHub Repository", value = "github.com/nikhilGreenBot/fitzni-pins-android")
                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                SettingsRow(icon = Icons.Filled.Info, label = "Tech Stack", value = "Kotlin · Jetpack Compose · Hilt · Room")
                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                SettingsRow(icon = Icons.Filled.Info, label = "Architecture", value = "Clean Arch · UDF · StateFlow")
            }

            Spacer(Modifier.height(16.dp))

            MagicalElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "FitzNi Pins is not affiliated with The Walt Disney Company. " +
                        "Pin product data and purchases are handled on disneystore.com. " +
                        "Pin identification suggestions are for reference only.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.88f),
                    modifier = Modifier.padding(16.dp),
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Text(
        title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        color = FitzNiGold,
    )
    Spacer(Modifier.height(8.dp))
    MagicalElevatedCard(modifier = Modifier.fillMaxWidth(), content = content)
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = FitzNiGold, modifier = Modifier.size(20.dp))
        Column(Modifier.padding(start = 12.dp)) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White,
            )
            Text(
                value,
                style = MaterialTheme.typography.bodySmall,
                color = FitzNiSteelLight,
            )
        }
    }
}
