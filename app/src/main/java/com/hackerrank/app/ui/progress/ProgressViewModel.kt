package com.hackerrank.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.core.Constants
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.usecase.ObserveProgressOverviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProgressUiState {
    data object Loading : ProgressUiState
    data class Loaded(
        val profile: UserProfile?,
        val allProgress: List<UserProgress>,
        val categoryMastery: Map<DataStructureCategory, Float>,
        val totalStructures: Int,
        val masteredStructures: Int
    ) : ProgressUiState
}

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val observeProgressOverviewUseCase: ObserveProgressOverviewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProgressUiState>(ProgressUiState.Loading)
    val uiState: StateFlow<ProgressUiState> = _uiState

    init {
        viewModelScope.launch {
            observeProgressOverviewUseCase().collect { overview ->
                _uiState.value = ProgressUiState.Loaded(
                    profile = overview.profile,
                    allProgress = overview.allProgress,
                    categoryMastery = overview.categoryMastery,
                    totalStructures = overview.totalStructures,
                    masteredStructures = overview.masteredStructures
                )
            }
        }
    }
}
