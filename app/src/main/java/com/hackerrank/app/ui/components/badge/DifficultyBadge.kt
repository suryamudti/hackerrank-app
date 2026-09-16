package com.hackerrank.app.ui.components.badge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hackerrank.app.core.localizedName
import com.hackerrank.app.core.theme.AppTheme
import com.hackerrank.app.domain.model.Difficulty

enum class DifficultyBadgeVariant {
    Subtle,
    Filled,
    Outlined,
    TextOnly,
}

enum class DifficultyBadgeSize {
    Small,
    Medium,
}

/**
 * Reusable difficulty badge displaying localized difficulty name with semantic colors.
 */
@Composable
fun DifficultyBadge(
    difficulty: Difficulty,
    modifier: Modifier = Modifier,
    variant: DifficultyBadgeVariant = DifficultyBadgeVariant.Subtle,
    size: DifficultyBadgeSize = DifficultyBadgeSize.Medium,
) {
    val semanticColors = AppTheme.semanticColors
    val baseColor = semanticColors.difficultyColor(difficulty)
    val textStyle =
        when (size) {
            DifficultyBadgeSize.Small -> MaterialTheme.typography.labelSmall
            DifficultyBadgeSize.Medium -> MaterialTheme.typography.labelMedium
        }
    val paddingValues =
        when (size) {
            DifficultyBadgeSize.Small -> Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            DifficultyBadgeSize.Medium -> Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        }

    when (variant) {
        DifficultyBadgeVariant.TextOnly -> {
            Text(
                text = difficulty.localizedName(),
                style = textStyle,
                fontWeight = FontWeight.Bold,
                color = baseColor,
                modifier = modifier,
            )
        }

        DifficultyBadgeVariant.Filled -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(12.dp),
                color = baseColor,
                contentColor = Color.White,
            ) {
                Text(
                    text = difficulty.localizedName(),
                    style = textStyle,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = paddingValues,
                )
            }
        }

        DifficultyBadgeVariant.Outlined -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, baseColor),
            ) {
                Text(
                    text = difficulty.localizedName(),
                    style = textStyle,
                    fontWeight = FontWeight.Bold,
                    color = baseColor,
                    modifier = paddingValues,
                )
            }
        }

        DifficultyBadgeVariant.Subtle -> {
            val containerColor = baseColor.copy(alpha = 0.15f)
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(12.dp),
                color = containerColor,
            ) {
                Text(
                    text = difficulty.localizedName(),
                    style = textStyle,
                    fontWeight = FontWeight.Bold,
                    color = baseColor,
                    modifier = paddingValues,
                )
            }
        }
    }
}
