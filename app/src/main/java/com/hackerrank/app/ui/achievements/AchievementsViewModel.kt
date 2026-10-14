package com.hackerrank.app.ui.achievements

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.R
import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.usecase.ObserveBadgesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BadgeWithState(
    val badge: Badge,
    val isEarned: Boolean,
    val progress: String = "",
)

sealed interface AchievementsUiState {
    data object Loading : AchievementsUiState

    data class Error(val message: String) : AchievementsUiState

    data class Loaded(val badges: List<BadgeWithState>) : AchievementsUiState
}

@HiltViewModel
class AchievementsViewModel
    @Inject
    constructor(
        private val observeBadgesUseCase: ObserveBadgesUseCase,
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<AchievementsUiState>(AchievementsUiState.Loading)
        val uiState: StateFlow<AchievementsUiState> = _uiState

        init {
            loadBadges()
        }

        private fun loadBadges() {
            viewModelScope.launch {
                try {
                    observeBadgesUseCase().collect { badges ->
                        _uiState.value =
                            AchievementsUiState.Loaded(
                                badges =
                                    badges.map { bp ->
                                        BadgeWithState(
                                            badge = bp.badge,
                                            isEarned = bp.isEarned,
                                            progress =
                                                if (bp.currentProgress != null && bp.targetProgress != null && !bp.isEarned) {
                                                    "${bp.currentProgress}/${bp.targetProgress}"
                                                } else {
                                                    ""
                                                },
                                        )
                                    },
                            )
                    }
                } catch (e: Exception) {
                    _uiState.value = AchievementsUiState.Error(e.localizedMessage ?: context.getString(R.string.error_unknown))
                }
            }
        }
    }
