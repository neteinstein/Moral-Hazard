package com.neteinstein.moralhazard.ui.theme

import androidx.compose.runtime.Composable
import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode

/**
 * Keeps platform chrome (status/navigation bar icons, iOS window appearance) in sync with the in-app theme,
 * which can differ from the system theme. This is genuinely platform-specific, hence expect/actual.
 */
@Composable
expect fun PlatformThemeEffect(themeMode: ThemeMode, darkTheme: Boolean)
