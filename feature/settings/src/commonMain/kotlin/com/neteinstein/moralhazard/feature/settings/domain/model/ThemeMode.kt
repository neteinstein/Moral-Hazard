package com.neteinstein.moralhazard.feature.settings.domain.model

enum class ThemeMode {
    System,
    Light,
    Dark,
    ;

    /** Resolves the mode against the platform's current dark-mode flag. */
    fun isDark(systemIsDark: Boolean): Boolean = when (this) {
        System -> systemIsDark
        Light -> false
        Dark -> true
    }
}
