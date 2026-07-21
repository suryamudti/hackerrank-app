package com.hackerrank.app.ui.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.R
import com.hackerrank.app.core.localizedDescription
import com.hackerrank.app.core.localizedTitle
import com.hackerrank.app.domain.usecase.BadgeWithProgress
import com.hackerrank.app.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeDetailScreen(
    badgeId: String,
    onBackClick: () -> Unit,
    viewModel: BadgeDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(badgeId) {
        viewModel.loadBadge(badgeId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.badge_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("backButton")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                ),
                        ),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            when (val state = uiState) {
                is BadgeDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.testTag("loadingIndicator"))
                }
                is BadgeDetailUiState.Error -> {
                    EmptyState(
                        icon = Icons.Default.EmojiEvents,
                        title = "Error",
                        message = state.message,
                    )
                }
                is BadgeDetailUiState.Loaded -> {
                    BadgeDetailContent(state.badgeProgress)
                }
            }
        }
    }
}

@Composable
private fun BadgeDetailContent(badgeProgress: BadgeWithProgress) {
    val badge = badgeProgress.badge
    val isEarned = badgeProgress.isEarned

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Large Badge Icon within styled circles
        Box(
            modifier =
                Modifier
                    .size(160.dp)
                    .background(
                        Brush.radialGradient(
                            colors =
                                if (isEarned) {
                                    listOf(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.surfaceVariant,
                                    )
                                } else {
                                    listOf(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.surface,
                                    )
                                },
                        ),
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = getBadgeEmoji(badge.id, isEarned),
                style = MaterialTheme.typography.displayLarge,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Badge Name
        Text(
            text = badge.localizedTitle(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Unlock status badge
        Surface(
            shape = RoundedCornerShape(16.dp),
            color =
                if (isEarned) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
            modifier = Modifier.testTag("statusBadge"),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (isEarned) "🏆 Earned" else "🔒 Locked",
                    style = MaterialTheme.typography.labelLarge,
                    color =
                        if (isEarned) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Description
        Text(
            text = badge.localizedDescription(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        // Progress section (if applicable)
        if (badgeProgress.currentProgress != null && badgeProgress.targetProgress != null) {
            Spacer(modifier = Modifier.height(32.dp))

            val current = badgeProgress.currentProgress
            val target = badgeProgress.targetProgress
            val progressFraction = if (target > 0) current.toFloat() / target.toFloat() else 0f

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Unlocking Progress",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = formatProgress(badge.id, current, target),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { progressFraction.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth(),
                        strokeCap = StrokeCap.Round,
                    )
                }
            }
        }
    }
}

private fun getBadgeEmoji(
    badgeId: String,
    isEarned: Boolean,
): String {
    if (!isEarned) return "🔒"
    return when (badgeId) {
        "first_steps" -> "👣"
        "quick_learner" -> "🧠"
        "speed_demon" -> "🚀"
        "streak_novice" -> "🔥"
        "streak_master" -> "⚡"
        "streak_legend" -> "☄️"
        "array_ace" -> "🍱"
        "tree_whisperer" -> "🌲"
        "graph_guru" -> "🕸️"
        "completionist" -> "👑"
        "level_10" -> "⭐"
        "level_25" -> "🏅"
        "level_50" -> "🏆"
        else -> "🏆"
    }
}

private fun formatProgress(
    badgeId: String,
    current: Int,
    target: Int,
): String {
    return when {
        badgeId.startsWith("level_") -> "$current / $target XP"
        badgeId.startsWith("streak_") -> "$current / $target days"
        badgeId.contains("_ace") || badgeId.contains("whisperer") || badgeId.contains("guru") || badgeId.equals("completionist") -> "$current / $target mastered"
        else -> "$current / $target"
    }
}
