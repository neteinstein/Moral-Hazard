package com.neteinstein.moralhazard.feature.settings.data

import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode
import com.neteinstein.moralhazard.feature.settings.domain.repository.PreferencesRepository
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Persists preferences in platform key-value storage (SharedPreferences / NSUserDefaults / localStorage). */
class SettingsPreferencesRepository(private val settings: Settings) : PreferencesRepository {
    private val _themeMode = MutableStateFlow(readThemeMode())
    override val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    override fun setThemeMode(mode: ThemeMode) {
        settings.putString(KEY_THEME_MODE, mode.name)
        _themeMode.value = mode
    }

    private fun readThemeMode(): ThemeMode {
        val stored = settings.getStringOrNull(KEY_THEME_MODE) ?: return ThemeMode.System
        return ThemeMode.entries.firstOrNull { it.name == stored } ?: ThemeMode.System
    }

    private companion object {
        const val KEY_THEME_MODE = "settings.theme_mode"
    }
}
