package com.hackerrank.app.ui.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BrowseUiState {
    data object Loading : BrowseUiState
    data class Loaded(
        val groupedStructures: Map<DataStructureCategory, List<DataStructure>>,
        val progressMap: Map<String, Float>
    ) : BrowseUiState
}

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BrowseUiState>(BrowseUiState.Loading)
    val uiState: StateFlow<BrowseUiState> = _uiState

    init {
        loadStructures()
    }

    private fun loadStructures() {
        viewModelScope.launch {
            combine(
                contentRepository.getAllStructures(),
                progressRepository.getAllProgress().map { list ->
                    list.associate { it.structureId to (it.masteryPercentage / 100f) }
                }
            ) { structures, progressMap ->
                val grouped = structures.groupBy { it.category }
                BrowseUiState.Loaded(
                    groupedStructures = DataStructureCategory.entries
                        .mapNotNull { cat -> grouped[cat]?.let { cat to it } }
                        .toMap(),
                    progressMap = progressMap
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
