package com.neteinstein.moralhazard.feature.home.domain.usecase

import com.neteinstein.moralhazard.feature.home.domain.model.GameInfo
import com.neteinstein.moralhazard.feature.home.domain.repository.GameCatalogRepository
import kotlin.random.Random

data class HomeFeed(val greeting: String, val games: List<GameInfo>)

class GetHomeFeedUseCase(
    private val repository: GameCatalogRepository,
    private val random: Random,
) {
    operator fun invoke(): HomeFeed {
        val greetings = repository.greetings()
        return HomeFeed(
            greeting = if (greetings.isEmpty()) "" else greetings[random.nextInt(greetings.size)],
            games = repository.games(),
        )
    }
}
