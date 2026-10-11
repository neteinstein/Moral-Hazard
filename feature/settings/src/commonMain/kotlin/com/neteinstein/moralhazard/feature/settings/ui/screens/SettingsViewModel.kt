package com.neteinstein.moralhazard.feature.settings.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode
import com.neteinstein.moralhazard.feature.settings.domain.usecase.ObserveThemeModeUseCase
import com.neteinstein.moralhazard.feature.settings.domain.usecase.SetThemeModeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class SettingsUiState(val themeMode: ThemeMode = ThemeMode.System)

class SettingsViewModel(
    observeThemeMode: ObserveThemeModeUseCase,
    private val setThemeMode: SetThemeModeUseCase,
) : ViewModel() {
    val state: StateFlow<SettingsUiState> = observeThemeMode()
        .map { SettingsUiState(themeMode = it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState(observeThemeMode().value))

    fun onThemeModeSelected(mode: ThemeMode) = setThemeMode(mode)
}
