package com.neteinstein.moralhazard.ui

import androidx.lifecycle.ViewModel
import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode
import com.neteinstein.moralhazard.feature.settings.domain.usecase.ObserveThemeModeUseCase
import com.neteinstein.moralhazard.ui.navigation.AppNavigator
import kotlinx.coroutines.flow.StateFlow

class AppViewModel(observeThemeMode: ObserveThemeModeUseCase) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> = observeThemeMode()
    val navigator = AppNavigator()
}
