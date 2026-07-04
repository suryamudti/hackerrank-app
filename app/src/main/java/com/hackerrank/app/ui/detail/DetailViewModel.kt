package com.hackerrank.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val structure: DataStructure? = null,
    val progress: UserProgress? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun loadStructure(slug: String) {
        viewModelScope.launch {
            val structure = contentRepository.getStructureBySlug(slug)
            if (structure != null) {
                combine(
                    progressRepository.getProgressByStructureId(structure.id),
                    progressRepository.getProfile()
                ) { progress, _ ->
                    _uiState.value = DetailUiState(
                        structure = structure,
                        progress = progress,
                        isLoading = false
                    )
                }.collect {}
            }
        }
    }
}
