package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class BadgeWithProgress(
    val badge: Badge,
    val isEarned: Boolean,
    val currentProgress: Int?,
    val targetProgress: Int?
)

class ObserveBadgesUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) {
    operator fun invoke(): Flow<List<BadgeWithProgress>> = flow {
        val profile = progressRepository.getProfile().first()
        val badges = BadgeDefinition.entries.map { def ->
            val isEarned = profile?.earnedBadgeIds?.contains(def.id) == true
            val (current, target) = when (def) {
                BadgeDefinition.STREAK_NOVICE -> (profile?.currentStreak ?: 0) to 3
                BadgeDefinition.STREAK_MASTER -> (profile?.currentStreak ?: 0) to 7
                BadgeDefinition.STREAK_LEGEND -> (profile?.currentStreak ?: 0) to 30
                BadgeDefinition.LEVEL_10 -> (profile?.totalXp ?: 0) to 10000
                BadgeDefinition.LEVEL_25 -> (profile?.totalXp ?: 0) to 62500
                BadgeDefinition.LEVEL_50 -> (profile?.totalXp ?: 0) to 250000
                else -> null to null
            }
            BadgeWithProgress(
                badge = Badge(def.id, def.title, def.description),
                isEarned = isEarned,
                currentProgress = current,
                targetProgress = target
            )
        }
        emit(badges)
    }
}
