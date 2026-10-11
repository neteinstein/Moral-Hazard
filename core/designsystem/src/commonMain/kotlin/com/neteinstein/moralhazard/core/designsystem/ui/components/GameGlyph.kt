package com.neteinstein.moralhazard.core.designsystem.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Hand-drawn glyphs for each game. Drawn in code so they scale crisply and theme with [color]. */
enum class GlyphKind { Fork, Horns, Tombstone }

@Composable
fun GameGlyph(
    kind: GlyphKind,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
) {
    Canvas(modifier.size(size)) {
        val stroke = Stroke(width = this.size.minDimension * 0.07f, cap = StrokeCap.Round)
        when (kind) {
            GlyphKind.Fork -> drawFork(color, stroke)
            GlyphKind.Horns -> drawHorns(color, stroke)
            GlyphKind.Tombstone -> drawTombstone(color, stroke)
        }
    }
}

// A railway track splitting in two: the trolley problem, abstracted.
private fun DrawScope.drawFork(color: Color, stroke: Stroke) {
    val w = size.width
    val h = size.height
    val trunk = Path().apply {
        moveTo(w * 0.5f, h * 0.92f)
        lineTo(w * 0.5f, h * 0.55f)
    }
    val left = Path().apply {
        moveTo(w * 0.5f, h * 0.55f)
        cubicTo(w * 0.5f, h * 0.35f, w * 0.2f, h * 0.35f, w * 0.18f, h * 0.1f)
    }
    val right = Path().apply {
        moveTo(w * 0.5f, h * 0.55f)
        cubicTo(w * 0.5f, h * 0.35f, w * 0.8f, h * 0.35f, w * 0.82f, h * 0.1f)
    }
    listOf(trunk, left, right).forEach { drawPath(it, color, style = stroke) }
    drawCircle(color, radius = w * 0.07f, center = Offset(w * 0.18f, h * 0.1f))
    drawCircle(color.copy(alpha = 0.45f), radius = w * 0.07f, center = Offset(w * 0.82f, h * 0.1f))
}

// A halo floating above two little horns.
private fun DrawScope.drawHorns(color: Color, stroke: Stroke) {
    val w = size.width
    val h = size.height
    drawOval(
        color = color.copy(alpha = 0.5f),
        topLeft = Offset(w * 0.22f, h * 0.06f),
        size = Size(w * 0.56f, h * 0.18f),
        style = stroke,
    )
    val leftHorn = Path().apply {
        moveTo(w * 0.28f, h * 0.9f)
        cubicTo(w * 0.1f, h * 0.7f, w * 0.12f, h * 0.45f, w * 0.26f, h * 0.32f)
        cubicTo(w * 0.28f, h * 0.5f, w * 0.36f, h * 0.66f, w * 0.44f, h * 0.76f)
        close()
    }
    val rightHorn = Path().apply {
        moveTo(w * 0.72f, h * 0.9f)
        cubicTo(w * 0.9f, h * 0.7f, w * 0.88f, h * 0.45f, w * 0.74f, h * 0.32f)
        cubicTo(w * 0.72f, h * 0.5f, w * 0.64f, h * 0.66f, w * 0.56f, h * 0.76f)
        close()
    }
    drawPath(leftHorn, color)
    drawPath(rightHorn, color)
}

// A slightly crooked headstone with a cross notch.
private fun DrawScope.drawTombstone(color: Color, stroke: Stroke) {
    val w = size.width
    val h = size.height
    val stone = Path().apply {
        moveTo(w * 0.22f, h * 0.88f)
        lineTo(w * 0.22f, h * 0.4f)
        cubicTo(w * 0.22f, h * 0.08f, w * 0.78f, h * 0.08f, w * 0.78f, h * 0.4f)
        lineTo(w * 0.78f, h * 0.88f)
        close()
    }
    drawPath(stone, color, style = stroke)
    drawLine(color, Offset(w * 0.5f, h * 0.3f), Offset(w * 0.5f, h * 0.62f), strokeWidth = stroke.width, cap = StrokeCap.Round)
    drawLine(color, Offset(w * 0.38f, h * 0.42f), Offset(w * 0.62f, h * 0.42f), strokeWidth = stroke.width, cap = StrokeCap.Round)
    drawRoundRect(
        color = color.copy(alpha = 0.35f),
        topLeft = Offset(w * 0.08f, h * 0.88f),
        size = Size(w * 0.84f, h * 0.06f),
        cornerRadius = CornerRadius(h * 0.03f),
    )
}
