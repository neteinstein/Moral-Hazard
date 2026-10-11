package com.neteinstein.moralhazard.feature.dilemma

import com.neteinstein.moralhazard.feature.dilemma.data.InMemoryDilemmaRepository
import com.neteinstein.moralhazard.feature.dilemma.domain.model.Dilemma
import com.neteinstein.moralhazard.feature.dilemma.domain.model.DilemmaOption
import com.neteinstein.moralhazard.feature.dilemma.domain.model.Side
import com.neteinstein.moralhazard.feature.dilemma.domain.repository.DilemmaRepository
import com.neteinstein.moralhazard.feature.dilemma.domain.usecase.JudgeSoulUseCase
import com.neteinstein.moralhazard.feature.dilemma.domain.usecase.StartDilemmaRoundUseCase
import com.neteinstein.moralhazard.feature.dilemma.ui.screens.LesserEvilViewModel
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LesserEvilTest {
    private val repository = InMemoryDilemmaRepository()

    @Test
    fun contentHasUniqueIdsAndEnoughForARound() {
        val dilemmas = repository.dilemmas()
        assertEquals(dilemmas.size, dilemmas.map { it.id }.toSet().size)
        assertTrue(dilemmas.size >= StartDilemmaRoundUseCase.ROUND_SIZE)
    }

    @Test
    fun roundIsDistinctAndSized() {
        val round = StartDilemmaRoundUseCase(repository, Random(42))()
        assertEquals(StartDilemmaRoundUseCase.ROUND_SIZE, round.size)
        assertEquals(round.size, round.toSet().size)
    }

    @Test
    fun roundIsDeterministicForSeed() {
        assertEquals(
            StartDilemmaRoundUseCase(repository, Random(3))(),
            StartDilemmaRoundUseCase(repository, Random(3))(),
        )
    }

    @Test
    fun percentagesAreComplementary() {
        repository.dilemmas().forEach { dilemma ->
            assertEquals(100, dilemma.percentChoosing(Side.A) + dilemma.percentChoosing(Side.B))
        }
    }

    @Test
    fun invalidDilemmaIsRejected() {
        assertFailsWith<IllegalArgumentException> {
            Dilemma("x", "?", DilemmaOption("a", 9, ""), DilemmaOption("b", 0, ""), percentChoosingA = 50)
        }
    }

    @Test
    fun verdictScalesWithCorruption() {
        val judge = JudgeSoulUseCase()
        assertEquals("Suspiciously Saintly", judge(0, 10).title)
        assertEquals("Mostly Harmless", judge(2, 10).title)
        assertEquals("Morally Flexible", judge(4, 10).title)
        assertEquals("HR Has Been Notified", judge(6, 10).title)
        assertEquals("Management Material", judge(10, 10).title)
        assertEquals("Suspiciously Saintly", judge(0, 0).title)
    }

    @Test
    fun viewModelPlaysAFullRound() {
        val viewModel = LesserEvilViewModel(StartDilemmaRoundUseCase(fixedRepository, Random(0)), JudgeSoulUseCase())

        viewModel.next() // ignored: nothing chosen yet
        assertEquals(0, viewModel.state.value.index)

        viewModel.choose(Side.A)
        viewModel.choose(Side.B) // ignored: already chosen
        assertEquals(Side.A, viewModel.state.value.chosen)
        assertEquals(3, viewModel.state.value.corruption)

        viewModel.next()
        assertEquals(1, viewModel.state.value.index)
        assertNull(viewModel.state.value.chosen)

        viewModel.choose(Side.B)
        viewModel.next()

        val finished = viewModel.state.value
        assertEquals(3, finished.corruption)
        assertEquals(6, finished.worstPossible)
        assertEquals("HR Has Been Notified", assertNotNull(finished.verdict).title)

        viewModel.restart()
        assertNull(viewModel.state.value.verdict)
        assertEquals(0, viewModel.state.value.corruption)
    }

    private val fixedRepository = object : DilemmaRepository {
        override fun dilemmas() = listOf(
            Dilemma("one", "?", DilemmaOption("bad", 3, ""), DilemmaOption("good", 0, ""), percentChoosingA = 50),
            Dilemma("two", "?", DilemmaOption("bad", 3, ""), DilemmaOption("good", 0, ""), percentChoosingA = 50),
        )
    }
}
