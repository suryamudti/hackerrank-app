package com.hackerrank.app.ui.problems

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.core.Constants.DAILY_CHALLENGE_BONUS_XP
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.repository.ProblemRepository
import com.hackerrank.app.domain.usecase.RecordDailyChallengeUseCase
import com.hackerrank.app.domain.usecase.RecordProblemSolveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProblemDetailUiState(
    val problem: Problem? = null,
    val isSolved: Boolean = false,
    val isSolving: Boolean = false,
    val solveResult: GamificationResult? = null,
    val isLoading: Boolean = true,
    val showSolution: Boolean = false,
    val isDailyChallenge: Boolean = false,
    val bonusXp: Int = 0
)

@HiltViewModel
class ProblemDetailViewModel @Inject constructor(
    private val problemRepository: ProblemRepository,
    private val recordProblemSolveUseCase: RecordProblemSolveUseCase,
    private val recordDailyChallengeUseCase: RecordDailyChallengeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProblemDetailUiState())
    val uiState: StateFlow<ProblemDetailUiState> = _uiState
    private val problemId: String = savedStateHandle["problemId"] ?: ""
    private val isDailyChallengeArg: Boolean = savedStateHandle["isDailyChallenge"] ?: false

    init {
        if (problemId.isNotEmpty()) loadProblem(problemId)
    }

    fun loadProblem(problemId: String) {
        viewModelScope.launch {
            combine(
                problemRepository.getProblemById(problemId),
                problemRepository.isSolved(problemId)
            ) { problem, isSolved ->
                _uiState.value = ProblemDetailUiState(
                    problem = problem,
                    isSolved = isSolved,
                    isLoading = false,
                    isDailyChallenge = isDailyChallengeArg,
                    bonusXp = if (isDailyChallengeArg) DAILY_CHALLENGE_BONUS_XP else 0
                )
            }.collect {}
        }
    }

    fun solve() {
        val problem = _uiState.value.problem ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSolving = true)
            val result = if (isDailyChallengeArg) {
                recordDailyChallengeUseCase(problem.id, problem.difficulty, DAILY_CHALLENGE_BONUS_XP)
            } else {
                recordProblemSolveUseCase(problem.id, problem.difficulty)
            }
            _uiState.value = _uiState.value.copy(
                isSolved = true,
                isSolving = false,
                solveResult = result
            )
        }
    }

    fun toggleSolution() {
        _uiState.value = _uiState.value.copy(
            showSolution = !_uiState.value.showSolution
        )
    }

    fun clearSolveResult() {
        _uiState.value = _uiState.value.copy(solveResult = null)
    }
}
