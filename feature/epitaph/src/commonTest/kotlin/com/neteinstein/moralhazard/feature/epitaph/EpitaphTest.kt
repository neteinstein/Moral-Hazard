package com.neteinstein.moralhazard.feature.epitaph

import com.neteinstein.moralhazard.feature.epitaph.data.InMemoryEpitaphRepository
import com.neteinstein.moralhazard.feature.epitaph.domain.usecase.GenerateEpitaphUseCase
import com.neteinstein.moralhazard.feature.epitaph.ui.screens.EpitaphViewModel
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EpitaphTest {
    private val repository = InMemoryEpitaphRepository()

    @Test
    fun everyOpenerHasANameSlot() {
        repository.fragments().openers.forEach { assertTrue(GenerateEpitaphUseCase.NAME_TOKEN in it, it) }
    }

    @Test
    fun nameIsInsertedIntoHeading() {
        val epitaph = GenerateEpitaphUseCase(repository, Random(1))("Morticia")
        assertTrue("Morticia" in epitaph.heading)
        assertTrue(GenerateEpitaphUseCase.NAME_TOKEN !in epitaph.heading)
    }

    @Test
    fun namesAreSanitized() {
        assertEquals(GenerateEpitaphUseCase.DEFAULT_NAME, GenerateEpitaphUseCase.sanitizeName("   "))
        assertEquals("Vlad the Impaler", GenerateEpitaphUseCase.sanitizeName("  Vlad   the\tImpaler "))
        assertEquals(GenerateEpitaphUseCase.MAX_NAME_LENGTH, GenerateEpitaphUseCase.sanitizeName("x".repeat(80)).length)
    }

    @Test
    fun neverRepeatsPreviousBodyBackToBack() {
        val generate = GenerateEpitaphUseCase(repository, Random(5))
        var previous = generate("A")
        repeat(50) {
            val next = generate("A", previous)
            assertNotEquals(previous.body, next.body)
            previous = next
        }
    }

    @Test
    fun viewModelCarvesAndCapsName() {
        val viewModel = EpitaphViewModel(GenerateEpitaphUseCase(repository, Random(2)))
        viewModel.onNameChange("y".repeat(100))
        assertEquals(GenerateEpitaphUseCase.MAX_NAME_LENGTH, viewModel.state.value.name.length)

        viewModel.carve()
        viewModel.carve()

        assertNotNull(viewModel.state.value.epitaph)
        assertEquals(2, viewModel.state.value.carveCount)
    }
}
