package com.neteinstein.moralhazard.feature.epitaph.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.neteinstein.moralhazard.feature.epitaph.domain.model.Epitaph

private val StoneShape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50, bottomStartPercent = 4, bottomEndPercent = 4)

@Composable
internal fun Tombstone(epitaph: Epitaph?, carveCount: Int, modifier: Modifier = Modifier) {
    val stone = MaterialTheme.colorScheme.surfaceContainerHigh
    val engraving = MaterialTheme.colorScheme.onSurface
    Box(
        modifier
            .fillMaxWidth()
            .heightIn(min = 380.dp)
            .shadow(24.dp, StoneShape, ambientColor = MaterialTheme.colorScheme.tertiary, spotColor = MaterialTheme.colorScheme.tertiary)
            .background(Brush.verticalGradient(listOf(stone, MaterialTheme.colorScheme.surfaceVariant)), StoneShape)
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, StoneShape)
            .padding(horizontal = 28.dp, vertical = 56.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = carveCount to epitaph,
            transitionSpec = {
                (fadeIn(tween(600, delayMillis = 150)) + scaleIn(tween(600), initialScale = 1.08f)) togetherWith fadeOut(tween(200))
            },
            contentKey = { it.first },
            label = "epitaph",
        ) { (_, shown) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (shown == null) {
                    Text("R.I.P.", style = MaterialTheme.typography.displayMedium, color = engraving.copy(alpha = 0.5f))
                    Text(
                        "Enter a name and carve. Nothing personal.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = engraving.copy(alpha = 0.6f),
                    )
                } else {
                    Text(shown.heading, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, color = engraving)
                    Text(shown.dates.uppercase(), style = MaterialTheme.typography.labelMedium, color = engraving.copy(alpha = 0.7f))
                    Text(
                        shown.body,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = engraving.copy(alpha = 0.9f),
                    )
                    Text(
                        shown.closer,
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
        }
    }
}
