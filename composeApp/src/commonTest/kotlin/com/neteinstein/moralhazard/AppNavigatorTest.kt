package com.neteinstein.moralhazard

import com.neteinstein.moralhazard.feature.home.domain.model.GameId
import com.neteinstein.moralhazard.ui.navigation.AppNavigator
import com.neteinstein.moralhazard.ui.navigation.Route
import com.neteinstein.moralhazard.ui.navigation.toRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppNavigatorTest {
    @Test
    fun startsAtHomeAndCannotPopRoot() {
        val navigator = AppNavigator()
        assertEquals(Route.Home, navigator.current)
        assertFalse(navigator.back())
        assertEquals(listOf<Route>(Route.Home), navigator.backStack.toList())
    }

    @Test
    fun navigatesAndPops() {
        val navigator = AppNavigator()
        navigator.navigate(Route.Settings)
        assertEquals(Route.Settings, navigator.current)
        assertTrue(navigator.back())
        assertEquals(Route.Home, navigator.current)
    }

    @Test
    fun ignoresDuplicateTopRoute() {
        val navigator = AppNavigator()
        navigator.navigate(Route.Epitaph)
        navigator.navigate(Route.Epitaph)
        assertEquals(2, navigator.backStack.size)
    }

    @Test
    fun everyGameHasADistinctRoute() {
        val routes = GameId.entries.map { it.toRoute() }
        assertEquals(routes.size, routes.toSet().size)
        assertFalse(Route.Home in routes || Route.Settings in routes)
    }
}
