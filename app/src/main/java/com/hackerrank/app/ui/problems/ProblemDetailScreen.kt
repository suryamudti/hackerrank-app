package com.hackerrank.app.ui.problems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.R
import com.hackerrank.app.core.localizedName
import com.hackerrank.app.domain.model.Difficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemDetailScreen(
    problemId: String,
    isDailyChallenge: Boolean = false,
    onBackClick: () -> Unit,
    viewModel: ProblemDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(problemId, isDailyChallenge) {
        viewModel.loadProblem(problemId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.solveResult) {
        uiState.solveResult?.let { result ->
            val msg = if (isDailyChallenge) {
                context.getString(R.string.daily_challenge_bonus_earned, uiState.bonusXp) + "\n" +
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
                title = { Text(uiState.problem?.title ?: stringResource(R.string.problem_loading)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.problem_navigate_back))
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading || uiState.problem == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val problem = uiState.problem!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (isDailyChallenge && !uiState.isSolved) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Whatshot, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.daily_challenge_bonus, uiState.bonusXp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            Row {
                Text(
                    text = problem.difficulty.localizedName(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = when (problem.difficulty) {
                        Difficulty.EASY -> Color(0xFF4CAF50)
                        Difficulty.MEDIUM -> Color(0xFFFF9800)
                        Difficulty.HARD -> Color(0xFFF44336)
                    }
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = problem.category.localizedName(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            SectionHeader(stringResource(R.string.section_problem), Icons.Default.Star)
            Spacer(Modifier.height(8.dp))
            Text(
                text = problem.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(12.dp))
            ExampleCard(input = problem.inputExample, output = problem.outputExample)

            Spacer(Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(Modifier.height(20.dp))

            SectionHeader(stringResource(R.string.section_approach), Icons.Default.Lightbulb)
            Spacer(Modifier.height(8.dp))
            Text(
                text = problem.approachExplanation,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeader(stringResource(R.string.section_solution), Icons.Default.Code, modifier = Modifier.weight(1f))
                TextButton(onClick = { viewModel.toggleSolution() }) {
                    Text(if (uiState.showSolution) stringResource(R.string.action_hide) else stringResource(R.string.action_show))
                }
            }
            Spacer(Modifier.height(8.dp))
            if (uiState.showSolution) {
                CodeBlock(problem.solutionCode)
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.solve() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSolved && !uiState.isSolving
            ) {
                if (uiState.isSolving) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp).width(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        if (uiState.isSolved) Icons.Default.CheckCircle else Icons.Default.Star,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (uiState.isSolved) stringResource(R.string.action_solved)
                        else stringResource(R.string.action_mark_solved, xpForDifficulty(problem.difficulty))
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

private fun xpForDifficulty(difficulty: Difficulty): Int = when (difficulty) {
    Difficulty.EASY -> 10
    Difficulty.MEDIUM -> 25
    Difficulty.HARD -> 50
}

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun CodeBlock(code: String) {
    val clipboardManager = LocalClipboardManager.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    clipboardManager.setText(AnnotatedString(code))
                }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = stringResource(R.string.code_copy))
                }
            }
            Text(
                text = code,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun ExampleCard(input: String, output: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.example_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Row {
                Text(
                    text = stringResource(R.string.example_input),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = input,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.height(6.dp))
            Row {
                Text(
                    text = stringResource(R.string.example_output),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = output,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
