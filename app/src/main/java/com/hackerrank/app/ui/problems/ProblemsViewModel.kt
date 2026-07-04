package com.hackerrank.app.ui.problems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.repository.ProblemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProblemsUiState(
    val allProblems: List<Problem> = emptyList(),
    val filteredProblems: List<Problem> = emptyList(),
    val solvedIds: Set<String> = emptySet(),
    val selectedDifficulty: Difficulty? = null,
    val selectedCategory: ProblemCategory? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProblemsViewModel @Inject constructor(
    private val problemRepository: ProblemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProblemsUiState())
    val uiState: StateFlow<ProblemsUiState> = _uiState

    private val _selectedDifficulty = MutableStateFlow<Difficulty?>(null)
    private val _selectedCategory = MutableStateFlow<ProblemCategory?>(null)

    init {
        loadProblems()
    }

    private fun loadProblems() {
        viewModelScope.launch {
            combine(
                problemRepository.getAllProblems(),
                problemRepository.getSolvedIds(),
                _selectedDifficulty,
                _selectedCategory
            ) { problems, solvedIds, difficulty, category ->
                val filtered = problems.filter { p ->
                    (difficulty == null || p.difficulty == difficulty) &&
                            (category == null || p.category == category)
                }
                ProblemsUiState(
                    allProblems = problems,
                    filteredProblems = filtered,
                    solvedIds = solvedIds,
                    selectedDifficulty = difficulty,
                    selectedCategory = category,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun selectDifficulty(difficulty: Difficulty?) {
        _selectedDifficulty.value = if (_selectedDifficulty.value == difficulty) null else difficulty
    }

    fun selectCategory(category: ProblemCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }
}
