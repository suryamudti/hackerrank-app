package com.hackerrank.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.core.Constants
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
    private val progressRepository: ProgressRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProgressUiState>(ProgressUiState.Loading)
    val uiState: StateFlow<ProgressUiState> = _uiState

    init {
        loadProgress()
    }

    private fun loadProgress() {
        viewModelScope.launch {
            val structures = contentRepository.getAllStructures().first()
            val totalStructures = structures.size

            combine(
                progressRepository.getProfile(),
                progressRepository.getAllProgress()
            ) { profile, allProgress ->
                val categoryProgress = structures
                    .groupBy { it.category }
                    .mapValues { (_, structs) ->
                        val structIds = structs.map { it.id }.toSet()
                        val relevantProgress = allProgress.filter { it.structureId in structIds }
                        if (structs.isEmpty()) 0f
                        else {
                            val correct = relevantProgress.sumOf { it.totalCorrect }
                            val total = relevantProgress.sumOf { it.totalQuestions }
                            if (total > 0) correct.toFloat() / total else 0f
                        }
                    }
                val masteredCount = allProgress.count { it.masteryLevel >= 80 }

                _uiState.value = ProgressUiState.Loaded(
                    profile = profile,
                    allProgress = allProgress,
                    categoryMastery = categoryProgress,
                    totalStructures = totalStructures,
                    masteredStructures = masteredCount
                )
            }.collect {}
        }
    }
}
