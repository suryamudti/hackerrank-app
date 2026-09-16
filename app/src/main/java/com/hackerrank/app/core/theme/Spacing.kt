package com.hackerrank.app.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Scalable spacing tokens for consistent padding, margins, and gaps across the app.
 */
@Immutable
data class AppSpacing(
    val none: Dp = 0.dp,
    val xxSmall: Dp = 2.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 20.dp,
    val xxLarge: Dp = 24.dp,
    val huge: Dp = 32.dp,
    val massive: Dp = 48.dp,
)

val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }
