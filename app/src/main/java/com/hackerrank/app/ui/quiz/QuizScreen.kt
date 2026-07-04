package com.hackerrank.app.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.domain.model.QuizSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    structureSlug: String,
    onBackClick: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    LaunchedEffect(structureSlug) {
        viewModel.loadQuiz(structureSlug)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is QuizState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is QuizState.Ready -> {
                    QuizContent(
                        session = state.session,
                        selectedAnswer = null,
                        showExplanation = false,
                        onAnswer = { id, idx -> viewModel.selectAnswer(id, idx) },
                        onNext = viewModel::nextQuestion
                    )
                }

                is QuizState.Answering -> {
                    QuizContent(
                        session = state.session,
                        selectedAnswer = state.selectedAnswer,
                        showExplanation = state.showExplanation,
                        onAnswer = { id, idx -> viewModel.selectAnswer(id, idx) },
                        onNext = viewModel::nextQuestion
                    )
                }

                is QuizState.Completed -> {
                    QuizResults(
                        session = state.session,
                        gamificationResult = state.gamificationResult,
                        onRetry = viewModel::retry,
                        onBack = onBackClick
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizContent(
    session: QuizSession,
    selectedAnswer: Int?,
    showExplanation: Boolean,
    onAnswer: (String, Int) -> Unit,
    onNext: () -> Unit
) {
    if (session.questions.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No questions available.")
        }
        return
    }

    val question = session.questions[session.currentIndex]
    val isLastQuestion = session.currentIndex >= session.questions.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Progress
        LinearProgressIndicator(
            progress = { (session.currentIndex + 1).toFloat() / session.questions.size },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Question ${session.currentIndex + 1} of ${session.questions.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        // Question
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(20.dp))

            // Options
            question.options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == index
                val isCorrect = index == question.correctIndex
                val buttonColor = when {
                    !showExplanation -> MaterialTheme.colorScheme.primary
                    isCorrect -> Color(0xFF4CAF50)
                    isSelected -> Color(0xFFE53935)
                    else -> MaterialTheme.colorScheme.primary
                }

                Button(
                    onClick = {
                        if (!showExplanation) {
                            onAnswer(question.id, index)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .semantics {
                            contentDescription = "Option ${('A' + index)}: $option" + if (showExplanation && isCorrect) " - Correct answer" else if (showExplanation && isSelected && !isCorrect) " - Your answer (incorrect)" else ""
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        disabledContainerColor = buttonColor.copy(alpha = 0.3f)
                    ),
                    enabled = !showExplanation || isCorrect || isSelected
                ) {
                    Text(
                        text = "${('A' + index)}. $option",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            // Explanation
            AnimatedVisibility(
                visible = showExplanation,
                enter = fadeIn() + slideInVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val isCorrect = selectedAnswer == question.correctIndex
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE53935)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (isCorrect) "Correct!" else "Incorrect",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE53935)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(text = question.explanation)
                    }
                }
            }
        }

        // Next button
        if (showExplanation) {
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLastQuestion) "See Results" else "Next Question")
            }
        }
    }
}

@Composable
private fun QuizResults(
    session: QuizSession,
    gamificationResult: com.hackerrank.app.domain.model.GamificationResult?,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    val percentage = (session.score.toFloat() / session.totalQuestions.toFloat()) * 100f
    val timeSeconds = session.elapsedTimeMs / 1000

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Complete!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        // Score
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${session.score}/${session.totalQuestions}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${percentage.toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Stats
        Text(
            text = "Time: ${timeSeconds / 60}m ${timeSeconds % 60}s",
            style = MaterialTheme.typography.bodyLarge
        )

        gamificationResult?.let { result ->
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "+${result.xpAwarded} XP",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    if (result.newLevel > result.previousLevel) {
                        Text(
                            text = "Level Up! Now level ${result.newLevel}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    if (result.newBadges.isNotEmpty()) {
                        result.newBadges.forEach { badge ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFFFC107))
                                Spacer(Modifier.width(8.dp))
                                Text(text = "Badge: ${badge.name}")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                            Spacer(Modifier.width(4.dp))
                            Text("Back")
                        }
                        Button(
                            onClick = onRetry,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Replay, contentDescription = "Retry quiz")
                            Spacer(Modifier.width(4.dp))
                            Text("Retry")
                        }
                    }
    }
}
