package com.neteinstein.moralhazard.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode
import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceStyle

@Composable
actual fun PlatformThemeEffect(themeMode: ThemeMode, darkTheme: Boolean) {
    LaunchedEffect(themeMode) {
        // Overriding the window's style makes the status bar (and system sheets) follow the in-app choice.
        // "System" must clear the override, otherwise isSystemInDarkTheme() would report our own override.
        UIApplication.sharedApplication.keyWindow?.overrideUserInterfaceStyle = when (themeMode) {
            ThemeMode.System -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
            ThemeMode.Light -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
            ThemeMode.Dark -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
        }
    }
}
