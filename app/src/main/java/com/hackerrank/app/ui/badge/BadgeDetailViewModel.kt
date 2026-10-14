package com.hackerrank.app.ui.badge

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.R
import com.hackerrank.app.domain.usecase.BadgeWithProgress
import com.hackerrank.app.domain.usecase.ObserveBadgesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BadgeDetailUiState {
    data object Loading : BadgeDetailUiState

    data class Error(val message: String) : BadgeDetailUiState

    data class Loaded(
        val badgeProgress: BadgeWithProgress,
    ) : BadgeDetailUiState
}

@HiltViewModel
class BadgeDetailViewModel
    @Inject
    constructor(
        private val observeBadgesUseCase: ObserveBadgesUseCase,
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<BadgeDetailUiState>(BadgeDetailUiState.Loading)
        val uiState: StateFlow<BadgeDetailUiState> = _uiState

        fun loadBadge(badgeId: String) {
            viewModelScope.launch {
                try {
                    observeBadgesUseCase().collect { list ->
                        val badge = list.find { it.badge.id == badgeId }
                        if (badge != null) {
                            _uiState.value = BadgeDetailUiState.Loaded(badge)
                        } else {
                            _uiState.value = BadgeDetailUiState.Error(context.getString(R.string.badge_not_found))
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = BadgeDetailUiState.Error(e.localizedMessage ?: context.getString(R.string.error_unknown))
                }
            }
        }
    }
