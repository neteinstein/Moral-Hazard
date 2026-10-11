package com.neteinstein.moralhazard.feature.devilsdeal.data

import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Deal
import com.neteinstein.moralhazard.feature.devilsdeal.domain.repository.DealRepository
import com.neteinstein.moralhazard.feature.devilsdeal.domain.repository.HighScoreRepository
import com.russhwolf.settings.Settings

/** Original copy, authored in-repo (see AGENTS.md "Content"). */
class InMemoryDealRepository : DealRepository {
    override fun deals(): List<Deal> = DEALS

    private companion object {
        val DEALS = listOf(
            Deal("hair", "Perfect hair. Forever.", "Including in the coffin. Especially in the coffin.", soulCost = 10, reward = 30),
            Deal("queues", "Never wait in a queue again.", "Everyone ahead of you simply stops existing. Don't look back.", soulCost = 25, reward = 60),
            Deal("arguments", "Win every argument.", "Nobody ever speaks to you again, which technically counts as winning.", soulCost = 15, reward = 40),
            Deal("followers", "A million devoted followers.", "They follow you. Physically. Everywhere. Especially at night.", soulCost = 20, reward = 55),
            Deal("wifi", "Flawless Wi-Fi, anywhere.", "Including Hell. The password changes hourly and is in Latin.", soulCost = 5, reward = 15),
            Deal("enemies", "The downfall of all your enemies.", "Delivered gradually over forty years. No refunds on schadenfreude.", soulCost = 30, reward = 70),
            Deal("animals", "The ability to talk to animals.", "They have opinions about you. All of them are bad. The cat started it.", soulCost = 10, reward = 20),
            Deal("tired", "Never feel tired again.", "You also never sleep again. You just lie there. Eight hours. Every night. Thinking.", soulCost = 25, reward = 45),
            Deal("dream-job", "Your dream job.", "As dreamt by your manager. You're the intern. Eternally.", soulCost = 15, reward = 25),
            Deal("youth", "Eternal youth.", "You'll be carded at your own funeral. And at your grandchildren's.", soulCost = 35, reward = 90),
            Deal("coffee", "A lifetime supply of coffee.", "Your lifetime is now measured in coffees. Choose your cup size wisely.", soulCost = 5, reward = 10),
            Deal("parking", "A perfect parking spot, every time.", "Always right outside a cemetery. They're saving you a space.", soulCost = 10, reward = 25),
            Deal("remembered", "To be remembered forever.", "As a cautionary tale. With diagrams. Taught in schools.", soulCost = 20, reward = 50),
            Deal("money", "Infinite money.", "In a currency discontinued in 1873. The exchange desk is downstairs.", soulCost = 30, reward = 35),
            Deal("plants", "Your houseplants will never die.", "Something has to. Every time a leaf should wilt, you lose a little. You'll barely notice.", soulCost = 15, reward = 20),
            Deal("secrets", "Know everyone's secrets.", "Including what your parents really think of you. Starting now. Oh dear.", soulCost = 25, reward = 65),
        )
    }
}

/** Best score in platform key-value storage. */
class SettingsHighScoreRepository(private val settings: Settings) : HighScoreRepository {
    override fun bestScore(): Int = settings.getInt(KEY_BEST_SCORE, 0)

    override fun saveBestScore(score: Int) = settings.putInt(KEY_BEST_SCORE, score)

    private companion object {
        const val KEY_BEST_SCORE = "devilsdeal.best_score"
    }
}
