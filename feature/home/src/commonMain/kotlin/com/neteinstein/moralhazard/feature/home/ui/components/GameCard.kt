package com.neteinstein.moralhazard.feature.home.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neteinstein.moralhazard.core.designsystem.ui.components.GameGlyph
import com.neteinstein.moralhazard.core.designsystem.ui.components.GlyphKind
import com.neteinstein.moralhazard.core.designsystem.ui.components.pressScale
import com.neteinstein.moralhazard.feature.home.domain.model.GameAccent
import com.neteinstein.moralhazard.feature.home.domain.model.GameId
import com.neteinstein.moralhazard.feature.home.domain.model.GameInfo

@Composable
internal fun GameCard(game: GameInfo, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val accent = game.accent.color()
    val interaction = remember { MutableInteractionSource() }
    Card(
        onClick = onClick,
        interactionSource = interaction,
        modifier = modifier.fillMaxWidth().pressScale(interaction),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f)),
    ) {
        Box(
            Modifier.background(
                Brush.linearGradient(listOf(accent.copy(alpha = 0.18f), Color.Transparent)),
            ),
        ) {
            Row(
                Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                Box(
                    Modifier.size(72.dp).background(accent.copy(alpha = 0.14f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    GameGlyph(kind = game.id.glyph(), color = accent, size = 44.dp)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(game.title, style = MaterialTheme.typography.titleLarge)
                    Text(game.tagline, style = MaterialTheme.typography.bodyMedium, color = accent)
                    Text(
                        game.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun GameAccent.color(): Color = when (this) {
    GameAccent.Ember -> MaterialTheme.colorScheme.primary
    GameAccent.Brimstone -> MaterialTheme.colorScheme.secondary
    GameAccent.Ectoplasm -> MaterialTheme.colorScheme.tertiary
}

private fun GameId.glyph(): GlyphKind = when (this) {
    GameId.LesserEvil -> GlyphKind.Fork
    GameId.DevilsDeal -> GlyphKind.Horns
    GameId.Epitaph -> GlyphKind.Tombstone
}
