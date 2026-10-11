package com.neteinstein.moralhazard.feature.devilsdeal.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.neteinstein.moralhazard.core.designsystem.ui.theme.KickerTextStyle
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Deal
import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Decision

@Composable
internal fun ContractCard(
    deal: Deal,
    number: Int,
    total: Int,
    decision: Decision?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
    ) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("CONTRACT $number OF $total", style = KickerTextStyle, color = MaterialTheme.colorScheme.secondary)
            Text(deal.offer, style = MaterialTheme.typography.headlineMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Term(label = "Price", value = "${deal.soulCost}% of soul")
                Term(label = "Pays", value = "${deal.reward} perks")
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text("FINE PRINT", style = KickerTextStyle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            AnimatedContent(
                targetState = decision != null,
                transitionSpec = { (fadeIn() + scaleIn(initialScale = 0.92f)) togetherWith fadeOut() },
                label = "finePrint",
            ) { revealed ->
                if (revealed) {
                    Text(
                        deal.finePrint,
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Redacted()
                }
            }
        }
    }
}

@Composable
private fun Term(label: String, value: String) {
    Column {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
}

/** Blacked-out bars standing in for terms you haven't agreed to read. */
@Composable
private fun Redacted() {
    Column(
        Modifier.semantics { contentDescription = "Fine print hidden until you decide" },
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        listOf(1f, 0.86f, 0.6f).forEach { width ->
            Spacer(
                Modifier
                    .fillMaxWidth(width)
                    .height(12.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)),
            )
        }
    }
}
