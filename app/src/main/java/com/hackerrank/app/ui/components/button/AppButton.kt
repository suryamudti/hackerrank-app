package com.hackerrank.app.ui.components.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class AppButtonVariant {
    Primary,
    Secondary,
    Outlined,
    Text,
}

/**
 * Standardized application button with built-in loading indicator support and multiple styling variants.
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    variant: AppButtonVariant = AppButtonVariant.Primary,
) {
    val isEffectivelyEnabled = enabled && !isLoading

    @Composable
    fun ButtonContent() {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color =
                    when (variant) {
                        AppButtonVariant.Primary -> MaterialTheme.colorScheme.onPrimary
                        AppButtonVariant.Secondary -> MaterialTheme.colorScheme.onSecondaryContainer
                        AppButtonVariant.Outlined -> MaterialTheme.colorScheme.primary
                        AppButtonVariant.Text -> MaterialTheme.colorScheme.primary
                    },
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(text = text)
                if (trailingIcon != null) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }

    when (variant) {
        AppButtonVariant.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier,
                enabled = isEffectivelyEnabled,
                shape = MaterialTheme.shapes.medium,
            ) {
                ButtonContent()
            }
        }

        AppButtonVariant.Secondary -> {
            FilledTonalButton(
                onClick = onClick,
                modifier = modifier,
                enabled = isEffectivelyEnabled,
                shape = MaterialTheme.shapes.medium,
            ) {
                ButtonContent()
            }
        }

        AppButtonVariant.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = isEffectivelyEnabled,
                shape = MaterialTheme.shapes.medium,
            ) {
                ButtonContent()
            }
        }

        AppButtonVariant.Text -> {
            TextButton(
                onClick = onClick,
                modifier = modifier,
                enabled = isEffectivelyEnabled,
                shape = MaterialTheme.shapes.medium,
            ) {
                ButtonContent()
            }
        }
    }
}
