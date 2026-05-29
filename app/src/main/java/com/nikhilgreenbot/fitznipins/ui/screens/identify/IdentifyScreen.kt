package com.nikhilgreenbot.fitznipins.ui.screens.identify

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nikhilgreenbot.fitznipins.domain.model.PinMatch
import com.nikhilgreenbot.fitznipins.presentation.identify.IdentifyEvent
import com.nikhilgreenbot.fitznipins.presentation.identify.IdentifyPhase
import com.nikhilgreenbot.fitznipins.presentation.identify.IdentifyViewModel
import com.nikhilgreenbot.fitznipins.ui.components.MagicalElevatedCard
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiRose
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentifyScreen(
    viewModel: IdentifyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Identify a Pin",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                }
            )
        }
    ) { padding ->
        AnimatedContent(
            targetState = uiState.phase,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(200))
            },
            label = "identify_phase",
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) { phase ->
            when (phase) {
                IdentifyPhase.CAPTURE -> CapturePhase(
                    onImageCaptured = { uri ->
                        viewModel.onEvent(IdentifyEvent.ImageCaptured(uri))
                        viewModel.onEvent(IdentifyEvent.IdentifySubmit)
                    }
                )
                IdentifyPhase.UPLOADING -> UploadingPhase()
                IdentifyPhase.RESULTS -> ResultsPhase(
                    result = uiState.result,
                    onRetry = { viewModel.onEvent(IdentifyEvent.RetryCapture) },
                )
                IdentifyPhase.ERROR -> ErrorPhase(
                    message = uiState.error?.userMessage() ?: "Something went wrong",
                    onRetry = { viewModel.onEvent(IdentifyEvent.DismissError) },
                )
            }
        }
    }
}

@Composable
private fun CapturePhase(onImageCaptured: (android.net.Uri) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Filled.CameraAlt,
            contentDescription = null,
            tint = FitzNiGold,
            modifier = Modifier.size(80.dp),
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "Point your camera at a Disney pin",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "We'll match it against our catalog and show you the top matches with confidence scores.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(32.dp))

        // In a full implementation, this launches CameraX preview
        // For this scaffold, tapping simulates a capture with a stub URI
        Button(
            onClick = {
                onImageCaptured(android.net.Uri.parse("https://placeholder.pin/mock"))
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FitzNiGold),
        ) {
            Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = MaterialTheme.colorScheme.background)
            Text("  Take Photo", color = MaterialTheme.colorScheme.background, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))
        Text(
            "⚠️ Suggestions only — always verify before trading.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun UploadingPhase() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = FitzNiGold, modifier = Modifier.size(64.dp), strokeWidth = 4.dp)
        Spacer(Modifier.height(24.dp))
        Text("Identifying your pin…", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(0.6f),
            color = FitzNiGold,
        )
    }
}

@Composable
private fun ResultsPhase(
    result: com.nikhilgreenbot.fitznipins.domain.model.IdentifyResult?,
    onRetry: () -> Unit,
) {
    val candidates = result?.candidates ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Spacer(Modifier.height(16.dp))
            Text(
                "Top Matches",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                "Results sorted by confidence — verify before trading.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
        }

        items(candidates) { match ->
            MatchCard(match = match)
        }

        item {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
                Text("  Try Another Pin")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MatchCard(match: PinMatch) {
    val confidencePct = (match.confidence * 100).roundToInt()
    val confidenceColor = when {
        confidencePct >= 80 -> MaterialTheme.colorScheme.primary
        confidencePct >= 60 -> FitzNiGold
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    MagicalElevatedCard(shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    match.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = androidx.compose.ui.graphics.Color.White,
                )
                Text(
                    "Source: ${match.source.name.replace('_', ' ')}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$confidencePct%",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = confidenceColor,
                )
                LinearProgressIndicator(
                    progress = { match.confidence },
                    modifier = Modifier.size(64.dp, 4.dp),
                    color = confidenceColor,
                )
            }
        }
    }
}

@Composable
private fun ErrorPhase(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Filled.CameraAlt, null, tint = FitzNiRose, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(16.dp))
        Text(message, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = FitzNiGold)) {
            Text("Try Again", color = MaterialTheme.colorScheme.background)
        }
    }
}
