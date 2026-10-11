package com.neteinstein.moralhazard.core.designsystem.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.neteinstein.moralhazard.core.designsystem.ui.theme.KickerTextStyle

/** End-of-game card that pops in with a spring. */
@Composable
fun ResultPanel(
    kicker: String,
    title: String,
    body: String,
    primaryAction: String,
    onPrimary: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryAction: String? = null,
    onSecondary: () -> Unit = {},
) {
    val scale = remember { Animatable(0.85f) }
    LaunchedEffect(title) {
        scale.snapTo(0.85f)
        scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
    }
    Card(
        modifier = modifier.fillMaxWidth().graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        },
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Column(
            Modifier.padding(28.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(kicker.uppercase(), style = KickerTextStyle, color = MaterialTheme.colorScheme.primary)
            Text(title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Text(
                body,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = onPrimary, modifier = Modifier.padding(top = 8.dp).fillMaxWidth()) {
                Text(primaryAction)
            }
            if (secondaryAction != null) {
                OutlinedButton(onClick = onSecondary, modifier = Modifier.fillMaxWidth()) {
                    Text(secondaryAction)
                }
            }
        }
    }
}
