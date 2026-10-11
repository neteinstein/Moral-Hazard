package com.neteinstein.moralhazard.feature.home.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neteinstein.moralhazard.core.designsystem.ui.components.EmberBackground
import com.neteinstein.moralhazard.core.designsystem.ui.components.MhIcons
import com.neteinstein.moralhazard.core.designsystem.ui.components.MhTopBar
import com.neteinstein.moralhazard.core.designsystem.ui.theme.KickerTextStyle
import com.neteinstein.moralhazard.core.designsystem.ui.theme.MoralHazardTheme
import com.neteinstein.moralhazard.feature.home.data.StaticGameCatalogRepository
import com.neteinstein.moralhazard.feature.home.domain.model.GameId
import com.neteinstein.moralhazard.feature.home.ui.components.GameCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onGameSelected: (GameId) -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeContent(state = state, onGameSelected = onGameSelected, onOpenSettings = onOpenSettings)
}

@Composable
fun HomeContent(
    state: HomeUiState,
    onGameSelected: (GameId) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EmberBackground(modifier) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                MhTopBar(
                    title = "",
                    actions = {
                        IconButton(onClick = onOpenSettings) { Icon(MhIcons.Settings, contentDescription = "Settings") }
                    },
                )
            },
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding() + 24.dp,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item { Header(greeting = state.greeting) }
                itemsIndexed(state.games, key = { _, game -> game.id }) { index, game ->
                    StaggeredEntrance(index) {
                        GameCard(game = game, onClick = { onGameSelected(game.id) }, modifier = Modifier.widthIn(max = 640.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(greeting: String) {
    Column(
        Modifier.fillMaxWidth().widthIn(max = 640.dp).padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("A GAME OF POOR CHOICES", style = KickerTextStyle, color = MaterialTheme.colorScheme.primary)
        Text("Moral\nHazard", style = MaterialTheme.typography.displayLarge)
        Text(greeting, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** Cards rise in one after another on first composition. */
@Composable
private fun StaggeredEntrance(index: Int, content: @Composable () -> Unit) {
    val visible = remember { MutableTransitionState(false).apply { targetState = true } }
    AnimatedVisibility(
        visibleState = visible,
        enter = fadeIn(tween(durationMillis = 500, delayMillis = 120 * index)) +
            slideInVertically(tween(durationMillis = 500, delayMillis = 120 * index)) { it / 3 },
    ) {
        content()
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    val repository = StaticGameCatalogRepository()
    MoralHazardTheme(darkTheme = true) {
        HomeContent(
            state = HomeUiState(greeting = repository.greetings().first(), games = repository.games()),
            onGameSelected = {},
            onOpenSettings = {},
        )
    }
}
