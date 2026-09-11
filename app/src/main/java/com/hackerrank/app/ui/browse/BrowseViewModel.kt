package com.hackerrank.app.ui.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.usecase.ObserveBrowseDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BrowseUiState {
    data object Loading : BrowseUiState

    data class Error(val message: String) : BrowseUiState

    data class Loaded(
        val groupedStructures: Map<DataStructureCategory, List<DataStructure>>,
        val progressMap: Map<String, Float>,
        val searchQuery: String = "",
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

        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery

        init {
            loadData()
        }

        private fun loadData() {
            viewModelScope.launch {
                try {
                    combine(
                        observeBrowseDataUseCase(),
                        _searchQuery,
                    ) { data, query ->
                        val filteredGrouped =
                            if (query.isBlank()) {
                                data.groupedStructures
                            } else {
                                data.groupedStructures.mapValues { (_, structures) ->
                                    structures.filter { s ->
                                        s.name.contains(query, ignoreCase = true) ||
                                            s.explanation.contains(query, ignoreCase = true)
                                    }
                                }.filterValues { it.isNotEmpty() }
                            }

                        BrowseUiState.Loaded(
                            groupedStructures = filteredGrouped,
                            progressMap = data.progressMap,
                            searchQuery = query,
                        )
                    }.collect { state ->
                        _uiState.value = state
                    }
                } catch (e: Exception) {
                    _uiState.value = BrowseUiState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }

        fun onSearchQueryChanged(query: String) {
            _searchQuery.value = query
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
