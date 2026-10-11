package com.neteinstein.moralhazard.feature.settings.domain.repository

import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface PreferencesRepository {
    val themeMode: StateFlow<ThemeMode>

    fun setThemeMode(mode: ThemeMode)
}
