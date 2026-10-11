package com.neteinstein.moralhazard.feature.dilemma.ui.screens

import androidx.lifecycle.ViewModel
import com.neteinstein.moralhazard.feature.dilemma.domain.model.Dilemma
import com.neteinstein.moralhazard.feature.dilemma.domain.model.Side
import com.neteinstein.moralhazard.feature.dilemma.domain.model.SoulVerdict
import com.neteinstein.moralhazard.feature.dilemma.domain.usecase.JudgeSoulUseCase
import com.neteinstein.moralhazard.feature.dilemma.domain.usecase.StartDilemmaRoundUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LesserEvilUiState(
    val round: List<Dilemma> = emptyList(),
    val index: Int = 0,
    val chosen: Side? = null,
    val corruption: Int = 0,
    val worstPossible: Int = 0,
    val verdict: SoulVerdict? = null,
) {
    val current: Dilemma? get() = round.getOrNull(index)
    val isFinished: Boolean get() = verdict != null
    val isLast: Boolean get() = index == round.lastIndex
    val questionNumber: Int get() = index + 1
    val corruptionFraction: Float get() = if (worstPossible == 0) 0f else corruption.toFloat() / worstPossible
}

class LesserEvilViewModel(
    private val startRound: StartDilemmaRoundUseCase,
    private val judgeSoul: JudgeSoulUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(newRound())
    val state: StateFlow<LesserEvilUiState> = _state.asStateFlow()

    fun choose(side: Side) = _state.update { state ->
        val dilemma = state.current
        if (dilemma == null || state.chosen != null) {
            state
        } else {
            state.copy(
                chosen = side,
                corruption = state.corruption + dilemma.option(side).corruption,
                worstPossible = state.worstPossible + dilemma.worstCorruption,
            )
        }
    }

    fun next() = _state.update { state ->
        when {
            state.chosen == null || state.isFinished -> state
            state.isLast -> state.copy(verdict = judgeSoul(state.corruption, state.worstPossible))
            else -> state.copy(index = state.index + 1, chosen = null)
        }
    }

    fun restart() {
        _state.value = newRound()
    }

    private fun newRound() = LesserEvilUiState(round = startRound())
}
