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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun ProblemsScreen(
    onProblemClick: (String) -> Unit,
    onError: (String) -> Unit = {},
    viewModel: ProblemsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.allProblems.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Code,
            title = stringResource(R.string.problems_no_problems_title),
            message = stringResource(R.string.problems_no_problems_message)
        )
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DifficultyFilterRow(
            selectedDifficulty = uiState.selectedDifficulty,
            onDifficultyClick = { viewModel.selectDifficulty(it) }
        )
        CategoryFilterRow(
            selectedCategory = uiState.selectedCategory,
            onCategoryClick = { viewModel.selectCategory(it) }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.filteredProblems, key = { it.id }) { problem ->
                ProblemCard(
                    problem = problem,
                    isSolved = problem.id in uiState.solvedIds,
                    onClick = { onProblemClick(problem.id) }
                )
            }
        }
    }
}

@Composable
private fun DifficultyFilterRow(
    selectedDifficulty: Difficulty?,
    onDifficultyClick: (Difficulty?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedDifficulty == null,
            onClick = { onDifficultyClick(null) },
            label = { Text(stringResource(R.string.problems_filter_all)) }
        )
        Difficulty.entries.forEach { diff ->
            FilterChip(
                selected = selectedDifficulty == diff,
                onClick = { onDifficultyClick(diff) },
                label = { Text(diff.localizedName()) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = when (diff) {
                        Difficulty.EASY -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        Difficulty.MEDIUM -> Color(0xFFFF9800).copy(alpha = 0.2f)
                        Difficulty.HARD -> Color(0xFFF44336).copy(alpha = 0.2f)
                    }
                )
            )
        }
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: ProblemCategory?,
    onCategoryClick: (ProblemCategory?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val categories = listOf<ProblemCategory?>(null) + ProblemCategory.entries
        categories.forEach { cat ->
            FilterChip(
                selected = selectedCategory == cat,
                onClick = { onCategoryClick(cat) },
                label = {
                    Text(
                        if (cat == null) stringResource(R.string.problems_filter_all) else cat.localizedName(),
                        maxLines = 1
                    )
                }
            )
        }
    }
}

@Composable
private fun ProblemCard(
    problem: Problem,
    isSolved: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSolved) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = problem.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = problem.difficulty.localizedName(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (problem.difficulty) {
                            Difficulty.EASY -> Color(0xFF4CAF50)
                            Difficulty.MEDIUM -> Color(0xFFFF9800)
                            Difficulty.HARD -> Color(0xFFF44336)
                        }
                    )
                    Text(
                        text = problem.category.localizedName(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isSolved) {
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = stringResource(R.string.quiz_solved),
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
