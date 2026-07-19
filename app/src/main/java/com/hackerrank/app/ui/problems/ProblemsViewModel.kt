package com.hackerrank.app.ui.problems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.data.remote.DailyChallengeApi
import com.hackerrank.app.data.remote.DailyChallengeResponse
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.repository.ProblemRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class DailyChallengeState(
    val isLoading: Boolean = true,
    val problem: Problem? = null,
    val bonusXp: Int = 0,
    val isCompleted: Boolean = false,
    val isUnavailable: Boolean = false
)

sealed interface ProblemsUiState {
    data object Loading : ProblemsUiState
    data class Loaded(
        val allProblems: List<Problem>,
        val filteredProblems: List<Problem>,
        val solvedIds: Set<String>,
        val selectedDifficulty: Difficulty?,
        val selectedCategory: ProblemCategory?,
        val dailyChallenge: DailyChallengeState
    ) : ProblemsUiState
}

@HiltViewModel
class ProblemsViewModel @Inject constructor(
    private val problemRepository: ProblemRepository,
    private val progressRepository: ProgressRepository,
    private val dailyChallengeApi: DailyChallengeApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProblemsUiState>(ProblemsUiState.Loading)
    val uiState: StateFlow<ProblemsUiState> = _uiState

    private val _selectedDifficulty = MutableStateFlow<Difficulty?>(null)
    private val _selectedCategory = MutableStateFlow<ProblemCategory?>(null)
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    init {
        loadProblems()
        loadDailyChallenge()
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
                val current = _uiState.value
                val dailyChallenge = if (current is ProblemsUiState.Loaded) current.dailyChallenge else DailyChallengeState()
                ProblemsUiState.Loaded(
                    allProblems = problems,
                    filteredProblems = filtered,
                    solvedIds = solvedIds,
                    selectedDifficulty = difficulty,
                    selectedCategory = category,
                    dailyChallenge = dailyChallenge
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun loadDailyChallenge() {
        viewModelScope.launch {
            val today = LocalDate.now().format(dateFormatter)
            val isCompleted = progressRepository.isDailyChallengeCompleted(today)
            val cached = progressRepository.getDailyChallengeState().first()

            if (cached != null && cached.date == today) {
                resolveDailyChallengeProblem(cached, isCompleted)
                return@launch
            }

            val response = dailyChallengeApi.fetchToday()
            if (response != null && response.date == today) {
                progressRepository.cacheDailyChallengeResponse(response)
                resolveDailyChallengeProblem(response, isCompleted)
            } else if (cached != null) {
                resolveDailyChallengeProblem(cached, isCompleted)
            } else {
                _uiState.value = ProblemsUiState.Loaded(
                    allProblems = emptyList(),
                    filteredProblems = emptyList(),
                    solvedIds = emptySet(),
                    selectedDifficulty = null,
                    selectedCategory = null,
                    dailyChallenge = DailyChallengeState(
                        isLoading = false,
                        isUnavailable = true
                    )
                )
            }
        }
    }

    private suspend fun resolveDailyChallengeProblem(
        response: DailyChallengeResponse,
        isCompleted: Boolean
    ) {
        val problem = problemRepository.getProblemById(response.problemId).first()
        val current = _uiState.value
        val loadedState = if (current is ProblemsUiState.Loaded) current else return
        _uiState.value = loadedState.copy(
            dailyChallenge = DailyChallengeState(
                isLoading = false,
                problem = problem,
                bonusXp = response.bonusXp,
                isCompleted = isCompleted
            )
        )
    }

    fun selectDifficulty(difficulty: Difficulty?) {
        _selectedDifficulty.value = if (_selectedDifficulty.value == difficulty) null else difficulty
    }

    fun selectCategory(category: ProblemCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }
}
