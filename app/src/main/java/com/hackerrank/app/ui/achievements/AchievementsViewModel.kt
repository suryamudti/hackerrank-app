package com.hackerrank.app.ui.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BadgeWithState(
    val badge: Badge,
    val isEarned: Boolean,
    val progress: String = ""
)

sealed interface AchievementsUiState {
    data object Loading : AchievementsUiState
    data class Loaded(val badges: List<BadgeWithState>) : AchievementsUiState
}

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AchievementsUiState>(AchievementsUiState.Loading)
    val uiState: StateFlow<AchievementsUiState> = _uiState

    init {
        loadBadges()
    }

    private fun loadBadges() {
        viewModelScope.launch {
            val profile = progressRepository.getProfile().first()

            val badges = BadgeDefinition.entries.map { def ->
                val isEarned = profile?.earnedBadgeIds?.contains(def.id) == true
                val progress = when (def) {
                    BadgeDefinition.STREAK_NOVICE -> "${profile?.currentStreak ?: 0}/3"
                    BadgeDefinition.STREAK_MASTER -> "${profile?.currentStreak ?: 0}/7"
                    BadgeDefinition.STREAK_LEGEND -> "${profile?.currentStreak ?: 0}/30"
                    BadgeDefinition.LEVEL_10 -> "${profile?.totalXp ?: 0}/10000"
                    BadgeDefinition.LEVEL_25 -> "${profile?.totalXp ?: 0}/62500"
                    BadgeDefinition.LEVEL_50 -> "${profile?.totalXp ?: 0}/250000"
                    else -> ""
                }
                BadgeWithState(
                    badge = Badge(def.id, def.title, def.description),
                    isEarned = isEarned,
                    progress = if (progress.isNotEmpty() && !isEarned) progress else ""
                )
            }
            _uiState.value = AchievementsUiState.Loaded(badges = badges)
        }
    }
}
