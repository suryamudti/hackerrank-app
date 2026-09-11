package com.hackerrank.app.ui.problems

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.R
import com.hackerrank.app.core.localizedName
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemsScreen(
    onProblemClick: (String) -> Unit,
    onDailyChallengeClick: (String) -> Unit = onProblemClick,
    onError: (String) -> Unit = {},
    viewModel: ProblemsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.refresh()
        }
    }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection),
    ) {
        when (val state = uiState) {
            is ProblemsUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.testTag("loadingIndicator"))
                }
            }

            is ProblemsUiState.Error -> {
                EmptyState(
                    icon = Icons.Default.Code,
                    title = "Error",
                    message = state.message,
                )
            }

            is ProblemsUiState.Loaded -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("problemsSearchField"),
                        placeholder = { Text(stringResource(R.string.search_problems_hint)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        trailingIcon = {
                            if (state.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search",
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                    )
                    DailyChallengeBanner(
                        state = state.dailyChallenge,
                        onProblemClick = { id -> onDailyChallengeClick(id) },
                    )
                    Spacer(Modifier.height(4.dp))
                    DifficultyFilterRow(
                        selectedDifficulty = state.selectedDifficulty,
                        onDifficultyClick = { viewModel.selectDifficulty(it) },
                        showBookmarkedOnly = state.showBookmarkedOnly,
                        onBookmarkToggle = { viewModel.toggleBookmarkedFilter() },
                    )
                    CategoryFilterRow(
                        selectedCategory = state.selectedCategory,
                        onCategoryClick = { viewModel.selectCategory(it) },
                    )
                    if (state.filteredProblems.isEmpty()) {
                        EmptyState(
                            icon = Icons.Default.Code,
                            title = stringResource(R.string.problems_no_problems_title),
                            message = stringResource(R.string.problems_no_problems_message),
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(state.filteredProblems, key = { it.id }) { problem ->
                                ProblemCard(
                                    problem = problem,
                                    isSolved = problem.id in state.solvedIds,
                                    isBookmarked = problem.id in state.bookmarkedIds,
                                    onBookmarkClick = { viewModel.toggleBookmark(problem.id) },
                                    onClick = { onProblemClick(problem.id) },
                                )
                            }
                        }
                    }
                }
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun DifficultyFilterRow(
    selectedDifficulty: Difficulty?,
    onDifficultyClick: (Difficulty?) -> Unit,
    showBookmarkedOnly: Boolean,
    onBookmarkToggle: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = showBookmarkedOnly,
            onClick = onBookmarkToggle,
            label = { Text(stringResource(R.string.filter_bookmarked)) },
            leadingIcon = {
                Icon(
                    imageVector = if (showBookmarkedOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
            },
        )
        FilterChip(
            selected = selectedDifficulty == null,
            onClick = { onDifficultyClick(null) },
            label = { Text(stringResource(R.string.problems_filter_all)) },
        )
        Difficulty.entries.forEach { diff ->
            FilterChip(
                selected = selectedDifficulty == diff,
                onClick = { onDifficultyClick(diff) },
                label = { Text(diff.localizedName()) },
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor =
                            when (diff) {
                                Difficulty.EASY -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                                Difficulty.MEDIUM -> Color(0xFFFF9800).copy(alpha = 0.2f)
                                Difficulty.HARD -> Color(0xFFF44336).copy(alpha = 0.2f)
                            },
                    ),
            )
        }
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: ProblemCategory?,
    onCategoryClick: (ProblemCategory?) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val categories = listOf<ProblemCategory?>(null) + ProblemCategory.entries
        categories.forEach { cat ->
            FilterChip(
                selected = selectedCategory == cat,
                onClick = { onCategoryClick(cat) },
                label = {
                    Text(
                        if (cat == null) stringResource(R.string.problems_filter_all) else cat.localizedName(),
                        maxLines = 1,
                    )
                },
            )
        }
    }
}

@Composable
private fun ProblemCard(
    problem: Problem,
    isSolved: Boolean,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (isSolved) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = problem.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = problem.difficulty.localizedName(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color =
                            when (problem.difficulty) {
                                Difficulty.EASY -> Color(0xFF4CAF50)
                                Difficulty.MEDIUM -> Color(0xFFFF9800)
                                Difficulty.HARD -> Color(0xFFF44336)
                            },
                    )
                    Text(
                        text = problem.category.localizedName(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            IconButton(
                onClick = onBookmarkClick,
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription =
                        if (isBookmarked) {
                            stringResource(R.string.unbookmark_problem)
                        } else {
                            stringResource(R.string.bookmark_problem)
                        },
                    tint =
                        if (isBookmarked) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        },
                    modifier = Modifier.size(20.dp),
                )
            }
            if (isSolved) {
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = stringResource(R.string.quiz_solved),
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
