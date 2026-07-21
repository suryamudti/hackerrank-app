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

    data class Error(val message: String) : BrowseUiState

    data class Loaded(
        val groupedStructures: Map<DataStructureCategory, List<DataStructure>>,
        val progressMap: Map<String, Float>,
    ) : BrowseUiState
}

@HiltViewModel
class BrowseViewModel
    @Inject
    constructor(
        private val observeBrowseDataUseCase: ObserveBrowseDataUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<BrowseUiState>(BrowseUiState.Loading)
        val uiState: StateFlow<BrowseUiState> = _uiState

        private val _isRefreshing = MutableStateFlow(false)
        val isRefreshing: StateFlow<Boolean> = _isRefreshing

        init {
            loadData()
        }

        private fun loadData() {
            viewModelScope.launch {
                try {
                    observeBrowseDataUseCase().collect { data ->
                        _uiState.value =
                            BrowseUiState.Loaded(
                                groupedStructures = data.groupedStructures,
                                progressMap = data.progressMap,
                            )
                    }
                } catch (e: Exception) {
                    _uiState.value = BrowseUiState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }

        fun refresh() {
            viewModelScope.launch {
                _isRefreshing.value = true
                try {
                    kotlinx.coroutines.delay(1000)
                    loadData()
                } catch (e: Exception) {
                    _uiState.value = BrowseUiState.Error(e.localizedMessage ?: "Unknown error")
                } finally {
                    _isRefreshing.value = false
                }
            }
        }
    }
