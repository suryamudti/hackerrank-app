package com.hackerrank.app.ui.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.R
import com.hackerrank.app.core.Constants
import com.hackerrank.app.core.localizedName
import com.hackerrank.app.core.theme.AppTheme
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.ui.components.ConfettiOverlay
import com.hackerrank.app.ui.components.EmptyState
import com.hackerrank.app.ui.components.MasteryRing
import com.hackerrank.app.ui.components.badge.DifficultyPill
import com.hackerrank.app.ui.components.card.StatCard
import com.hackerrank.app.ui.components.loading.LoadingView
import kotlinx.coroutines.delay

@Composable
fun ProgressScreen(
    onError: (String) -> Unit = {},
    viewModel: ProgressViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var lastSeenLevel by rememberSaveable { mutableStateOf<Int?>(null) }
    var showConfetti by remember { mutableStateOf(false) }

    val currentLevel = (uiState as? ProgressUiState.Loaded)?.profile?.totalXp?.let { Constants.getLevel(it) } ?: 0

    LaunchedEffect(currentLevel) {
        if (lastSeenLevel != null && currentLevel > lastSeenLevel!!) {
            showConfetti = true
        }
        if (uiState is ProgressUiState.Loaded) {
            lastSeenLevel = currentLevel
        }
    }

    val haptic = LocalHapticFeedback.current
    LaunchedEffect(showConfetti) {
        if (showConfetti) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            delay(2000)
            showConfetti = false
        }
    }

    Box(Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is ProgressUiState.Loading -> {
                LoadingView()
            }

            is ProgressUiState.Error -> {
                EmptyState(
                    icon = Icons.Default.Warning,
                    title = "Error",
                    message = state.message,
                )
            }

            is ProgressUiState.Loaded -> {
                ContentProgressScreen(state)
            }
        }
        ConfettiOverlay(visible = showConfetti)
    }
}

@Composable
private fun ContentProgressScreen(state: ProgressUiState.Loaded) {
    val profile = state.profile

    if (profile == null && state.allProgress.isEmpty()) {
        EmptyState(
            icon = Icons.Default.BarChart,
            title = stringResource(R.string.progress_no_progress_title),
            message = stringResource(R.string.progress_no_progress_message),
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("progressLazyColumn"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Level & XP Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    profile?.let {
                        val xpProgress = Constants.getXpProgress(it.totalXp)
                        MasteryRing(
                            progress = if (xpProgress.second > 0) xpProgress.first.toFloat() / xpProgress.second.toFloat() else 0f,
                            size = 72.dp,
                            strokeWidth = 8.dp,
                            progressColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    } ?: MasteryRing(progress = 0f, size = 72.dp, strokeWidth = 8.dp)

                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.progress_level, profile?.let { Constants.getLevel(it.totalXp) } ?: 0),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        profile?.let {
                            val xpProgress = Constants.getXpProgress(it.totalXp)
                            Text(
                                text = stringResource(R.string.progress_xp_progress, xpProgress.first, xpProgress.second),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Text(
                            text = stringResource(R.string.progress_total_xp, profile?.totalXp ?: 0),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }

        // Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StatCard(
                    label = stringResource(R.string.progress_stat_structures),
                    value = "${state.totalStructures}",
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    label = stringResource(R.string.progress_stat_mastered),
                    value = "${state.masteredStructures}",
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    label = stringResource(R.string.progress_stat_quizzes),
                    value = "${state.allProgress.sumOf { it.quizzesCompleted }}",
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    label = stringResource(R.string.progress_stat_problems),
                    value = "${state.problemStats.solvedCount}",
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Streak Section
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.progress_streak),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "\uD83D\uDD25",
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.semantics { contentDescription = "Streak flame" },
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.progress_current_streak, profile?.currentStreak ?: 0),
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = stringResource(R.string.progress_longest_streak, profile?.longestStreak ?: 0),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        // Algorithm Problems Section
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.progress_problems_card_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        val percent =
                            if (state.problemStats.totalProblems > 0) {
                                (state.problemStats.solvedCount * 100) / state.problemStats.totalProblems
                            } else {
                                0
                            }
                        Text(
                            text = "${state.problemStats.solvedCount} / ${state.problemStats.totalProblems} ($percent%)",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    val progressFraction =
                        if (state.problemStats.totalProblems > 0) {
                            state.problemStats.solvedCount.toFloat() / state.problemStats.totalProblems.toFloat()
                        } else {
                            0f
                        }
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                    )

                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        DifficultyPill(
                            label = stringResource(R.string.difficulty_easy),
                            solved = state.problemStats.easySolved,
                            total = state.problemStats.easyTotal,
                            color = AppTheme.semanticColors.easy,
                            modifier = Modifier.weight(1f),
                        )
                        DifficultyPill(
                            label = stringResource(R.string.difficulty_medium),
                            solved = state.problemStats.mediumSolved,
                            total = state.problemStats.mediumTotal,
                            color = AppTheme.semanticColors.medium,
                            modifier = Modifier.weight(1f),
                        )
                        DifficultyPill(
                            label = stringResource(R.string.difficulty_hard),
                            solved = state.problemStats.hardSolved,
                            total = state.problemStats.hardTotal,
                            color = AppTheme.semanticColors.hard,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        // Recent Activity Section
        if (state.recentActivities.isNotEmpty()) {
            item {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            items(state.recentActivities, key = { it.id }) { activity ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        ),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "📝",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activity.structureName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = formatDate(activity.completedAt),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${activity.score}/${activity.totalQuestions}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "+${activity.xpEarned} XP",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }

        // Category Mastery
        item {
            Text(
                text = stringResource(R.string.progress_category_progress),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        state.categoryMastery.forEach { (category, progress) ->
            item(key = "cat_${category.name}") {
                CategoryProgressCard(
                    category = category,
                    progress = progress,
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")
    return java.time.Instant.ofEpochMilli(timestamp)
        .atZone(java.time.ZoneId.systemDefault())
        .format(formatter)
}

@Composable
private fun CategoryProgressCard(
    category: DataStructureCategory,
    progress: Float,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.localizedName(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
