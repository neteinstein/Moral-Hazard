package com.neteinstein.moralhazard.core.designsystem.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

private data class Ember(val x: Float, val speed: Float, val phase: Float, val radius: Float, val sway: Float)

/**
 * Full-bleed animated backdrop: a slow, breathing glow from below plus drifting embers.
 * Purely decorative; content is drawn on top.
 */
@Composable
fun EmberBackground(
    modifier: Modifier = Modifier,
    glow: Color = MaterialTheme.colorScheme.primary,
    emberCount: Int = 26,
    content: @Composable BoxScope.() -> Unit,
) {
    val background = MaterialTheme.colorScheme.background
    val embers = remember(emberCount) {
        val random = Random(seed = 666)
        List(emberCount) {
            Ember(
                x = random.nextFloat(),
                speed = 0.35f + random.nextFloat() * 0.65f,
                phase = random.nextFloat(),
                radius = 1.2f + random.nextFloat() * 2.8f,
                sway = 8f + random.nextFloat() * 24f,
            )
        }
    }
    val transition = rememberInfiniteTransition(label = "embers")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 14_000, easing = LinearEasing)),
        label = "time",
    )
    val breath by transition.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.22f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 4_000), repeatMode = RepeatMode.Reverse),
        label = "breath",
    )

    Box(modifier.fillMaxSize().background(background)) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(glow.copy(alpha = breath), Color.Transparent),
                    center = Offset(size.width / 2f, size.height * 1.05f),
                    radius = size.maxDimension * 0.85f,
                ),
            )
            embers.forEach { ember ->
                val progress = (time * ember.speed + ember.phase) % 1f
                val y = size.height * (1f - progress)
                val x = size.width * ember.x + sin((progress * 2 * PI + ember.phase * 6).toFloat()) * ember.sway
                val alpha = sin((progress * PI).toFloat()) * 0.55f
                drawCircle(color = glow.copy(alpha = alpha), radius = ember.radius * density, center = Offset(x, y))
            }
        }
        content()
    }
}
