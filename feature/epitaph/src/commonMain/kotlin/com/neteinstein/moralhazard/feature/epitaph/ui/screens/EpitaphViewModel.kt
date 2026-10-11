package com.neteinstein.moralhazard.feature.epitaph.ui.screens

import androidx.lifecycle.ViewModel
import com.neteinstein.moralhazard.feature.epitaph.domain.model.Epitaph
import com.neteinstein.moralhazard.feature.epitaph.domain.usecase.GenerateEpitaphUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EpitaphUiState(
    val name: String = "",
    val epitaph: Epitaph? = null,
    /** Increments on every carve so the UI can replay its animation even for identical text. */
    val carveCount: Int = 0,
)

class EpitaphViewModel(private val generate: GenerateEpitaphUseCase) : ViewModel() {
    private val _state = MutableStateFlow(EpitaphUiState())
    val state: StateFlow<EpitaphUiState> = _state.asStateFlow()

    fun onNameChange(name: String) = _state.update { it.copy(name = name.take(GenerateEpitaphUseCase.MAX_NAME_LENGTH)) }

    fun carve() = _state.update { it.copy(epitaph = generate(it.name, it.epitaph), carveCount = it.carveCount + 1) }
}
