package com.neteinstein.moralhazard.feature.devilsdeal

import com.neteinstein.moralhazard.feature.devilsdeal.data.InMemoryDealRepository
import com.neteinstein.moralhazard.feature.devilsdeal.data.SettingsHighScoreRepository
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Deal
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Decision
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.SoulLedger
import com.neteinstein.moralhazard.feature.devilsdeal.domain.repository.DealRepository
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.DrawDealsUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.FinalScoreUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.RecordScoreUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.SignDealUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.ui.screens.DevilsDealViewModel
import com.russhwolf.settings.MapSettings
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DevilsDealTest {
    @Test
    fun contentIsValidAndLargeEnough() {
        val deals = InMemoryDealRepository().deals()
        assertEquals(deals.size, deals.map { it.id }.toSet().size)
        assertTrue(deals.size >= DrawDealsUseCase.DECK_SIZE)
    }

    @Test
    fun dealRejectsNonsenseTerms() {
        assertFailsWith<IllegalArgumentException> { Deal("x", "", "", soulCost = 0, reward = 1) }
        assertFailsWith<IllegalArgumentException> { Deal("x", "", "", soulCost = 10, reward = 0) }
    }

    @Test
    fun signingCostsSoulAndPaysPerksButNeverGoesNegative() {
        val sign = SignDealUseCase()
        val ledger = sign(SoulLedger(soul = 20), Deal("x", "", "", soulCost = 35, reward = 90))
        assertEquals(0, ledger.soul)
        assertEquals(90, ledger.perks)
        assertEquals(1, ledger.dealsSigned)
        assertTrue(ledger.isDamned)
    }

    @Test
    fun damnedScoresZeroOtherwisePerksPlusSoul() {
        val score = FinalScoreUseCase()
        assertEquals(0, score(SoulLedger(soul = 0, perks = 500)))
        assertEquals(130, score(SoulLedger(soul = 40, perks = 90)))
    }

    @Test
    fun bestScoreOnlyImproves() {
        val record = RecordScoreUseCase(SettingsHighScoreRepository(MapSettings()))
        assertTrue(record(120).isNewBest)
        val worse = record(80)
        assertFalse(worse.isNewBest)
        assertEquals(120, worse.best)
        assertEquals(120, record.best())
    }

    @Test
    fun deckIsDistinctAndSeeded() {
        val repository = InMemoryDealRepository()
        val deck = DrawDealsUseCase(repository, Random(9))()
        assertEquals(DrawDealsUseCase.DECK_SIZE, deck.size)
        assertEquals(deck, DrawDealsUseCase(repository, Random(9))())
    }

    @Test
    fun viewModelEndsEarlyWhenSoulRunsOut() {
        val viewModel = viewModel(
            Deal("a", "", "", soulCost = 60, reward = 10),
            Deal("b", "", "", soulCost = 60, reward = 10),
            Deal("c", "", "", soulCost = 60, reward = 10),
        )
        viewModel.sign()
        viewModel.next()
        viewModel.sign()
        assertEquals(Decision.Signed, viewModel.state.value.decision)
        viewModel.next()

        val result = assertNotNull(viewModel.state.value.result)
        assertEquals(0, result.score)
        assertTrue(viewModel.state.value.ledger.isDamned)
    }

    @Test
    fun viewModelScoresAfterLastDealAndPersistsBest() {
        val settings = MapSettings()
        val viewModel = viewModel(
            Deal("a", "", "", soulCost = 10, reward = 30),
            Deal("b", "", "", soulCost = 10, reward = 30),
            settings = settings,
        )
        viewModel.next() // ignored: no decision yet
        viewModel.sign()
        viewModel.refuse() // ignored: already decided
        viewModel.next()
        viewModel.refuse()
        viewModel.next()

        val state = viewModel.state.value
        assertEquals(120, state.result?.score)
        assertEquals(120, state.bestScore)
        assertEquals(120, SettingsHighScoreRepository(settings).bestScore())

        viewModel.restart()
        assertEquals(SoulLedger(), viewModel.state.value.ledger)
        assertEquals(120, viewModel.state.value.bestScore)
    }

    // Decks are shuffled, so fixtures use identical terms wherever order would matter.
    private fun viewModel(vararg deals: Deal, settings: MapSettings = MapSettings()): DevilsDealViewModel {
        val repository = object : DealRepository {
            override fun deals(): List<Deal> = deals.toList()
        }
        return DevilsDealViewModel(
            drawDeals = DrawDealsUseCase(repository, Random(0)),
            signDeal = SignDealUseCase(),
            finalScore = FinalScoreUseCase(),
            recordScore = RecordScoreUseCase(SettingsHighScoreRepository(settings)),
        )
    }
}
