package com.neteinstein.moralhazard.feature.home.ui.screens

import androidx.lifecycle.ViewModel
import com.neteinstein.moralhazard.feature.home.domain.model.GameInfo
import com.neteinstein.moralhazard.feature.home.domain.usecase.GetHomeFeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val greeting: String = "",
    val games: List<GameInfo> = emptyList(),
)

class HomeViewModel(getHomeFeed: GetHomeFeedUseCase) : ViewModel() {
    private val _state = MutableStateFlow(getHomeFeed().let { HomeUiState(greeting = it.greeting, games = it.games) })
    val state: StateFlow<HomeUiState> = _state.asStateFlow()
}
