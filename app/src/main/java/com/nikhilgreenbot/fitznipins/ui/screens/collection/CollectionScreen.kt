package com.nikhilgreenbot.fitznipins.ui.screens.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.nikhilgreenbot.fitznipins.domain.model.UserPin
import com.nikhilgreenbot.fitznipins.presentation.collection.CollectionEffect
import com.nikhilgreenbot.fitznipins.presentation.collection.CollectionEvent
import com.nikhilgreenbot.fitznipins.presentation.collection.CollectionViewModel
import com.nikhilgreenbot.fitznipins.ui.components.EmptyStateView
import com.nikhilgreenbot.fitznipins.ui.components.ErrorBanner
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    onNavigateToPinDetail: (String) -> Unit,
    onAddPin: () -> Unit,
    viewModel: CollectionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Consume one-shot effects
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CollectionEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                is CollectionEffect.NavigateToPinDetail -> onNavigateToPinDetail(effect.pinId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Collection",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPin,
                containerColor = FitzNiGold,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add pin", tint = MaterialTheme.colorScheme.background)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Error banner (non-blocking)
            uiState.error?.let { error ->
                ErrorBanner(
                    message = error.userMessage(),
                    onDismiss = { viewModel.onEvent(CollectionEvent.DismissError) },
                )
            }

            // Search bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onEvent(CollectionEvent.SearchQueryChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search your pins…") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )

            when {
                uiState.isLoading -> PinGridSkeleton()

                uiState.pins.isEmpty() -> EmptyStateView(
                    icon = Icons.Filled.Collections,
                    title = "No pins yet",
                    subtitle = "Tap + to add your first Disney pin",
                )

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement   = Arrangement.spacedBy(12.dp),
                ) {
                    items(uiState.pins, key = { it.id }) { pin ->
                        PinGridItem(
                            pin = pin,
                            onClick = { onNavigateToPinDetail(pin.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PinGridItem(pin: UserPin, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        pin = pin,
    )
}

@Composable
private fun Card(
    modifier: Modifier,
    shape: RoundedCornerShape,
    pin: UserPin,
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = FitzNiGold.copy(alpha = 0.18f),
                spotColor = FitzNiGold.copy(alpha = 0.3f),
            )
            .clip(shape)
            .background(com.nikhilgreenbot.fitznipins.ui.theme.FitzNiCardSolid),
    ) {
        Column {
            if (pin.photoUris.isNotEmpty()) {
                AsyncImage(
                    model = pin.photoUris.first(),
                    contentDescription = pin.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.Collections,
                        contentDescription = null,
                        tint = FitzNiGold.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp),
                    )
                }
            }
            Column(Modifier.padding(10.dp)) {
                Text(
                    pin.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = androidx.compose.ui.graphics.Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (pin.tradeStatus != null) {
                    Text(
                        "Open to trade",
                        style = MaterialTheme.typography.labelSmall,
                        color = FitzNiGold,
                    )
                }
            }
        }
    }
}

@Composable
private fun PinGridSkeleton() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement   = Arrangement.spacedBy(12.dp),
    ) {
        items(6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )
        }
    }
}
