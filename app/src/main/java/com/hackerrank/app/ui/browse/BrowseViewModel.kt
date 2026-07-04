package com.hackerrank.app.ui.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BrowseUiState(
    val groupedStructures: Map<DataStructureCategory, List<DataStructure>> = emptyMap(),
    val isLoading: Boolean = true
)

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState

    init {
        loadStructures()
    }

    private fun loadStructures() {
        viewModelScope.launch {
            contentRepository.getAllStructures().collect { structures ->
                val grouped = structures.groupBy { it.category }
                _uiState.value = BrowseUiState(
                    groupedStructures = DataStructureCategory.entries
                        .mapNotNull { cat -> grouped[cat]?.let { cat to it } }
                        .toMap(),
                    isLoading = false
                )
            }
        }
    }
}
