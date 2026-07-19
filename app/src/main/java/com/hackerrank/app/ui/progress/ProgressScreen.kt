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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.R
import com.hackerrank.app.core.Constants
import com.hackerrank.app.core.localizedName
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.ui.components.EmptyState
import com.hackerrank.app.ui.components.MasteryRing

@Composable
fun ProgressScreen(
    onError: (String) -> Unit = {},
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is ProgressUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ProgressUiState.Loaded -> {
            ContentProgressScreen(state)
        }
    }
}

@Composable
private fun ContentProgressScreen(state: ProgressUiState.Loaded) {
    val profile = state.profile

    if (profile == null && state.allProgress.isEmpty()) {
        EmptyState(
            icon = Icons.Default.BarChart,
            title = stringResource(R.string.progress_no_progress_title),
            message = stringResource(R.string.progress_no_progress_message)
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Level & XP Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    profile?.let {
                        val xpProgress = Constants.getXpProgress(it.totalXp)
                        MasteryRing(
                            progress = if (xpProgress.second > 0) xpProgress.first.toFloat() / xpProgress.second.toFloat() else 0f,
                            size = 72.dp,
                            strokeWidth = 8.dp,
                            progressColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } ?: MasteryRing(progress = 0f, size = 72.dp, strokeWidth = 8.dp)

                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.progress_level, profile?.let { Constants.getLevel(it.totalXp) } ?: 0),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        profile?.let {
                            val xpProgress = Constants.getXpProgress(it.totalXp)
                            Text(
                                text = stringResource(R.string.progress_xp_progress, xpProgress.first, xpProgress.second),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Text(
                            text = stringResource(R.string.progress_total_xp, profile?.totalXp ?: 0),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = stringResource(R.string.progress_stat_structures),
                    value = "${state.totalStructures}",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = stringResource(R.string.progress_stat_mastered),
                    value = "${state.masteredStructures}",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = stringResource(R.string.progress_stat_quizzes),
                    value = "${state.allProgress.sumOf { it.quizzesCompleted }}",
                    modifier = Modifier.weight(1f)
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
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "\uD83D\uDD25",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.progress_current_streak, profile?.currentStreak ?: 0),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = stringResource(R.string.progress_longest_streak, profile?.longestStreak ?: 0),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                fontWeight = FontWeight.Bold
            )
        }

        state.categoryMastery.forEach { (category, progress) ->
            item(key = "cat_${category.name}") {
                CategoryProgressCard(
                    category = category,
                    progress = progress
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CategoryProgressCard(
    category: DataStructureCategory,
    progress: Float
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.localizedName(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
