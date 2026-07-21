package com.hackerrank.app.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.core.Constants
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.QuizSession
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.QuizRepository
import com.hackerrank.app.domain.usecase.FinishQuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class QuizState {
    data object Loading : QuizState()

    data class Error(val message: String) : QuizState()

    data class Ready(val session: QuizSession) : QuizState()

    data class Answering(
        val session: QuizSession,
        val selectedAnswer: Int? = null,
        val showExplanation: Boolean = false,
    ) : QuizState()

    data class Completed(
        val session: QuizSession,
        val gamificationResult: GamificationResult?,
    ) : QuizState()
}

@HiltViewModel
class QuizViewModel
    @Inject
    constructor(
        private val contentRepository: ContentRepository,
        private val quizRepository: QuizRepository,
        private val finishQuizUseCase: FinishQuizUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<QuizState>(QuizState.Loading)
        val uiState: StateFlow<QuizState> = _uiState

        private var structureId: String = ""

        fun loadQuiz(slug: String) {
            viewModelScope.launch {
                try {
                    val structure = contentRepository.getStructureBySlug(slug)
                    if (structure != null) {
                        structureId = structure.id
                        val questions =
                            quizRepository
                                .getQuestionsByStructureId(structure.id)
                                .first()
                                .take(Constants.QUESTIONS_PER_QUIZ)
                        val session = QuizSession(questions = questions)
                        _uiState.value = QuizState.Ready(session)
                    }
                } catch (e: Exception) {
                    _uiState.value = QuizState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }

        fun selectAnswer(
            questionId: String,
            selectedIndex: Int,
            timeMs: Long,
        ) {
            val state = _uiState.value
            if (state is QuizState.Ready) {
                val session = state.session
                val question = session.questions.find { it.id == questionId } ?: return
                val newAnswers = session.answers + (questionId to selectedIndex)
                val newQuestionTimes = session.questionTimesMs + (questionId to timeMs)

                _uiState.value =
                    QuizState.Answering(
                        session = session.copy(answers = newAnswers, questionTimesMs = newQuestionTimes),
                        selectedAnswer = selectedIndex,
                        showExplanation = true,
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
                    _uiState.value =
                        QuizState.Ready(
                            session.copy(currentIndex = nextIndex),
                        )
                }
            }
        }

        private fun finishQuiz(session: QuizSession) {
            val totalElapsedMs = session.questionTimesMs.values.sum()
            val completedSession =
                session.copy(
                    endTimeNanos = session.startTimeNanos + totalElapsedMs * 1_000_000L,
                    isCompleted = true,
                )

            viewModelScope.launch {
                try {
                    val result =
                        finishQuizUseCase(
                            session = completedSession,
                            structureId = structureId,
                        )

                    _uiState.value =
                        QuizState.Completed(
                            session = completedSession,
                            gamificationResult = result.gamificationResult,
                        )
                } catch (e: Exception) {
                    _uiState.value = QuizState.Error(e.localizedMessage ?: "Unknown error")
                }
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
