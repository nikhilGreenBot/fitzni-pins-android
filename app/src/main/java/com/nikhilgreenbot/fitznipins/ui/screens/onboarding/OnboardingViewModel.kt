package com.nikhilgreenbot.fitznipins.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilgreenbot.fitznipins.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferences: PreferencesRepository,
) : ViewModel() {
    fun completeOnboarding() {
        viewModelScope.launch { preferences.setOnboardingComplete() }
    }
}
