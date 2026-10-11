package com.neteinstein.moralhazard.core.designsystem.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A big tappable option. When [revealed] the card shows [detail] (e.g. a verdict) and stops being clickable;
 * the chosen card is highlighted, the other one dims.
 */
@Composable
fun ChoiceCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    revealed: Boolean = false,
    chosen: Boolean = false,
    detail: String? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    val container by animateColorAsState(
        when {
            chosen -> MaterialTheme.colorScheme.primaryContainer
            revealed -> MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f)
            else -> MaterialTheme.colorScheme.surfaceContainerHigh
        },
        label = "choiceContainer",
    )
    val border by animateDpAsState(if (chosen) 2.dp else 1.dp, label = "choiceBorder")
    Card(
        onClick = onClick,
        enabled = !revealed,
        interactionSource = interaction,
        modifier = modifier.fillMaxWidth().pressScale(interaction),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = container,
            disabledContainerColor = container,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = BorderStroke(
            border,
            if (chosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        ),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(text, style = MaterialTheme.typography.titleMedium)
            AnimatedVisibility(
                visible = revealed && detail != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Text(
                    detail.orEmpty(),
                    modifier = Modifier.padding(top = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
