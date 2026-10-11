package com.neteinstein.moralhazard.ui.navigation

import com.neteinstein.moralhazard.feature.home.domain.model.GameId

/** Every destination in the app. Keys for the Navigation 3 back stack. */
sealed interface Route {
    data object Home : Route

    data object Settings : Route

    data object LesserEvil : Route

    data object DevilsDeal : Route

    data object Epitaph : Route
}

fun GameId.toRoute(): Route = when (this) {
    GameId.LesserEvil -> Route.LesserEvil
    GameId.DevilsDeal -> Route.DevilsDeal
    GameId.Epitaph -> Route.Epitaph
}
