package com.neteinstein.moralhazard.feature.devilsdeal.ui.screens

import androidx.lifecycle.ViewModel
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Deal
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Decision
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.ScoreRecord
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.SoulLedger
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.DrawDealsUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.FinalScoreUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.RecordScoreUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.SignDealUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DevilsDealUiState(
    val deck: List<Deal> = emptyList(),
    val index: Int = 0,
    val ledger: SoulLedger = SoulLedger(),
    val decision: Decision? = null,
    val result: ScoreRecord? = null,
    val bestScore: Int = 0,
) {
    val current: Deal? get() = deck.getOrNull(index)
    val isFinished: Boolean get() = result != null
    val isLast: Boolean get() = index == deck.lastIndex
    val offerNumber: Int get() = index + 1
    val soulFraction: Float get() = ledger.soul.toFloat() / SoulLedger.FULL_SOUL
}

class DevilsDealViewModel(
    private val drawDeals: DrawDealsUseCase,
    private val signDeal: SignDealUseCase,
    private val finalScore: FinalScoreUseCase,
    private val recordScore: RecordScoreUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(newGame())
    val state: StateFlow<DevilsDealUiState> = _state.asStateFlow()

    fun sign() = _state.update { state ->
        val deal = state.current
        if (deal == null || state.decision != null || state.isFinished) {
            state
        } else {
            state.copy(ledger = signDeal(state.ledger, deal), decision = Decision.Signed)
        }
    }

    fun refuse() = _state.update { state ->
        if (state.current == null || state.decision != null || state.isFinished) state else state.copy(decision = Decision.Refused)
    }

    /** Moves to the next offer, or settles the account when the deck is done or the soul is gone. */
    fun next() = _state.update { state ->
        when {
            state.decision == null || state.isFinished -> state

            state.ledger.isDamned || state.isLast -> {
                val record = recordScore(finalScore(state.ledger))
                state.copy(result = record, bestScore = record.best)
            }

            else -> state.copy(index = state.index + 1, decision = null)
        }
    }

    fun restart() {
        _state.value = newGame()
    }

    private fun newGame() = DevilsDealUiState(deck = drawDeals(), bestScore = recordScore.best())
}
