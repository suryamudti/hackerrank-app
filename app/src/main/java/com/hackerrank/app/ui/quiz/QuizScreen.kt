package com.hackerrank.app.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.R
import com.hackerrank.app.core.localizedTitle
import com.hackerrank.app.domain.model.QuizSession
import com.hackerrank.app.ui.components.ConfettiOverlay
import com.hackerrank.app.ui.components.EmptyState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    structureSlug: String,
    onBackClick: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    LaunchedEffect(structureSlug) {
        viewModel.loadQuiz(structureSlug)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var elapsedNanos by remember { mutableStateOf(0L) }

    val currentIndex =
        when (val state = uiState) {
            is QuizState.Ready -> state.session.currentIndex
            is QuizState.Answering -> state.session.currentIndex
            else -> -1
        }

    val isTimerActive = uiState is QuizState.Ready
    val timeLimitSeconds = 30
    val elapsedSeconds = (elapsedNanos / 1_000_000_000L).toInt()

    LaunchedEffect(currentIndex, isTimerActive) {
        if (isTimerActive && currentIndex != -1) {
            val startTime = System.nanoTime() - elapsedNanos
            while (isActive) {
                elapsedNanos = System.nanoTime() - startTime
                delay(100)
            }
        }
    }

    LaunchedEffect(currentIndex) {
        if (currentIndex != -1) {
            elapsedNanos = 0L
        }
    }

    LaunchedEffect(elapsedSeconds) {
        if (isTimerActive && elapsedSeconds >= timeLimitSeconds) {
            val state = uiState
            if (state is QuizState.Ready) {
                val currentQuestion = state.session.questions[state.session.currentIndex]
                viewModel.selectAnswer(currentQuestion.id, -1, timeLimitSeconds * 1000L)
            }
        }
    }

    var showReview by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.quiz_title)) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (showReview) {
                            showReview = false
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.quiz_back))
                    }
                },
                actions = {
                    if ((uiState is QuizState.Ready || uiState is QuizState.Answering) && !showReview) {
                        val remainingSeconds = maxOf(0, timeLimitSeconds - elapsedSeconds)
                        Text(
                            text = String.format(java.util.Locale.US, "%02d:%02d", remainingSeconds / 60, remainingSeconds % 60),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 16.dp),
                            color = if (remainingSeconds <= 5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            if (showReview && uiState is QuizState.Completed) {
                ReviewAnswersContent(
                    session = (uiState as QuizState.Completed).session,
                    onBackToResults = { showReview = false },
                )
            } else {
                when (val state = uiState) {
                    is QuizState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is QuizState.Error -> {
                        EmptyState(
                            icon = Icons.Default.Warning,
                            title = "Error",
                            message = state.message,
                        )
                    }

                    is QuizState.Ready -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            val progress = (timeLimitSeconds - (elapsedNanos / 1_000_000_000f)) / timeLimitSeconds
                            LinearProgressIndicator(
                                progress = { progress.coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth(),
                                color = if (progress <= 0.2f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            )
                            QuizContent(
                                session = state.session,
                                selectedAnswer = null,
                                showExplanation = false,
                                onAnswer = { id, idx, timeMs -> viewModel.selectAnswer(id, idx, timeMs) },
                                onNext = viewModel::nextQuestion,
                                questionElapsedNanos = elapsedNanos,
                            )
                        }
                    }

                    is QuizState.Answering -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            LinearProgressIndicator(
                                progress = { 0f },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            QuizContent(
                                session = state.session,
                                selectedAnswer = state.selectedAnswer,
                                showExplanation = state.showExplanation,
                                onAnswer = { id, idx, timeMs -> viewModel.selectAnswer(id, idx, timeMs) },
                                onNext = viewModel::nextQuestion,
                                questionElapsedNanos = elapsedNanos,
                            )
                        }
                    }

                    is QuizState.Completed -> {
                        QuizResults(
                            session = state.session,
                            gamificationResult = state.gamificationResult,
                            onRetry = {
                                showReview = false
                                viewModel.retry()
                            },
                            onBack = onBackClick,
                            onReviewClick = { showReview = true },
                        )
                    }
                }
            }

            // Confetti Overlay: trigger when level increases or badges are earned
            val showConfetti =
                remember(uiState) {
                    val state = uiState
                    state is QuizState.Completed && (
                        (state.gamificationResult?.newLevel ?: 0) > (state.gamificationResult?.previousLevel ?: 0) ||
                            (state.gamificationResult?.newBadges?.isNotEmpty() == true)
                    )
                }
            val haptic = LocalHapticFeedback.current
            LaunchedEffect(showConfetti) {
                if (showConfetti) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
            ConfettiOverlay(visible = showConfetti)
        }
    }
}

@Composable
private fun QuizContent(
    session: QuizSession,
    selectedAnswer: Int?,
    showExplanation: Boolean,
    onAnswer: (String, Int, Long) -> Unit,
    onNext: () -> Unit,
    questionElapsedNanos: Long,
) {
    if (session.questions.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.quiz_no_questions))
        }
        return
    }

    val question = session.questions[session.currentIndex]
    val isLastQuestion = session.currentIndex >= session.questions.size - 1

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        // Progress
        LinearProgressIndicator(
            progress = { (session.currentIndex + 1).toFloat() / session.questions.size },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.quiz_question_x_of_y, session.currentIndex + 1, session.questions.size),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(24.dp))

        // Question
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(Modifier.height(20.dp))

            // Options
            question.options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == index
                val isCorrect = index == question.correctIndex
                val buttonColor =
                    when {
                        !showExplanation -> MaterialTheme.colorScheme.primary
                        isCorrect -> Color(0xFF4CAF50)
                        isSelected -> Color(0xFFE53935)
                        else -> MaterialTheme.colorScheme.primary
                    }

                val optionLetter = ('A' + index)
                val optionDesc =
                    "Option $optionLetter: $option" +
                        if (showExplanation && isCorrect) {
                            " - Correct answer"
                        } else if (showExplanation && isSelected && !isCorrect) {
                            " - Your answer (incorrect)"
                        } else {
                            ""
                        }

                Button(
                    onClick = {
                        if (!showExplanation) {
                            onAnswer(question.id, index, questionElapsedNanos / 1_000_000L)
                        }
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .semantics {
                                contentDescription = "$optionDesc"
                            },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            disabledContainerColor = buttonColor.copy(alpha = 0.3f),
                        ),
                    enabled = !showExplanation || isCorrect || isSelected,
                ) {
                    Text(
                        text = "${('A' + index)}. $option",
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }

            // Explanation
            AnimatedVisibility(
                visible = showExplanation,
                enter = fadeIn() + slideInVertically(),
            ) {
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val isCorrect = selectedAnswer == question.correctIndex
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription =
                                    if (isCorrect) {
                                        stringResource(
                                            R.string.quiz_correct,
                                        )
                                    } else {
                                        stringResource(R.string.quiz_incorrect)
                                    },
                                tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE53935),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (isCorrect) stringResource(R.string.quiz_correct) else stringResource(R.string.quiz_incorrect),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE53935),
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
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (isLastQuestion) stringResource(R.string.quiz_see_results) else stringResource(R.string.quiz_next_question))
            }
        }
    }
}

@Composable
private fun QuizResults(
    session: QuizSession,
    gamificationResult: com.hackerrank.app.domain.model.GamificationResult?,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onReviewClick: () -> Unit,
) {
    val percentage = (session.score.toFloat() / session.totalQuestions.toFloat()) * 100f
    val timeSeconds = session.elapsedTimeMs / 1000

    val xpAwarded = gamificationResult?.xpAwarded ?: 0
    val animatedXp = remember { Animatable(0f) }
    LaunchedEffect(xpAwarded) {
        if (xpAwarded > 0) {
            animatedXp.animateTo(
                targetValue = xpAwarded.toFloat(),
                animationSpec = tween(durationMillis = 1500),
            )
        }
    }

    val showLevelUp = remember { mutableStateOf(false) }
    LaunchedEffect(gamificationResult) {
        if (gamificationResult != null && gamificationResult.newLevel > gamificationResult.previousLevel) {
            delay(1000)
            showLevelUp.value = true
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.quiz_complete),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(24.dp))

        // Score
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${session.score}/${session.totalQuestions}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${percentage.toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Stats
        Text(
            text = stringResource(R.string.quiz_time, timeSeconds / 60, timeSeconds % 60),
            style = MaterialTheme.typography.bodyLarge,
        )

        if (gamificationResult != null) {
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.quiz_xp_earned, animatedXp.value.toInt()),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                    AnimatedVisibility(
                        visible = showLevelUp.value,
                        enter = fadeIn(animationSpec = tween(500)) + expandVertically(animationSpec = tween(500)),
                    ) {
                        val scale by animateFloatAsState(
                            targetValue = if (showLevelUp.value) 1.1f else 0.8f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                        )
                        Text(
                            text = stringResource(R.string.quiz_level_up, gamificationResult.newLevel),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier =
                                Modifier
                                    .padding(top = 4.dp)
                                    .scale(scale),
                        )
                    }
                    if (gamificationResult.newBadges.isNotEmpty()) {
                        gamificationResult.newBadges.forEach { badge ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp),
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = stringResource(R.string.badge_earned),
                                    tint = Color(0xFFFFC107),
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = stringResource(R.string.quiz_badge_earned, badge.localizedTitle()))
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onReviewClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Review Answers")
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.quiz_go_back))
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.action_back))
            }
            Button(
                onClick = onRetry,
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.Replay, contentDescription = stringResource(R.string.quiz_retry_desc))
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.action_retry))
            }
        }
    }
}

@Composable
private fun ReviewAnswersContent(
    session: QuizSession,
    onBackToResults: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconButton(onClick = onBackToResults) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to results")
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Review Answers",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            itemsIndexed(session.questions) { index, question ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${index + 1}. ${question.question}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(Modifier.height(12.dp))

                        val userAnswerIndex = session.answers[question.id] ?: -1
                        val isUserCorrect = userAnswerIndex == question.correctIndex

                        question.options.forEachIndexed { optIndex, option ->
                            val isCorrect = optIndex == question.correctIndex
                            val isSelected = optIndex == userAnswerIndex

                            val backgroundColor =
                                when {
                                    isCorrect -> Color(0xFFE8F5E9)
                                    isSelected && !isCorrect -> Color(0xFFFFEBEE)
                                    else -> Color.Transparent
                                }

                            val borderColor =
                                when {
                                    isCorrect -> Color(0xFF4CAF50)
                                    isSelected && !isCorrect -> Color(0xFFE53935)
                                    else -> Color.Transparent
                                }

                            Card(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                border =
                                    if (borderColor != Color.Transparent) {
                                        androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            borderColor,
                                        )
                                    } else {
                                        null
                                    },
                                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "${('A' + optIndex)}. $option",
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color =
                                            when {
                                                isCorrect -> Color(0xFF2E7D32)
                                                isSelected && !isCorrect -> Color(0xFFC62828)
                                                else -> MaterialTheme.colorScheme.onSurface
                                            },
                                    )
                                    if (isCorrect) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF4CAF50))
                                    } else if (isSelected) {
                                        Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = Color(0xFFE53935))
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Explanation: ${question.explanation}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
