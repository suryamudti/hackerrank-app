package com.hackerrank.app.ui.achievements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.R
import com.hackerrank.app.core.localizedTitle
import com.hackerrank.app.ui.components.ConfettiOverlay
import com.hackerrank.app.ui.components.EmptyState
import kotlinx.coroutines.delay

@Composable
fun AchievementsScreen(
    onBadgeClick: (String) -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: AchievementsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var lastSeenBadgesStr by rememberSaveable { mutableStateOf<String?>(null) }
    var showConfetti by remember { mutableStateOf(false) }

    val earnedBadgeIds =
        (uiState as? AchievementsUiState.Loaded)?.badges
            ?.filter { it.isEarned }
            ?.map { it.badge.id }
            ?.toSet() ?: emptySet()
    val earnedBadgesStr = earnedBadgeIds.joinToString(",")

    LaunchedEffect(earnedBadgesStr) {
        if (lastSeenBadgesStr != null) {
            val lastSeenIds = lastSeenBadgesStr!!.split(",").filter { it.isNotEmpty() }.toSet()
            val newlyEarned = earnedBadgeIds - lastSeenIds
            if (newlyEarned.isNotEmpty()) {
                showConfetti = true
            }
        }
        if (uiState is AchievementsUiState.Loaded) {
            lastSeenBadgesStr = earnedBadgesStr
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
            is AchievementsUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.testTag("loadingIndicator"))
                }
            }

            is AchievementsUiState.Error -> {
                EmptyState(
                    icon = Icons.Default.Warning,
                    title = "Error",
                    message = state.message,
                )
            }

            is AchievementsUiState.Loaded -> {
                if (state.badges.isEmpty()) {
                    EmptyState(
                        icon = Icons.Default.EmojiEvents,
                        title = stringResource(R.string.achievements_no_badges_title),
                        message = stringResource(R.string.achievements_no_badges_message),
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(state.badges, key = { it.badge.id }) { badgeState ->
                            BadgeCard(badgeState = badgeState, onClick = onBadgeClick)
                        }
                    }
                }
            }
        }
        ConfettiOverlay(visible = showConfetti)
    }
}

@Composable
private fun BadgeCard(
    badgeState: BadgeWithState,
    onClick: (String) -> Unit,
) {
    val containerColor =
        if (badgeState.isEarned) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        }

    val statusText = if (badgeState.isEarned) stringResource(R.string.badge_earned) else stringResource(R.string.badge_locked)
    val badgeDesc =
        "${badgeState.badge.localizedTitle()} - $statusText" +
            if (badgeState.progress.isNotEmpty()) " - Progress: ${badgeState.progress}" else ""

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable { onClick(badgeState.badge.id) }
                .semantics { contentDescription = badgeDesc },
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (badgeState.isEarned) "\uD83C\uDFC6" else "\uD83D\uDD12",
                    style = MaterialTheme.typography.displaySmall,
                )
            }
            Text(
                text = badgeState.badge.localizedTitle(),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color =
                    if (badgeState.isEarned) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    },
            )
            if (badgeState.progress.isNotEmpty()) {
                Text(
                    text = badgeState.progress,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                )
            }
        }
    }
}
