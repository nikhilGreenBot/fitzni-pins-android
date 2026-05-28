package com.nikhilgreenbot.fitznipins.ui.screens.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nikhilgreenbot.fitznipins.domain.model.PinLocation
import com.nikhilgreenbot.fitznipins.domain.model.TradeStatus
import com.nikhilgreenbot.fitznipins.domain.model.UserPin
import com.nikhilgreenbot.fitznipins.presentation.collection.CollectionEvent
import com.nikhilgreenbot.fitznipins.presentation.collection.CollectionViewModel
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiGold
import com.nikhilgreenbot.fitznipins.ui.theme.FitzNiMidnight
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPinScreen(
    pinId: String?,
    onBack: () -> Unit,
    viewModel: CollectionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEditing = pinId != null && pinId != "new"

    // Prefill when editing
    val existingPin = remember(pinId, uiState.pins) {
        if (isEditing) uiState.pins.find { it.id == pinId } else null
    }

    var title       by remember(existingPin) { mutableStateOf(existingPin?.title ?: "") }
    var description by remember(existingPin) { mutableStateOf(existingPin?.description ?: "") }
    var tagsText    by remember(existingPin) { mutableStateOf(existingPin?.tags?.joinToString(", ") ?: "") }
    var location    by remember(existingPin) { mutableStateOf(existingPin?.location) }
    var tradeStatus by remember(existingPin) { mutableStateOf(existingPin?.tradeStatus) }
    var locationExpanded    by remember { mutableStateOf(false) }
    var tradeStatusExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Edit Pin" else "Add Pin",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (title.isBlank()) {
                                scope.launch { snackbarHostState.showSnackbar("Title can't be empty") }
                                return@IconButton
                            }
                            val tags = tagsText.split(",")
                                .map { it.trim() }
                                .filter { it.isNotBlank() }
                            val pin = UserPin(
                                id          = existingPin?.id ?: UUID.randomUUID().toString(),
                                title       = title.trim(),
                                description = description.trim().ifBlank { null },
                                location    = location,
                                tags        = tags,
                                tradeStatus = tradeStatus,
                                createdAt   = existingPin?.createdAt ?: Instant.now(),
                                updatedAt   = Instant.now(),
                            )
                            viewModel.onEvent(CollectionEvent.SavePin(pin))
                            onBack()
                        }
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Save", tint = FitzNiGold)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Pin title *") },
                placeholder = { Text("e.g. Mickey 50th Anniversary Pin") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Notes / description") },
                placeholder = { Text("Where you got it, condition, etc.") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
            )

            OutlinedTextField(
                value = tagsText,
                onValueChange = { tagsText = it },
                label = { Text("Tags (comma separated)") },
                placeholder = { Text("mickey, limited, park exclusive") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Location dropdown
            ExposedDropdownMenuBox(
                expanded = locationExpanded,
                onExpandedChange = { locationExpanded = it },
            ) {
                OutlinedTextField(
                    value = location?.name?.replace('_', ' ')?.lowercase()
                        ?.replaceFirstChar { it.uppercase() } ?: "Not set",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Where acquired") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                )
                ExposedDropdownMenu(
                    expanded = locationExpanded,
                    onDismissRequest = { locationExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Not set") },
                        onClick = { location = null; locationExpanded = false },
                    )
                    PinLocation.entries.forEach { loc ->
                        DropdownMenuItem(
                            text = { Text(loc.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }) },
                            onClick = { location = loc; locationExpanded = false },
                        )
                    }
                }
            }

            // Trade status dropdown
            ExposedDropdownMenuBox(
                expanded = tradeStatusExpanded,
                onExpandedChange = { tradeStatusExpanded = it },
            ) {
                OutlinedTextField(
                    value = when (tradeStatus) {
                        TradeStatus.OPEN_TO_TRADE  -> "Open to trade"
                        TradeStatus.NOT_FOR_TRADE  -> "Not for trade"
                        null                       -> "Not set"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Trade status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tradeStatusExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                )
                ExposedDropdownMenu(
                    expanded = tradeStatusExpanded,
                    onDismissRequest = { tradeStatusExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Not set") },
                        onClick = { tradeStatus = null; tradeStatusExpanded = false },
                    )
                    TradeStatus.entries.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(if (status == TradeStatus.OPEN_TO_TRADE) "Open to trade" else "Not for trade") },
                            onClick = { tradeStatus = status; tradeStatusExpanded = false },
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Title can't be empty") }
                        return@Button
                    }
                    val tags = tagsText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    val pin = UserPin(
                        id          = existingPin?.id ?: UUID.randomUUID().toString(),
                        title       = title.trim(),
                        description = description.trim().ifBlank { null },
                        location    = location,
                        tags        = tags,
                        tradeStatus = tradeStatus,
                        createdAt   = existingPin?.createdAt ?: Instant.now(),
                        updatedAt   = Instant.now(),
                    )
                    viewModel.onEvent(CollectionEvent.SavePin(pin))
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitzNiGold),
            ) {
                Text(
                    if (isEditing) "Save Changes" else "Add to Collection",
                    color = FitzNiMidnight,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
