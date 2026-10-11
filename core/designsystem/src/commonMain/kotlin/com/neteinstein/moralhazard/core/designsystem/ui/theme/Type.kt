package com.neteinstein.moralhazard.core.designsystem.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

private val Display = FontFamily.Serif

internal val MoralHazardTypography: Typography = Typography().let { base ->
    base.copy(
        displayLarge = base.displayLarge.copy(fontFamily = Display, fontWeight = FontWeight.Black, letterSpacing = (-0.02).em),
        displayMedium = base.displayMedium.copy(fontFamily = Display, fontWeight = FontWeight.Black, letterSpacing = (-0.02).em),
        displaySmall = base.displaySmall.copy(fontFamily = Display, fontWeight = FontWeight.Bold),
        headlineLarge = base.headlineLarge.copy(fontFamily = Display, fontWeight = FontWeight.Bold),
        headlineMedium = base.headlineMedium.copy(fontFamily = Display, fontWeight = FontWeight.Bold),
        headlineSmall = base.headlineSmall.copy(fontFamily = Display, fontWeight = FontWeight.SemiBold),
        titleLarge = base.titleLarge.copy(fontFamily = Display, fontWeight = FontWeight.SemiBold),
        labelLarge = base.labelLarge.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 0.06.em),
        labelSmall = base.labelSmall.copy(letterSpacing = 0.12.em),
    )
}

/** Small-caps style "kicker" used above headings. */
val KickerTextStyle: TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.2.em)
