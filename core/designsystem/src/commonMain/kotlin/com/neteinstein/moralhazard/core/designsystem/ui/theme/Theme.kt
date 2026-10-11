package com.neteinstein.moralhazard.core.designsystem.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal val DarkColors: ColorScheme = darkColorScheme(
    primary = Palette.Ember,
    onPrimary = Palette.DeepBlood,
    primaryContainer = Palette.Blood,
    onPrimaryContainer = Palette.Ash,
    secondary = Palette.Brimstone,
    onSecondary = Palette.Abyss,
    secondaryContainer = Palette.Sulfur,
    onSecondaryContainer = Palette.Ash,
    tertiary = Palette.Ectoplasm,
    onTertiary = Palette.Abyss,
    tertiaryContainer = Palette.Crypt,
    onTertiaryContainer = Palette.Ash,
    background = Palette.Abyss,
    onBackground = Palette.Ash,
    surface = Palette.Abyss,
    onSurface = Palette.Ash,
    surfaceVariant = Palette.Tomb,
    onSurfaceVariant = Palette.Smoke,
    surfaceContainerLowest = Palette.Abyss,
    surfaceContainerLow = Palette.Coffin,
    surfaceContainer = Palette.Coffin,
    surfaceContainerHigh = Palette.Tomb,
    surfaceContainerHighest = Palette.Tomb,
    outline = Palette.Soot,
    outlineVariant = Palette.Tomb,
)

internal val LightColors: ColorScheme = lightColorScheme(
    primary = Palette.Blood,
    onPrimary = Palette.Parchment,
    primaryContainer = Palette.Ember,
    onPrimaryContainer = Palette.DeepBlood,
    secondary = Palette.Sulfur,
    onSecondary = Palette.Parchment,
    secondaryContainer = Palette.Brimstone,
    onSecondaryContainer = Palette.Ink,
    tertiary = Palette.Crypt,
    onTertiary = Palette.Parchment,
    tertiaryContainer = Palette.Ectoplasm,
    onTertiaryContainer = Palette.Ink,
    background = Palette.Parchment,
    onBackground = Palette.Ink,
    surface = Palette.Parchment,
    onSurface = Palette.Ink,
    surfaceVariant = Palette.Bone,
    onSurfaceVariant = Palette.Soot,
    surfaceContainerLowest = Palette.Parchment,
    surfaceContainerLow = Palette.Vellum,
    surfaceContainer = Palette.Vellum,
    surfaceContainerHigh = Palette.Bone,
    surfaceContainerHighest = Palette.Bone,
    outline = Palette.Soot,
    outlineVariant = Palette.Bone,
)

private val MoralHazardShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp),
)

/**
 * App theme. Brand colours are deliberate (no dynamic colour): the app's identity is the hellfire palette.
 * Colour changes animate so light/dark switches cross-fade instead of snapping.
 */
@Composable
fun MoralHazardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val target = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = target.animated(),
        typography = MoralHazardTypography,
        shapes = MoralHazardShapes,
        content = content,
    )
}

@Composable
private fun ColorScheme.animated(): ColorScheme {
    val spec = tween<Color>(durationMillis = 450)
    val primary by animateColorAsState(primary, spec)
    val onPrimary by animateColorAsState(onPrimary, spec)
    val primaryContainer by animateColorAsState(primaryContainer, spec)
    val secondary by animateColorAsState(secondary, spec)
    val tertiary by animateColorAsState(tertiary, spec)
    val background by animateColorAsState(background, spec)
    val onBackground by animateColorAsState(onBackground, spec)
    val surface by animateColorAsState(surface, spec)
    val onSurface by animateColorAsState(onSurface, spec)
    val surfaceVariant by animateColorAsState(surfaceVariant, spec)
    val onSurfaceVariant by animateColorAsState(onSurfaceVariant, spec)
    val surfaceContainer by animateColorAsState(surfaceContainer, spec)
    val surfaceContainerHigh by animateColorAsState(surfaceContainerHigh, spec)
    return copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        secondary = secondary,
        tertiary = tertiary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
    )
}
