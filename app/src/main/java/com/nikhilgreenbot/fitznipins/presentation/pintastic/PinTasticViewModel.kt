package com.nikhilgreenbot.fitznipins.presentation.pintastic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilgreenbot.fitznipins.domain.model.FitzNiResult
import com.nikhilgreenbot.fitznipins.domain.model.Franchise
import com.nikhilgreenbot.fitznipins.domain.model.OfficialProduct
import com.nikhilgreenbot.fitznipins.domain.model.UserFacingError
import com.nikhilgreenbot.fitznipins.domain.usecase.ObservePinTasticProductsUseCase
import com.nikhilgreenbot.fitznipins.domain.usecase.RefreshCatalogUseCase
import com.nikhilgreenbot.fitznipins.domain.usecase.ToggleProductWishlistUseCase
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

data class PinTasticUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val products: List<OfficialProduct> = emptyList(),
    val selectedFranchise: Franchise = Franchise.ALL,
    val error: UserFacingError? = null,
    val lastSyncText: String? = null,
)

sealed interface PinTasticEvent {
    data class FranchiseSelected(val franchise: Franchise) : PinTasticEvent
    data class ToggleWishlist(val productId: String) : PinTasticEvent
    data object Refresh : PinTasticEvent
    data object DismissError : PinTasticEvent
}

sealed interface PinTasticEffect {
    data class OpenUrl(val url: String) : PinTasticEffect
    data class ShowSnackbar(val message: String) : PinTasticEffect
    data class NavigateToDetail(val productId: String) : PinTasticEffect
}

@HiltViewModel
class PinTasticViewModel @Inject constructor(
    private val observeProducts: ObservePinTasticProductsUseCase,
    private val refreshCatalog: RefreshCatalogUseCase,
    private val toggleWishlist: ToggleProductWishlistUseCase,
) : ViewModel() {

    private val _franchise   = MutableStateFlow(Franchise.ALL)
    private val _isRefreshing = MutableStateFlow(false)
    private val _error       = MutableStateFlow<UserFacingError?>(null)

    private val _effects = Channel<PinTasticEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    val uiState: StateFlow<PinTasticUiState> = combine(
        _franchise.flatMapLatest { observeProducts(it) },
        _franchise,
        _isRefreshing,
        _error,
    ) { products, franchise, refreshing, error ->
        PinTasticUiState(
            isLoading = false,
            isRefreshing = refreshing,
            products = products,
            selectedFranchise = franchise,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PinTasticUiState(isLoading = true),
    )

    init {
        // Seed/refresh catalog on first launch (stale-while-revalidate)
        viewModelScope.launch {
            refreshCatalog()
        }
    }

    fun onEvent(event: PinTasticEvent) {
        when (event) {
            is PinTasticEvent.FranchiseSelected -> _franchise.update { event.franchise }

            is PinTasticEvent.ToggleWishlist -> viewModelScope.launch {
                when (val r = toggleWishlist(event.productId)) {
                    is FitzNiResult.Error -> _error.update { r.error }
                    is FitzNiResult.Success -> Unit
                }
            }

            PinTasticEvent.Refresh -> viewModelScope.launch {
                _isRefreshing.update { true }
                when (val r = refreshCatalog()) {
                    is FitzNiResult.Error -> _error.update { r.error }
                    is FitzNiResult.Success -> Unit
                }
                _isRefreshing.update { false }
            }

            PinTasticEvent.DismissError -> _error.update { null }
        }
    }
}
