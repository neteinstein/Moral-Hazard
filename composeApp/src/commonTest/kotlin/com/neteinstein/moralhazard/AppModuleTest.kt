package com.neteinstein.moralhazard

import com.neteinstein.moralhazard.di.appModule
import com.neteinstein.moralhazard.feature.devilsdeal.ui.screens.DevilsDealViewModel
import com.neteinstein.moralhazard.feature.dilemma.ui.screens.LesserEvilViewModel
import com.neteinstein.moralhazard.feature.epitaph.ui.screens.EpitaphViewModel
import com.neteinstein.moralhazard.feature.home.ui.screens.HomeViewModel
import com.neteinstein.moralhazard.feature.settings.ui.screens.SettingsViewModel
import com.neteinstein.moralhazard.ui.AppViewModel
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.dsl.koinApplication
import org.koin.dsl.module

/** Resolves every ViewModel from the real graph, so a missing registration fails CI instead of the app. */
@OptIn(ExperimentalCoroutinesApi::class)
class AppModuleTest {
    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun everyViewModelResolves() {
        val koin = koinApplication {
            allowOverride(true)
            // Platform storage isn't available in host tests; swap in an in-memory map.
            modules(appModule, module { single<Settings> { MapSettings() } })
        }.koin

        assertNotNull(koin.get<AppViewModel>())
        assertNotNull(koin.get<HomeViewModel>())
        assertNotNull(koin.get<LesserEvilViewModel>())
        assertNotNull(koin.get<DevilsDealViewModel>())
        assertNotNull(koin.get<EpitaphViewModel>())
        assertNotNull(koin.get<SettingsViewModel>())
    }
}
