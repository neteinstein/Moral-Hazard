package com.neteinstein.moralhazard.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.neteinstein.moralhazard.feature.devilsdeal.ui.screens.DevilsDealScreen
import com.neteinstein.moralhazard.feature.dilemma.ui.screens.LesserEvilScreen
import com.neteinstein.moralhazard.feature.epitaph.ui.screens.EpitaphScreen
import com.neteinstein.moralhazard.feature.home.ui.screens.HomeScreen
import com.neteinstein.moralhazard.feature.settings.ui.screens.SettingsScreen

private const val NAV_ANIM_MS = 380

/**
 * Navigation 3 host. Each entry gets its own ViewModelStore, so a game's ViewModel is cleared when you
 * leave it and a fresh round starts next time.
 */
@Composable
fun AppNavDisplay(navigator: AppNavigator) {
    val back: () -> Unit = { navigator.back() }
    NavDisplay(
        backStack = navigator.backStack,
        onBack = back,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            (slideInHorizontally(tween(NAV_ANIM_MS)) { it } + fadeIn(tween(NAV_ANIM_MS))) togetherWith
                (slideOutHorizontally(tween(NAV_ANIM_MS)) { -it / 4 } + fadeOut(tween(NAV_ANIM_MS)))
        },
        popTransitionSpec = {
            (slideInHorizontally(tween(NAV_ANIM_MS)) { -it / 4 } + fadeIn(tween(NAV_ANIM_MS))) togetherWith
                (slideOutHorizontally(tween(NAV_ANIM_MS)) { it } + fadeOut(tween(NAV_ANIM_MS)))
        },
        predictivePopTransitionSpec = {
            fadeIn(tween(NAV_ANIM_MS)) togetherWith (scaleOut(targetScale = 0.9f) + fadeOut())
        },
        entryProvider = entryProvider {
            entry<Route.Home> {
                HomeScreen(
                    onGameSelected = { navigator.navigate(it.toRoute()) },
                    onOpenSettings = { navigator.navigate(Route.Settings) },
                )
            }
            entry<Route.Settings> { SettingsScreen(onBack = back) }
            entry<Route.LesserEvil> { LesserEvilScreen(onBack = back) }
            entry<Route.DevilsDeal> { DevilsDealScreen(onBack = back) }
            entry<Route.Epitaph> { EpitaphScreen(onBack = back) }
        },
    )
}
