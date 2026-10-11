package com.neteinstein.moralhazard.ui.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * Owns the back stack. Lives in [com.neteinstein.moralhazard.ui.AppViewModel] so it survives configuration
 * changes, and is plain Kotlin so it is unit-testable without a UI.
 */
class AppNavigator(start: Route = Route.Home) {
    val backStack: SnapshotStateList<Route> = mutableStateListOf(start)

    val current: Route get() = backStack.last()

    fun navigate(route: Route) {
        if (current != route) backStack.add(route)
    }

    /** @return false when already at the root (the platform should handle back, e.g. close the app). */
    fun back(): Boolean {
        if (backStack.size <= 1) return false
        backStack.removeAt(backStack.lastIndex)
        return true
    }
}
