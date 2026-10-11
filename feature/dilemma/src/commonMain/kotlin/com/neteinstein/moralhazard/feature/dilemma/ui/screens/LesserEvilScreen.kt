package com.neteinstein.moralhazard.feature.dilemma.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neteinstein.moralhazard.core.designsystem.ui.components.ChoiceCard
import com.neteinstein.moralhazard.core.designsystem.ui.components.EmberBackground
import com.neteinstein.moralhazard.core.designsystem.ui.components.HazardMeter
import com.neteinstein.moralhazard.core.designsystem.ui.components.MhTopBar
import com.neteinstein.moralhazard.core.designsystem.ui.components.ResultPanel
import com.neteinstein.moralhazard.core.designsystem.ui.theme.KickerTextStyle
import com.neteinstein.moralhazard.core.designsystem.ui.theme.MoralHazardTheme
import com.neteinstein.moralhazard.feature.dilemma.data.InMemoryDilemmaRepository
import com.neteinstein.moralhazard.feature.dilemma.domain.model.Dilemma
import com.neteinstein.moralhazard.feature.dilemma.domain.model.Side
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LesserEvilScreen(onBack: () -> Unit, viewModel: LesserEvilViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LesserEvilContent(
        state = state,
        onBack = onBack,
        onChoose = viewModel::choose,
        onNext = viewModel::next,
        onRestart = viewModel::restart,
    )
}

@Composable
fun LesserEvilContent(
    state: LesserEvilUiState,
    onBack: () -> Unit,
    onChoose: (Side) -> Unit,
    onNext: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EmberBackground(modifier, emberCount = 14) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { MhTopBar(title = "The Lesser Evil", onBack = onBack) },
        ) { padding ->
            Box(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    Modifier.widthIn(max = 600.dp).fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    HazardMeter(
                        label = "Corruption",
                        valueText = "${(state.corruptionFraction * 100).toInt()}%",
                        fraction = state.corruptionFraction,
                    )
                    val verdict = state.verdict
                    if (verdict != null) {
                        ResultPanel(
                            kicker = "Your verdict",
                            title = verdict.title,
                            body = verdict.description,
                            primaryAction = "Sin again",
                            onPrimary = onRestart,
                            secondaryAction = "Back to the lobby",
                            onSecondary = onBack,
                        )
                    } else {
                        state.current?.let { dilemma ->
                            AnimatedContent(
                                targetState = dilemma,
                                transitionSpec = {
                                    (slideInHorizontally { it / 2 } + fadeIn()) togetherWith (slideOutHorizontally { -it / 2 } + fadeOut())
                                },
                                contentKey = { it.id },
                                label = "dilemma",
                            ) { shown ->
                                DilemmaCard(
                                    dilemma = shown,
                                    number = state.questionNumber,
                                    total = state.round.size,
                                    chosen = state.chosen.takeIf { shown.id == dilemma.id },
                                    onChoose = onChoose,
                                )
                            }
                        }
                        AnimatedVisibility(visible = state.chosen != null) {
                            Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                                Text(if (state.isLast) "Face judgement" else "Next dilemma")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DilemmaCard(dilemma: Dilemma, number: Int, total: Int, chosen: Side?, onChoose: (Side) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("DILEMMA $number OF $total", style = KickerTextStyle, color = MaterialTheme.colorScheme.primary)
        Text(dilemma.prompt, style = MaterialTheme.typography.headlineSmall)
        Side.entries.forEach { side ->
            val option = dilemma.option(side)
            ChoiceCard(
                text = option.text,
                onClick = { onChoose(side) },
                revealed = chosen != null,
                chosen = chosen == side,
                detail = if (chosen == side) option.aftermath else null,
            )
        }
        AnimatedVisibility(visible = chosen != null) {
            val percent by animateIntAsState(
                targetValue = chosen?.let(dilemma::percentChoosing) ?: 0,
                animationSpec = tween(durationMillis = 900),
                label = "percent",
            )
            Text(
                "$percent% of the damned agreed with you.",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun LesserEvilContentPreview() {
    val round = InMemoryDilemmaRepository().dilemmas().take(3)
    MoralHazardTheme(darkTheme = true) {
        LesserEvilContent(
            state = LesserEvilUiState(round = round, chosen = Side.A, corruption = 3, worstPossible = 3),
            onBack = {},
            onChoose = {},
            onNext = {},
            onRestart = {},
        )
    }
}
