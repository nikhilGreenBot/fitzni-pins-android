package com.nikhilgreenbot.fitznipins.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilgreenbot.fitznipins.domain.model.FitzNiResult
import com.nikhilgreenbot.fitznipins.domain.model.UserFacingError
import com.nikhilgreenbot.fitznipins.domain.model.UserPin
import com.nikhilgreenbot.fitznipins.domain.usecase.DeletePinUseCase
import com.nikhilgreenbot.fitznipins.domain.usecase.ObserveCollectionUseCase
import com.nikhilgreenbot.fitznipins.domain.usecase.SearchCollectionUseCase
import com.nikhilgreenbot.fitznipins.domain.usecase.UpsertPinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── UiState ───────────────────────────────

data class CollectionUiState(
    val isLoading: Boolean = false,
    val pins: List<UserPin> = emptyList(),
    val searchQuery: String = "",
    val error: UserFacingError? = null,
    val isRefreshing: Boolean = false,
)

// ─── Events (user intents) ──────────────────

sealed interface CollectionEvent {
    data class SearchQueryChanged(val query: String) : CollectionEvent
    data class DeletePin(val pinId: String) : CollectionEvent
    data class SavePin(val pin: UserPin) : CollectionEvent
    data object DismissError : CollectionEvent
    data object Refresh : CollectionEvent
}

// ─── One-shot effects ───────────────────────

sealed interface CollectionEffect {
    data class ShowSnackbar(val message: String) : CollectionEffect
    data class NavigateToPinDetail(val pinId: String) : CollectionEffect
}

// ─── ViewModel ─────────────────────────────

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val observeCollection: ObserveCollectionUseCase,
    private val searchCollection: SearchCollectionUseCase,
    private val deletePin: DeletePinUseCase,
    private val upsertPin: UpsertPinUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _error = MutableStateFlow<UserFacingError?>(null)
    private val _isLoading = MutableStateFlow(false)

    private val _effects = Channel<CollectionEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    val uiState: StateFlow<CollectionUiState> = combine(
        _searchQuery.flatMapLatest { query ->
            if (query.isBlank()) observeCollection() else searchCollection(query)
        },
        _searchQuery,
        _error,
        _isLoading,
    ) { pins, query, error, loading ->
        CollectionUiState(
            isLoading = loading,
            pins = pins,
            searchQuery = query,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CollectionUiState(isLoading = true),
    )

    fun onEvent(event: CollectionEvent) {
        when (event) {
            is CollectionEvent.SearchQueryChanged -> _searchQuery.update { event.query }

            is CollectionEvent.DeletePin -> viewModelScope.launch {
                when (val result = deletePin(event.pinId)) {
                    is FitzNiResult.Success ->
                        _effects.send(CollectionEffect.ShowSnackbar("Pin removed"))
                    is FitzNiResult.Error ->
                        _error.update { result.error }
                }
            }

            is CollectionEvent.SavePin -> viewModelScope.launch {
                when (val result = upsertPin(event.pin)) {
                    is FitzNiResult.Success ->
                        _effects.send(CollectionEffect.ShowSnackbar("Pin saved ✨"))
                    is FitzNiResult.Error ->
                        _error.update { result.error }
                }
            }

            CollectionEvent.DismissError -> _error.update { null }
            CollectionEvent.Refresh      -> { /* re-emits automatically via Flow */ }
        }
    }
}
