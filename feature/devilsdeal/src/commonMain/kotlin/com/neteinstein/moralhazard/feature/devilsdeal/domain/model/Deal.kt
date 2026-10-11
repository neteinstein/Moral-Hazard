package com.neteinstein.moralhazard.feature.devilsdeal.domain.model

data class Deal(
    val id: String,
    val offer: String,
    /** Revealed only after you decide. Naturally. */
    val finePrint: String,
    val soulCost: Int,
    val reward: Int,
) {
    init {
        require(soulCost in 1..SoulLedger.FULL_SOUL) { "soulCost must be 1..${SoulLedger.FULL_SOUL}" }
        require(reward > 0) { "reward must be positive" }
    }
}

/** Running tally of a game: how much soul is left and how many perks you've hoarded. */
data class SoulLedger(
    val soul: Int = FULL_SOUL,
    val perks: Int = 0,
    val dealsSigned: Int = 0,
) {
    val isDamned: Boolean get() = soul <= 0

    companion object {
        const val FULL_SOUL = 100
    }
}

enum class Decision { Signed, Refused }

data class ScoreRecord(val score: Int, val best: Int, val isNewBest: Boolean)
