package com.neteinstein.moralhazard.feature.settings

import com.neteinstein.moralhazard.feature.settings.data.SettingsPreferencesRepository
import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode
import com.neteinstein.moralhazard.feature.settings.domain.usecase.ObserveThemeModeUseCase
import com.neteinstein.moralhazard.feature.settings.domain.usecase.SetThemeModeUseCase
import com.neteinstein.moralhazard.feature.settings.ui.screens.SettingsViewModel
import com.russhwolf.settings.MapSettings
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun selectingModeUpdatesState() {
        val repository = SettingsPreferencesRepository(MapSettings())
        val viewModel = SettingsViewModel(ObserveThemeModeUseCase(repository), SetThemeModeUseCase(repository))

        viewModel.onThemeModeSelected(ThemeMode.Light)

        assertEquals(ThemeMode.Light, viewModel.state.value.themeMode)
    }
}
