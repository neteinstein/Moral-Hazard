package com.neteinstein.moralhazard.feature.home

import com.neteinstein.moralhazard.feature.home.data.StaticGameCatalogRepository
import com.neteinstein.moralhazard.feature.home.domain.model.GameId
import com.neteinstein.moralhazard.feature.home.domain.model.GameInfo
import com.neteinstein.moralhazard.feature.home.domain.repository.GameCatalogRepository
import com.neteinstein.moralhazard.feature.home.domain.usecase.GetHomeFeedUseCase
import com.neteinstein.moralhazard.feature.home.ui.screens.HomeViewModel
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetHomeFeedUseCaseTest {
    @Test
    fun catalogListsEveryGameExactlyOnce() {
        val ids = StaticGameCatalogRepository().games().map { it.id }
        assertEquals(GameId.entries.toSet(), ids.toSet())
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun greetingComesFromRepository() {
        val repository = StaticGameCatalogRepository()
        val feed = GetHomeFeedUseCase(repository, Random(1))()
        assertTrue(feed.greeting in repository.greetings())
        assertEquals(repository.games(), feed.games)
    }

    @Test
    fun emptyGreetingsYieldBlankGreeting() {
        val empty = object : GameCatalogRepository {
            override fun games(): List<GameInfo> = emptyList()

            override fun greetings(): List<String> = emptyList()
        }
        assertEquals("", GetHomeFeedUseCase(empty, Random(1))().greeting)
    }

    @Test
    fun viewModelExposesFeed() {
        val viewModel = HomeViewModel(GetHomeFeedUseCase(StaticGameCatalogRepository(), Random(7)))
        assertEquals(GameId.entries.size, viewModel.state.value.games.size)
    }
}
