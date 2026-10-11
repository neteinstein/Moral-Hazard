package com.neteinstein.moralhazard.feature.devilsdeal.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import com.neteinstein.moralhazard.core.designsystem.ui.components.HazardMeter
import com.neteinstein.moralhazard.core.designsystem.ui.components.MhTopBar
import com.neteinstein.moralhazard.core.designsystem.ui.components.ResultPanel
import com.neteinstein.moralhazard.core.designsystem.ui.theme.MoralHazardTheme
import com.neteinstein.moralhazard.feature.devilsdeal.data.InMemoryDealRepository
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Decision
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.ScoreRecord
import com.neteinstein.moralhazard.feature.devilsdeal.ui.components.ContractCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DevilsDealScreen(onBack: () -> Unit, viewModel: DevilsDealViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DevilsDealContent(
        state = state,
        onBack = onBack,
        onSign = viewModel::sign,
        onRefuse = viewModel::refuse,
        onNext = viewModel::next,
        onRestart = viewModel::restart,
    )
}

@Composable
fun DevilsDealContent(
    state: DevilsDealUiState,
    onBack: () -> Unit,
    onSign: () -> Unit,
    onRefuse: () -> Unit,
    onNext: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EmberBackground(modifier, glow = MaterialTheme.colorScheme.secondary, emberCount = 20) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { MhTopBar(title = "Deal With the Devil", onBack = onBack) },
        ) { padding ->
            Box(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    Modifier.widthIn(max = 600.dp).fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    Scoreboard(state)
                    val result = state.result
                    if (result != null) {
                        ResultPanel(
                            kicker = if (result.isNewBest) "New personal best" else "Account settled",
                            title = if (state.ledger.isDamned) "Soul foreclosed" else "${result.score} points",
                            body = resultBody(state, result),
                            primaryAction = "Haggle again",
                            onPrimary = onRestart,
                            secondaryAction = "Back to the lobby",
                            onSecondary = onBack,
                        )
                    } else {
                        state.current?.let { deal ->
                            AnimatedContent(
                                targetState = deal,
                                contentKey = { it.id },
                                transitionSpec = {
                                    (slideInVertically { it / 3 } + fadeIn()) togetherWith (slideOutVertically { -it / 3 } + fadeOut())
                                },
                                label = "contract",
                            ) { shown ->
                                ContractCard(
                                    deal = shown,
                                    number = state.offerNumber,
                                    total = state.deck.size,
                                    decision = state.decision.takeIf { shown.id == deal.id },
                                )
                            }
                        }
                        Actions(state = state, onSign = onSign, onRefuse = onRefuse, onNext = onNext)
                    }
                }
            }
        }
    }
}

@Composable
private fun Scoreboard(state: DevilsDealUiState) {
    val perks by animateIntAsState(state.ledger.perks, tween(durationMillis = 700), label = "perks")
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HazardMeter(
            label = "Soul remaining",
            valueText = "${state.ledger.soul}%",
            fraction = state.soulFraction,
            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary),
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Perks hoarded: $perks", style = MaterialTheme.typography.labelLarge)
            Text(
                "Best: ${state.bestScore}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Actions(state: DevilsDealUiState, onSign: () -> Unit, onRefuse: () -> Unit, onNext: () -> Unit) {
    if (state.decision == null) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onRefuse, modifier = Modifier.weight(1f)) { Text("Refuse") }
            Button(
                onClick = onSign,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) { Text("Sign in blood") }
        }
    } else {
        val label = when {
            state.ledger.isDamned -> "Meet your new landlord"
            state.isLast -> "Settle the account"
            else -> "Next offer"
        }
        Text(
            if (state.decision == Decision.Signed) "Signed. Pleasure doing business." else "Refused. The devil makes a note.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) { Text(label) }
    }
}

private fun resultBody(state: DevilsDealUiState, result: ScoreRecord): String = when {
    state.ledger.isDamned ->
        "You signed ${state.ledger.dealsSigned} contracts and ran out of soul. The house keeps everything. It always does."

    state.ledger.dealsSigned == 0 ->
        "You refused every offer. Your soul is pristine and your life is unchanged. The devil calls you 'a tough room'."

    else ->
        "${state.ledger.perks} perks plus ${state.ledger.soul}% soul retained. Best so far: ${result.best}."
}

@Preview
@Composable
private fun DevilsDealContentPreview() {
    MoralHazardTheme(darkTheme = true) {
        DevilsDealContent(
            state = DevilsDealUiState(deck = InMemoryDealRepository().deals().take(8)),
            onBack = {},
            onSign = {},
            onRefuse = {},
            onNext = {},
            onRestart = {},
        )
    }
}
