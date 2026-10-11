package com.neteinstein.moralhazard.feature.settings.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neteinstein.moralhazard.core.designsystem.ui.components.EmberBackground
import com.neteinstein.moralhazard.core.designsystem.ui.components.MhTopBar
import com.neteinstein.moralhazard.core.designsystem.ui.theme.KickerTextStyle
import com.neteinstein.moralhazard.core.designsystem.ui.theme.MoralHazardTheme
import com.neteinstein.moralhazard.feature.settings.domain.model.ThemeMode
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(onBack: () -> Unit, viewModel: SettingsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SettingsContent(state = state, onBack = onBack, onThemeModeSelected = viewModel::onThemeModeSelected)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    state: SettingsUiState,
    onBack: () -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    EmberBackground(modifier, emberCount = 10) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { MhTopBar(title = "Settings", onBack = onBack) },
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(PaddingValues(horizontal = 20.dp, vertical = 12.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Section(title = "Appearance", modifier = Modifier.widthIn(max = 560.dp)) {
                    Text(
                        "Choose how dark you like it. Visually, at least.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth().padding(top = 12.dp)) {
                        ThemeMode.entries.forEachIndexed { index, mode ->
                            SegmentedButton(
                                selected = state.themeMode == mode,
                                onClick = { onThemeModeSelected(mode) },
                                shape = SegmentedButtonDefaults.itemShape(index, ThemeMode.entries.size),
                                label = { Text(mode.label) },
                            )
                        }
                    }
                }
                Section(title = "Fine print", modifier = Modifier.widthIn(max = 560.dp)) {
                    Text(
                        "Moral Hazard is a collection of dark-humour games. Every scenario is fiction. " +
                            "No souls were harmed in the making of this app (contractually, we cannot say the same for yours).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

private val ThemeMode.label: String
    get() = when (this) {
        ThemeMode.System -> "System"
        ThemeMode.Light -> "Light"
        ThemeMode.Dark -> "Dark"
    }

@Composable
private fun Section(title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.85f)),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(title.uppercase(), style = KickerTextStyle, color = MaterialTheme.colorScheme.primary)
            Column(Modifier.padding(top = 8.dp)) { content() }
        }
    }
}

@Preview
@Composable
private fun SettingsContentPreview() {
    MoralHazardTheme(darkTheme = true) {
        SettingsContent(state = SettingsUiState(ThemeMode.Dark), onBack = {}, onThemeModeSelected = {})
    }
}
