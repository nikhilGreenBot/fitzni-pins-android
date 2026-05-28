package com.nikhilgreenbot.fitznipins.presentation.identify

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilgreenbot.fitznipins.domain.model.FitzNiResult
import com.nikhilgreenbot.fitznipins.domain.model.IdentifyResult
import com.nikhilgreenbot.fitznipins.domain.model.UserFacingError
import com.nikhilgreenbot.fitznipins.domain.usecase.IdentifyPinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class IdentifyUiState(
    val phase: IdentifyPhase = IdentifyPhase.CAPTURE,
    val capturedUri: Uri? = null,
    val isUploading: Boolean = false,
    val result: IdentifyResult? = null,
    val error: UserFacingError? = null,
)

enum class IdentifyPhase { CAPTURE, UPLOADING, RESULTS, ERROR }

sealed interface IdentifyEvent {
    data class ImageCaptured(val uri: Uri) : IdentifyEvent
    data object RetryCapture : IdentifyEvent
    data object DismissError : IdentifyEvent
    data object IdentifySubmit : IdentifyEvent
}

sealed interface IdentifyEffect {
    data class NavigateToResult(val sessionId: String) : IdentifyEffect
    data class ShowSnackbar(val message: String) : IdentifyEffect
}

@HiltViewModel
class IdentifyViewModel @Inject constructor(
    private val identifyPin: IdentifyPinUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IdentifyUiState())
    val uiState: StateFlow<IdentifyUiState> = _uiState.asStateFlow()

    private val _effects = Channel<IdentifyEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: IdentifyEvent) {
        when (event) {
            is IdentifyEvent.ImageCaptured -> {
                _uiState.update { it.copy(capturedUri = event.uri, phase = IdentifyPhase.CAPTURE) }
            }

            IdentifyEvent.IdentifySubmit -> {
                val uri = _uiState.value.capturedUri ?: return
                viewModelScope.launch {
                    _uiState.update { it.copy(phase = IdentifyPhase.UPLOADING, isUploading = true) }
                    when (val result = identifyPin(uri)) {
                        is FitzNiResult.Success -> _uiState.update {
                            it.copy(
                                phase = IdentifyPhase.RESULTS,
                                isUploading = false,
                                result = result.data,
                            )
                        }
                        is FitzNiResult.Error -> _uiState.update {
                            it.copy(
                                phase = IdentifyPhase.ERROR,
                                isUploading = false,
                                error = result.error,
                            )
                        }
                    }
                }
            }

            IdentifyEvent.RetryCapture -> _uiState.update {
                IdentifyUiState()
            }

            IdentifyEvent.DismissError -> _uiState.update {
                it.copy(error = null, phase = IdentifyPhase.CAPTURE)
            }
        }
    }
}
