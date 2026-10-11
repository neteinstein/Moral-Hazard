package com.neteinstein.moralhazard.feature.settings

import com.neteinstein.moralhazard.feature.settings.data.SettingsPreferencesRepository
import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode
import com.russhwolf.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsPreferencesRepositoryTest {
    @Test
    fun defaultsToSystemWhenNothingStored() {
        val repository = SettingsPreferencesRepository(MapSettings())
        assertEquals(ThemeMode.System, repository.themeMode.value)
    }

    @Test
    fun persistsAndEmitsSelectedMode() {
        val settings = MapSettings()
        val repository = SettingsPreferencesRepository(settings)

        repository.setThemeMode(ThemeMode.Dark)

        assertEquals(ThemeMode.Dark, repository.themeMode.value)
        assertEquals(ThemeMode.Dark, SettingsPreferencesRepository(settings).themeMode.value)
    }

    @Test
    fun fallsBackToSystemOnCorruptValue() {
        val settings = MapSettings("settings.theme_mode" to "Purgatory")
        assertEquals(ThemeMode.System, SettingsPreferencesRepository(settings).themeMode.value)
    }

    @Test
    fun themeModeResolvesAgainstSystemFlag() {
        assertTrue(ThemeMode.System.isDark(systemIsDark = true))
        assertFalse(ThemeMode.System.isDark(systemIsDark = false))
        assertTrue(ThemeMode.Dark.isDark(systemIsDark = false))
        assertFalse(ThemeMode.Light.isDark(systemIsDark = true))
    }
}
