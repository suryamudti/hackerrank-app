package com.hackerrank.app.ui.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.usecase.ObserveBrowseDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val observeBrowseDataUseCase: ObserveBrowseDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BrowseUiState>(BrowseUiState.Loading)
    val uiState: StateFlow<BrowseUiState> = _uiState

    init {
        viewModelScope.launch {
            observeBrowseDataUseCase().collect { data ->
                _uiState.value = BrowseUiState.Loaded(
                    groupedStructures = data.groupedStructures,
                    progressMap = data.progressMap
                )
            }
        }
    }
}
