package com.hackerrank.app.ui.problems

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.core.Constants.DAILY_CHALLENGE_BONUS_XP
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.usecase.ObserveProblemDetailUseCase
import com.hackerrank.app.domain.usecase.RecordDailyChallengeUseCase
import com.hackerrank.app.domain.usecase.RecordProblemSolveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProblemDetailUiState {
    data object Loading : ProblemDetailUiState

    data class Error(val message: String) : ProblemDetailUiState

    data class Loaded(
        val problem: Problem,
        val isSolved: Boolean,
        val isSolving: Boolean,
        val solveResult: GamificationResult?,
        val showSolution: Boolean,
        val isDailyChallenge: Boolean,
        val bonusXp: Int,
    ) : ProblemDetailUiState
}

@HiltViewModel
class ProblemDetailViewModel
    @Inject
    constructor(
        private val observeProblemDetailUseCase: ObserveProblemDetailUseCase,
        private val recordProblemSolveUseCase: RecordProblemSolveUseCase,
        private val recordDailyChallengeUseCase: RecordDailyChallengeUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<ProblemDetailUiState>(ProblemDetailUiState.Loading)
        val uiState: StateFlow<ProblemDetailUiState> = _uiState
        private val isDailyChallengeArg: Boolean = savedStateHandle["isDailyChallenge"] ?: false

        fun loadProblem(problemId: String) {
            viewModelScope.launch {
                try {
                    observeProblemDetailUseCase(problemId).collect { data ->
                        if (data.problem != null) {
                            _uiState.value =
                                ProblemDetailUiState.Loaded(
                                    problem = data.problem,
                                    isSolved = data.isSolved,
                                    isSolving = false,
                                    solveResult = null,
                                    showSolution = false,
                                    isDailyChallenge = isDailyChallengeArg,
                                    bonusXp = if (isDailyChallengeArg) DAILY_CHALLENGE_BONUS_XP else 0,
                                )
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = ProblemDetailUiState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }

        fun solve() {
            val state = _uiState.value
            if (state !is ProblemDetailUiState.Loaded) return
            val problem = state.problem
            viewModelScope.launch {
                try {
                    _uiState.value = state.copy(isSolving = true)
                    val result =
                        if (isDailyChallengeArg) {
                            recordDailyChallengeUseCase(problem.id, problem.difficulty, DAILY_CHALLENGE_BONUS_XP)
                        } else {
                            recordProblemSolveUseCase(problem.id, problem.difficulty)
                        }
                    _uiState.value =
                        state.copy(
                            isSolved = true,
                            isSolving = false,
                            solveResult = result,
                        )
                } catch (e: Exception) {
                    _uiState.value = ProblemDetailUiState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }

        fun toggleSolution() {
            val state = _uiState.value
            if (state is ProblemDetailUiState.Loaded) {
                _uiState.value = state.copy(showSolution = !state.showSolution)
            }
        }

        fun clearSolveResult() {
            val state = _uiState.value
            if (state is ProblemDetailUiState.Loaded) {
                _uiState.value = state.copy(solveResult = null)
            }
        }
    }
