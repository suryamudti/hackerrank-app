package com.hackerrank.app.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.QuizQuestion
import com.hackerrank.app.domain.model.QuizSession
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import com.hackerrank.app.domain.repository.QuizRepository
import com.hackerrank.app.domain.usecase.RecordQuizCompleteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class QuizState {
    data object Loading : QuizState()
    data class Ready(val session: QuizSession) : QuizState()
    data class Answering(
        val session: QuizSession,
        val selectedAnswer: Int? = null,
        val showExplanation: Boolean = false
    ) : QuizState()
    data class Completed(
        val session: QuizSession,
        val gamificationResult: GamificationResult?
    ) : QuizState()
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val quizRepository: QuizRepository,
    private val progressRepository: ProgressRepository,
    private val recordQuizCompleteUseCase: RecordQuizCompleteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizState>(QuizState.Loading)
    val uiState: StateFlow<QuizState> = _uiState

    private var structureId: String = ""

    fun loadQuiz(slug: String) {
        viewModelScope.launch {
            val structure = contentRepository.getStructureBySlug(slug)
            if (structure != null) {
                structureId = structure.id
                val questions = quizRepository
                    .getQuestionsByStructureId(structure.id)
                    .first()
                    .take(8)
                val session = QuizSession(questions = questions)
                _uiState.value = QuizState.Ready(session)
            }
        }
    }

    fun selectAnswer(questionId: String, selectedIndex: Int) {
        val state = _uiState.value
        if (state is QuizState.Ready) {
            val session = state.session
            val question = session.questions.find { it.id == questionId } ?: return
            val newAnswers = session.answers + (questionId to selectedIndex)

            _uiState.value = QuizState.Answering(
                session = session.copy(answers = newAnswers),
                selectedAnswer = selectedIndex,
                showExplanation = true
            )
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state is QuizState.Answering) {
            val session = state.session
            val nextIndex = session.currentIndex + 1

            if (nextIndex >= session.questions.size) {
                finishQuiz(session)
            } else {
                _uiState.value = QuizState.Ready(
                    session.copy(currentIndex = nextIndex)
                )
            }
        }
    }

    private fun finishQuiz(session: QuizSession) {
        val completedSession = session.copy(
            endTimeNanos = System.nanoTime(),
            isCompleted = true
        )

        viewModelScope.launch {
            // Persist progress
            val existingProgress = progressRepository
                .getProgressByStructureId(structureId)
                .first()

            val newProgress = (existingProgress ?: com.hackerrank.app.domain.model.UserProgress(
                structureId = structureId
            )).copy(
                quizzesCompleted = (existingProgress?.quizzesCompleted ?: 0) + 1,
                totalCorrect = (existingProgress?.totalCorrect ?: 0) + completedSession.score,
                totalQuestions = (existingProgress?.totalQuestions ?: 0) + completedSession.totalQuestions,
                bestScore = maxOf(existingProgress?.bestScore ?: 0, completedSession.score),
                masteryLevel = ((existingProgress?.totalCorrect ?: 0) + completedSession.score) * 100 /
                        ((existingProgress?.totalQuestions ?: 0) + completedSession.totalQuestions)
            )
            progressRepository.upsertProgress(newProgress)

            // Record gamification event
            val result = recordQuizCompleteUseCase(
                score = completedSession.score,
                totalQuestions = completedSession.totalQuestions,
                elapsedTimeMs = completedSession.elapsedTimeMs,
                structureId = structureId
            )

            _uiState.value = QuizState.Completed(
                session = completedSession,
                gamificationResult = result
            )
        }
    }

    fun retry() {
        val state = _uiState.value
        if (state is QuizState.Completed) {
            val questions = state.session.questions.shuffled()
            val newSession = QuizSession(questions = questions)
            _uiState.value = QuizState.Ready(newSession)
        }
    }
}
