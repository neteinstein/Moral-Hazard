package com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase

import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Deal
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.ScoreRecord
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.SoulLedger
import com.neteinstein.moralhazard.feature.devilsdeal.domain.repository.DealRepository
import com.neteinstein.moralhazard.feature.devilsdeal.domain.repository.HighScoreRepository
import kotlin.random.Random

/** Shuffles a fresh deck of distinct offers. */
class DrawDealsUseCase(
    private val repository: DealRepository,
    private val random: Random,
) {
    operator fun invoke(size: Int = DECK_SIZE): List<Deal> = repository.deals().shuffled(random).take(size)

    companion object {
        const val DECK_SIZE = 8
    }
}

/** Signing costs soul (never below zero) and pays out perks. */
class SignDealUseCase {
    operator fun invoke(ledger: SoulLedger, deal: Deal): SoulLedger = ledger.copy(
        soul = (ledger.soul - deal.soulCost).coerceAtLeast(0),
        perks = ledger.perks + deal.reward,
        dealsSigned = ledger.dealsSigned + 1,
    )
}

/**
 * Final score: perks plus whatever soul you kept. Lose your whole soul and the house keeps everything.
 */
class FinalScoreUseCase {
    operator fun invoke(ledger: SoulLedger): Int = if (ledger.isDamned) 0 else ledger.perks + ledger.soul
}

/** Persists the score if it beats the stored best. */
class RecordScoreUseCase(private val repository: HighScoreRepository) {
    operator fun invoke(score: Int): ScoreRecord {
        val previous = repository.bestScore()
        val isNewBest = score > previous
        if (isNewBest) repository.saveBestScore(score)
        return ScoreRecord(score = score, best = maxOf(score, previous), isNewBest = isNewBest)
    }

    fun best(): Int = repository.bestScore()
}
