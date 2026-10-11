package com.neteinstein.moralhazard.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neteinstein.moralhazard.core.designsystem.ui.theme.MoralHazardTheme
import com.neteinstein.moralhazard.ui.navigation.AppNavDisplay
import com.neteinstein.moralhazard.ui.theme.PlatformThemeEffect
import org.koin.compose.viewmodel.koinViewModel

/** Root composable shared by Android, iOS and Web. Koin must already be started via `initKoin`. */
@Composable
fun App(viewModel: AppViewModel = koinViewModel()) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val darkTheme = themeMode.isDark(systemIsDark = isSystemInDarkTheme())
    PlatformThemeEffect(themeMode = themeMode, darkTheme = darkTheme)
    MoralHazardTheme(darkTheme = darkTheme) {
        AppNavDisplay(navigator = viewModel.navigator)
    }
}
