package com.hackerrank.app.ui.problems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.repository.ProblemRepository
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
    val showSolution: Boolean = false
)

@HiltViewModel
class ProblemDetailViewModel @Inject constructor(
    private val problemRepository: ProblemRepository,
    private val recordProblemSolveUseCase: RecordProblemSolveUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProblemDetailUiState())
    val uiState: StateFlow<ProblemDetailUiState> = _uiState

    fun loadProblem(problemId: String) {
        viewModelScope.launch {
            combine(
                problemRepository.getProblemById(problemId),
                problemRepository.isSolved(problemId)
            ) { problem, isSolved ->
                _uiState.value = ProblemDetailUiState(
                    problem = problem,
                    isSolved = isSolved,
                    isLoading = false
                )
            }.collect {}
        }
    }

    fun solve() {
        val problem = _uiState.value.problem ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSolving = true)
            val result = recordProblemSolveUseCase(problem.id, problem.difficulty)
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
