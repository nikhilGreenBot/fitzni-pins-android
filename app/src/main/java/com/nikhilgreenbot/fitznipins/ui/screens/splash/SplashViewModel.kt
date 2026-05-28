package com.nikhilgreenbot.fitznipins.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.nikhilgreenbot.fitznipins.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferences: PreferencesRepository,
) : ViewModel() {
    val isOnboardingComplete: Flow<Boolean> = preferences.isOnboardingComplete
}
