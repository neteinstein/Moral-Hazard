package com.neteinstein.moralhazard.ui.theme

import androidx.compose.runtime.Composable
import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode

@Composable
actual fun PlatformThemeEffect(themeMode: ThemeMode, darkTheme: Boolean) {
    // Browsers have no system bars to tint; the canvas paints the whole viewport.
}
