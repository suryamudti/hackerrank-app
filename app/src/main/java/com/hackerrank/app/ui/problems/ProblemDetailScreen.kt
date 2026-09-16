package com.hackerrank.app.ui.problems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.R
import com.hackerrank.app.core.Constants
import com.hackerrank.app.core.localizedName
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.ui.components.EmptyState
import com.hackerrank.app.ui.components.badge.DifficultyBadge
import com.hackerrank.app.ui.components.badge.DifficultyBadgeVariant
import com.hackerrank.app.ui.components.button.AppButton
import com.hackerrank.app.ui.components.button.BookmarkButton
import com.hackerrank.app.ui.components.code.CodeBlock
import com.hackerrank.app.ui.components.code.ExampleCard
import com.hackerrank.app.ui.components.header.SectionHeader
import com.hackerrank.app.ui.components.loading.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemDetailScreen(
    problemId: String,
    isDailyChallenge: Boolean = false,
    onBackClick: () -> Unit,
    viewModel: ProblemDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(problemId, isDailyChallenge) {
        viewModel.loadProblem(problemId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val solveResult =
        when (val s = uiState) {
            is ProblemDetailUiState.Loaded -> s.solveResult
            is ProblemDetailUiState.Loading -> null
            is ProblemDetailUiState.Error -> null
        }

    LaunchedEffect(solveResult) {
        solveResult?.let { result ->
            val bonusXp =
                when (val s = uiState) {
                    is ProblemDetailUiState.Loaded -> s.bonusXp
                    is ProblemDetailUiState.Loading -> 0
                    is ProblemDetailUiState.Error -> 0
                }
            val msg =
                if (isDailyChallenge) {
                    context.getString(R.string.daily_challenge_bonus_earned, bonusXp) + "\n" +
                        context.getString(R.string.xp_earned, result.xpAwarded, result.newLevel)
                } else {
                    context.getString(R.string.xp_earned, result.xpAwarded, result.newLevel)
                }
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSolveResult()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (val s = uiState) {
                            is ProblemDetailUiState.Loaded -> s.problem.title
                            is ProblemDetailUiState.Loading -> stringResource(R.string.problem_loading)
                            is ProblemDetailUiState.Error -> stringResource(R.string.problem_loading)
                        },
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.problem_navigate_back))
                    }
                },
                actions = {
                    if (uiState is ProblemDetailUiState.Loaded) {
                        val isBookmarked = (uiState as ProblemDetailUiState.Loaded).isBookmarked
                        BookmarkButton(
                            isBookmarked = isBookmarked,
                            onClick = { viewModel.toggleBookmark() },
                        )
                    }
                },
            )
        },
    ) { padding ->
        when (val state = uiState) {
            is ProblemDetailUiState.Loading -> {
                LoadingView(modifier = Modifier.padding(padding))
                return@Scaffold
            }

            is ProblemDetailUiState.Error -> {
                EmptyState(
                    icon = Icons.Default.Warning,
                    title = "Error",
                    message = state.message,
                    modifier = Modifier.padding(padding),
                )
                return@Scaffold
            }

            is ProblemDetailUiState.Loaded -> {
                val problem = state.problem

                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                ) {
                    if (isDailyChallenge && !state.isSolved) {
                        Card(
                            colors =
                                CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                ),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    Icons.Default.Whatshot,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.size(24.dp),
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.daily_challenge_bonus, state.bonusXp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DifficultyBadge(
                            difficulty = problem.difficulty,
                            variant = DifficultyBadgeVariant.Subtle,
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = problem.category.localizedName(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))

                    SectionHeader(stringResource(R.string.section_problem), icon = Icons.Default.Star)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = problem.description,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Spacer(Modifier.height(12.dp))
                    ExampleCard(input = problem.inputExample, output = problem.outputExample)

                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(20.dp))

                    SectionHeader(stringResource(R.string.section_approach), icon = Icons.Default.Lightbulb)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = problem.approachExplanation,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(20.dp))

                    SectionHeader(
                        title = stringResource(R.string.section_solution),
                        icon = Icons.Default.Code,
                        modifier = Modifier.fillMaxWidth(),
                        action = {
                            TextButton(onClick = { viewModel.toggleSolution() }) {
                                Text(if (state.showSolution) stringResource(R.string.action_hide) else stringResource(R.string.action_show))
                            }
                        },
                    )
                    Spacer(Modifier.height(8.dp))
                    if (state.showSolution) {
                        CodeBlock(problem.solutionCode)
                    }

                    Spacer(Modifier.height(24.dp))

                    AppButton(
                        text =
                            if (state.isSolved) {
                                stringResource(R.string.action_solved)
                            } else {
                                stringResource(R.string.action_mark_solved, xpForDifficulty(problem.difficulty))
                            },
                        onClick = { viewModel.solve() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSolved,
                        isLoading = state.isSolving,
                        leadingIcon = if (state.isSolved) Icons.Default.CheckCircle else Icons.Default.Star,
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

private fun xpForDifficulty(difficulty: Difficulty): Int =
    when (difficulty) {
        Difficulty.EASY -> Constants.PROBLEM_EASY_XP
        Difficulty.MEDIUM -> Constants.PROBLEM_MEDIUM_XP
        Difficulty.HARD -> Constants.PROBLEM_HARD_XP
    }
