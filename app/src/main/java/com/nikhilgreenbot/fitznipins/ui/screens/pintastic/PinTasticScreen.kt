package com.nikhilgreenbot.fitznipins.ui.screens.pintastic

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.nikhilgreenbot.fitznipins.domain.model.Franchise
import com.nikhilgreenbot.fitznipins.domain.model.OfficialProduct
import com.nikhilgreenbot.fitznipins.domain.model.ProductBadge
import com.nikhilgreenbot.fitznipins.presentation.pintastic.PinTasticEvent
import com.nikhilgreenbot.fitznipins.presentation.pintastic.PinTasticEffect
import com.nikhilgreenbot.fitznipins.presentation.pintastic.PinTasticViewModel
import com.nikhilgreenbot.fitznipins.ui.components.ErrorBanner
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiRose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinTasticScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: PinTasticViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val pullRefreshState = rememberPullToRefreshState()

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            viewModel.onEvent(PinTasticEvent.Refresh)
        }
    }
    if (!uiState.isRefreshing) {
        LaunchedEffect(uiState.isRefreshing) {
            pullRefreshState.endRefresh()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is PinTasticEffect.OpenUrl -> {
                    CustomTabsIntent.Builder().build()
                        .launchUrl(context, Uri.parse(effect.url))
                }
                is PinTasticEffect.NavigateToDetail -> onNavigateToDetail(effect.productId)
                is PinTasticEffect.ShowSnackbar    -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = FitzNiGold,
                            modifier = Modifier.size(22.dp),
                        )
                        Text(
                            "  Pin-Tastic",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            Column(Modifier.fillMaxSize()) {
                uiState.error?.let { error ->
                    ErrorBanner(
                        message = error.userMessage(),
                        onDismiss = { viewModel.onEvent(PinTasticEvent.DismissError) },
                    )
                }

                // Franchise filter chips
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(Franchise.entries) { franchise ->
                        FilterChip(
                            selected = uiState.selectedFranchise == franchise,
                            onClick  = { viewModel.onEvent(PinTasticEvent.FranchiseSelected(franchise)) },
                            label    = { Text(franchise.displayName) },
                        )
                    }
                }

                // Disclaimer
                Text(
                    text = "Not affiliated with The Walt Disney Company. Products on shopDisney.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                if (uiState.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = FitzNiGold)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(uiState.products, key = { it.id }) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onNavigateToDetail(product.id) },
                                onWishlistToggle = {
                                    viewModel.onEvent(PinTasticEvent.ToggleWishlist(product.id))
                                },
                            )
                        }
                    }
                }
            }

            // Pull-to-refresh indicator (Material3 1.2 compatible API)
            PullToRefreshContainer(
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: OfficialProduct,
    onClick: () -> Unit,
    onWishlistToggle: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
    ) {
        Column {
            Box {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop,
                )
                IconButton(
                    onClick = onWishlistToggle,
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    Icon(
                        imageVector = if (product.isInWishlist) Icons.Filled.Favorite
                                      else Icons.Filled.FavoriteBorder,
                        contentDescription = "Toggle wishlist",
                        tint = FitzNiRose,
                    )
                }
                if (product.badges.contains(ProductBadge.SOLD_OUT)) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "Sold Out",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }

            Column(Modifier.padding(10.dp)) {
                Text(
                    product.title,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    product.price,
                    style = MaterialTheme.typography.bodySmall,
                    color = FitzNiGold,
                    fontWeight = FontWeight.Bold,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    product.badges.filter { it != ProductBadge.SOLD_OUT }.take(2).forEach { badge ->
                        Badge(containerColor = badgeColor(badge)) {
                            Text(badge.label, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun badgeColor(badge: ProductBadge) = when (badge) {
    ProductBadge.NEW                -> MaterialTheme.colorScheme.primary
    ProductBadge.LIMITED_RELEASE    -> FitzNiRose
    ProductBadge.PIN_TASTIC_TUESDAY -> FitzNiGold
    ProductBadge.PRE_ORDER          -> MaterialTheme.colorScheme.secondary
    ProductBadge.SOLD_OUT           -> MaterialTheme.colorScheme.error
}
