package com.hackerrank.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.usecase.ObserveStructureDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data class Error(val message: String) : DetailUiState

    data class Loaded(
        val structure: DataStructure,
        val progress: UserProgress?,
    ) : DetailUiState
}

@HiltViewModel
class DetailViewModel
    @Inject
    constructor(
        private val observeStructureDetailUseCase: ObserveStructureDetailUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
        val uiState: StateFlow<DetailUiState> = _uiState

        fun loadStructure(slug: String) {
            viewModelScope.launch {
                try {
                    observeStructureDetailUseCase(slug).collect { data ->
                        if (data.structure != null) {
                            _uiState.value =
                                DetailUiState.Loaded(
                                    structure = data.structure,
                                    progress = data.progress,
                                )
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = DetailUiState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }
    }
