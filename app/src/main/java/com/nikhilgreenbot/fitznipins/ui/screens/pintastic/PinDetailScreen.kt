package com.nikhilgreenbot.fitznipins.ui.screens.pintastic

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.nikhilgreenbot.fitznipins.presentation.pintastic.PinTasticEvent
import com.nikhilgreenbot.fitznipins.presentation.pintastic.PinTasticViewModel
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiMidnight
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiRose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinDetailScreen(
    productId: String,
    onBack: () -> Unit,
    viewModel: PinTasticViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val product = remember(uiState.products, productId) {
        uiState.products.find { it.id == productId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.title ?: "Pin Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    product?.let { p ->
                        IconButton(onClick = { viewModel.onEvent(PinTasticEvent.ToggleWishlist(p.id)) }) {
                            Icon(
                                imageVector = if (p.isInWishlist) Icons.Filled.Favorite
                                              else Icons.Filled.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = FitzNiRose,
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (product == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = FitzNiGold)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            // ── Pinch-to-zoom image (pure Compose, no extra dependency) ──────
            ZoomableImage(
                imageUrl = product.imageUrl,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )

            Column(Modifier.padding(20.dp)) {
                Text(
                    product.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    product.price,
                    style = MaterialTheme.typography.titleLarge,
                    color = FitzNiGold,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    product.badges.forEach { badge -> Badge { Text(badge.label) } }
                }

                Spacer(Modifier.height(12.dp))

                product.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(16.dp))
                }

                Text(
                    text = "Franchise: ${product.franchise.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(Modifier.height(24.dp))

                // Primary CTA — Custom Tab
                Button(
                    onClick = {
                        CustomTabsIntent.Builder().build()
                            .launchUrl(context, Uri.parse(product.productUrl))
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitzNiGold),
                ) {
                    Icon(Icons.Filled.OpenInBrowser, contentDescription = null, tint = FitzNiMidnight)
                    Text("  View on shopDisney", color = FitzNiMidnight, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(8.dp))

                // Secondary CTA — Wishlist
                OutlinedButton(
                    onClick = { viewModel.onEvent(PinTasticEvent.ToggleWishlist(product.id)) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Icon(
                        imageVector = if (product.isInWishlist) Icons.Filled.Favorite
                                      else Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = FitzNiRose,
                    )
                    Text("  ${if (product.isInWishlist) "Remove from Wishlist" else "Add to Wishlist"}")
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "FitzNi Pins is not affiliated with The Walt Disney Company. " +
                    "Product data and purchases are handled on shopDisney.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * Pure-Compose pinch-to-zoom image.
 * Uses [rememberTransformableState] + [Modifier.transformable] — zero extra dependency.
 * Clamps zoom between 1× and 5× and resets pan when zoomed back to 1×.
 */
@Composable
private fun ZoomableImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    var scale  by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, panChange, _ ->
        scale  = (scale * zoomChange).coerceIn(1f, 5f)
        offset = if (scale <= 1f) Offset.Zero else offset + panChange
    }

    Box(modifier = modifier) {
        AsyncImage(
            model              = imageUrl,
            contentDescription = contentDescription,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX        = scale,
                    scaleY        = scale,
                    translationX  = offset.x,
                    translationY  = offset.y,
                )
                .transformable(state = state),
        )
    }
}
