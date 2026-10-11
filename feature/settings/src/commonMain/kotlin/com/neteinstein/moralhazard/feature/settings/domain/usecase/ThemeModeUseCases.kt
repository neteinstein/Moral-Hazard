package com.neteinstein.moralhazard.feature.settings.domain.usecase

import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode
import com.neteinstein.moralhazard.feature.settings.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveThemeModeUseCase(private val repository: PreferencesRepository) {
    operator fun invoke(): StateFlow<ThemeMode> = repository.themeMode
}

class SetThemeModeUseCase(private val repository: PreferencesRepository) {
    operator fun invoke(mode: ThemeMode) = repository.setThemeMode(mode)
}
