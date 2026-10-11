package com.neteinstein.moralhazard.feature.epitaph.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neteinstein.moralhazard.core.designsystem.ui.components.EmberBackground
import com.neteinstein.moralhazard.core.designsystem.ui.components.MhIcons
import com.neteinstein.moralhazard.core.designsystem.ui.components.MhTopBar
import com.neteinstein.moralhazard.core.designsystem.ui.theme.MoralHazardTheme
import com.neteinstein.moralhazard.feature.epitaph.domain.model.Epitaph
import com.neteinstein.moralhazard.feature.epitaph.ui.components.Tombstone
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EpitaphScreen(onBack: () -> Unit, viewModel: EpitaphViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EpitaphContent(state = state, onBack = onBack, onNameChange = viewModel::onNameChange, onCarve = viewModel::carve)
}

@Composable
fun EpitaphContent(
    state: EpitaphUiState,
    onBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onCarve: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EmberBackground(modifier, glow = MaterialTheme.colorScheme.tertiary, emberCount = 16) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { MhTopBar(title = "Epitaph Engraver", onBack = onBack) },
        ) { padding ->
            Box(
                Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    Modifier.widthIn(max = 520.dp).fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Tombstone(epitaph = state.epitaph, carveCount = state.carveCount)
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = onNameChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Name of the deceased-to-be") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onCarve() }),
                    )
                    Button(
                        onClick = onCarve,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                        ),
                    ) {
                        if (state.epitaph != null) {
                            Icon(MhIcons.Refresh, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        }
                        Text(if (state.epitaph == null) "Carve it in stone" else "Recarve")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EpitaphContentPreview() {
    MoralHazardTheme(darkTheme = true) {
        EpitaphContent(
            state = EpitaphUiState(
                name = "Alex",
                epitaph = Epitaph(
                    heading = "Here lies Alex",
                    body = "Tried to pet the 'friendly' goose.",
                    dates = "Born: yes. Died: also yes.",
                    closer = "No refunds.",
                ),
                carveCount = 1,
            ),
            onBack = {},
            onNameChange = {},
            onCarve = {},
        )
    }
}
