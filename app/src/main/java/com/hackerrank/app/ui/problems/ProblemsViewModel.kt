package com.hackerrank.app.ui.problems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.usecase.GetDailyChallengeUseCase
import com.hackerrank.app.domain.usecase.ObserveProblemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DailyChallengeState(
    val isLoading: Boolean = true,
    val problem: Problem? = null,
    val bonusXp: Int = 0,
    val isCompleted: Boolean = false,
    val isUnavailable: Boolean = false,
)

sealed interface ProblemsUiState {
    data object Loading : ProblemsUiState

    data class Error(val message: String) : ProblemsUiState

    data class Loaded(
        val allProblems: List<Problem>,
        val filteredProblems: List<Problem>,
        val solvedIds: Set<String>,
        val selectedDifficulty: Difficulty?,
        val selectedCategory: ProblemCategory?,
        val dailyChallenge: DailyChallengeState,
    ) : ProblemsUiState
}

@HiltViewModel
class ProblemsViewModel
    @Inject
    constructor(
        private val observeProblemsUseCase: ObserveProblemsUseCase,
        private val getDailyChallengeUseCase: GetDailyChallengeUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<ProblemsUiState>(ProblemsUiState.Loading)
        val uiState: StateFlow<ProblemsUiState> = _uiState

        private val _isRefreshing = MutableStateFlow(false)
        val isRefreshing: StateFlow<Boolean> = _isRefreshing

        private val _selectedDifficulty = MutableStateFlow<Difficulty?>(null)
        private val _selectedCategory = MutableStateFlow<ProblemCategory?>(null)

        private var problemsJob: kotlinx.coroutines.Job? = null
        private var dailyChallengeJob: kotlinx.coroutines.Job? = null

        init {
            loadProblems()
            loadDailyChallenge()
        }

        private fun loadProblems() {
            problemsJob?.cancel()
            problemsJob =
                viewModelScope.launch {
                    try {
                        combine(
                            observeProblemsUseCase(),
                            _selectedDifficulty,
                            _selectedCategory,
                        ) { problemsData, difficulty, category ->
                            val filtered =
                                problemsData.allProblems.filter { p ->
                                    (difficulty == null || p.difficulty == difficulty) &&
                                        (category == null || p.category == category)
                                }
                            val current = _uiState.value
                            val dailyChallenge = if (current is ProblemsUiState.Loaded) current.dailyChallenge else DailyChallengeState()
                            ProblemsUiState.Loaded(
                                allProblems = problemsData.allProblems,
                                filteredProblems = filtered,
                                solvedIds = problemsData.solvedIds,
                                selectedDifficulty = difficulty,
                                selectedCategory = category,
                                dailyChallenge = dailyChallenge,
                            )
                        }.collect { state ->
                            _uiState.value = state
                        }
                    } catch (e: Exception) {
                        _uiState.value = ProblemsUiState.Error(e.localizedMessage ?: "Unknown error")
                    }
                }
        }

        private fun loadDailyChallenge() {
            dailyChallengeJob?.cancel()
            dailyChallengeJob =
                viewModelScope.launch {
                    try {
                        val result = getDailyChallengeUseCase()
                        val current = _uiState.value
                        val dcState =
                            DailyChallengeState(
                                isLoading = false,
                                problem = result.problem,
                                bonusXp = result.bonusXp,
                                isCompleted = result.isCompleted,
                                isUnavailable = !result.isAvailable,
                            )
                        if (current is ProblemsUiState.Loaded) {
                            _uiState.value = current.copy(dailyChallenge = dcState)
                        } else {
                            _uiState.value =
                                ProblemsUiState.Loaded(
                                    allProblems = emptyList(),
                                    filteredProblems = emptyList(),
                                    solvedIds = emptySet(),
                                    selectedDifficulty = null,
                                    selectedCategory = null,
                                    dailyChallenge = dcState,
                                )
                        }
                    } catch (e: Exception) {
                        _uiState.value = ProblemsUiState.Error(e.localizedMessage ?: "Unknown error")
                    }
                }
        }

        fun refresh() {
            viewModelScope.launch {
                _isRefreshing.value = true
                try {
                    kotlinx.coroutines.delay(1000)
                    loadProblems()
                    loadDailyChallenge()
                } catch (e: Exception) {
                    _uiState.value = ProblemsUiState.Error(e.localizedMessage ?: "Unknown error")
                } finally {
                    _isRefreshing.value = false
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
