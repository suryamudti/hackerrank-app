package com.hackerrank.app.domain.gamification

import com.hackerrank.app.core.Constants
import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.StreakInfo
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamificationEngine @Inject constructor(
    private val progressRepository: ProgressRepository
) {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    suspend fun recordQuizComplete(
        score: Int,
        totalQuestions: Int,
        elapsedTimeMs: Long,
        structureId: String
    ): GamificationResult {
        val profile = progressRepository.getProfileSync() ?: UserProfile()
        val isPerfect = score == totalQuestions

        // Calculate XP
        var xpGained = Constants.BASE_QUIZ_XP
        if (isPerfect) xpGained += Constants.PERFECT_SCORE_BONUS
        if (elapsedTimeMs < Constants.SPEED_BONUS_THRESHOLD_MS) {
            xpGained += Constants.SPEED_BONUS_MAX
        }
        xpGained += Constants.DAILY_QUIZ_XP

        // Update streak
        val streakResult = updateStreak(profile)
        if (streakResult.streakMilestoneReached != null) {
            xpGained += Constants.streakMilestones[streakResult.streakMilestoneReached] ?: 0
        }

        val oldLevel = Constants.getLevel(profile.totalXp)
        val newTotalXp = profile.totalXp + xpGained
        val newLevel = Constants.getLevel(newTotalXp)

        // Evaluate badges
        val allProgress = progressRepository.getAllProgress().first()
        val currentBadges = profile.earnedBadgeIds.toMutableSet()
        val newBadges = evaluateBadges(
            profile = profile.copy(totalXp = newTotalXp, currentStreak = streakResult.currentStreak),
            isPerfect = isPerfect,
            elapsedTimeMs = elapsedTimeMs,
            structureId = structureId,
            allProgress = allProgress,
            earnedBadgeIds = currentBadges
        )
        currentBadges.addAll(newBadges.map { it.id })

        // Persist
        val updatedProfile = profile.copy(
            totalXp = newTotalXp,
            currentStreak = streakResult.currentStreak,
            longestStreak = streakResult.longestStreak,
            lastActiveDate = streakResult.lastActiveDate,
            earnedBadgeIds = currentBadges.toList()
        )
        progressRepository.upsertProfile(updatedProfile)

        return GamificationResult(
            xpAwarded = xpGained,
            newTotalXp = newTotalXp,
            newLevel = newLevel,
            previousLevel = oldLevel,
            newBadges = newBadges,
            streakInfo = streakResult
        )
    }

    suspend fun recordLogin(): GamificationResult {
        val profile = progressRepository.getProfileSync() ?: UserProfile()
        val streakResult = updateStreak(profile)
        val oldLevel = Constants.getLevel(profile.totalXp)
        val newTotalXp = profile.totalXp + Constants.DAILY_LOGIN_XP
        val newLevel = Constants.getLevel(newTotalXp)

        val updatedProfile = profile.copy(
            totalXp = newTotalXp,
            currentStreak = streakResult.currentStreak,
            longestStreak = streakResult.longestStreak,
            lastActiveDate = streakResult.lastActiveDate
        )
        progressRepository.upsertProfile(updatedProfile)

        return GamificationResult(
            xpAwarded = Constants.DAILY_LOGIN_XP,
            newTotalXp = newTotalXp,
            newLevel = newLevel,
            previousLevel = oldLevel,
            newBadges = emptyList(),
            streakInfo = streakResult
        )
    }

    private fun updateStreak(profile: UserProfile): StreakInfo {
        val today = LocalDate.now()
        val todayStr = today.format(dateFormatter)

        if (profile.lastActiveDate == todayStr) {
            return StreakInfo(
                currentStreak = profile.currentStreak,
                longestStreak = profile.longestStreak,
                isNewStreakDay = false,
                streakMilestoneReached = null,
                lastActiveDate = todayStr
            )
        }

        val yesterday = today.minusDays(1)
        val yesterdayStr = yesterday.format(dateFormatter)

        val newStreak = if (profile.lastActiveDate == yesterdayStr) {
            profile.currentStreak + 1
        } else {
            1
        }

        val newLongestStreak = maxOf(profile.longestStreak, newStreak)

        // Check streak milestones
        val milestoneReached = Constants.streakMilestones.keys.find { it == newStreak }

        return StreakInfo(
            currentStreak = newStreak,
            longestStreak = newLongestStreak,
            isNewStreakDay = true,
            streakMilestoneReached = milestoneReached,
            lastActiveDate = todayStr
        )
    }

    private suspend fun evaluateBadges(
        profile: UserProfile,
        isPerfect: Boolean,
        elapsedTimeMs: Long,
        structureId: String,
        allProgress: List<com.hackerrank.app.domain.model.UserProgress>,
        earnedBadgeIds: Set<String>
    ): List<Badge> {
        val newBadges = mutableListOf<Badge>()

        if (BadgeDefinition.FIRST_STEPS.id !in earnedBadgeIds &&
            allProgress.any { it.quizzesCompleted > 0 }
        ) {
            newBadges.add(Badge(
                BadgeDefinition.FIRST_STEPS.id,
                BadgeDefinition.FIRST_STEPS.title,
                BadgeDefinition.FIRST_STEPS.description
            ))
        }

        if (BadgeDefinition.QUICK_LEARNER.id !in earnedBadgeIds && isPerfect) {
            newBadges.add(Badge(
                BadgeDefinition.QUICK_LEARNER.id,
                BadgeDefinition.QUICK_LEARNER.title,
                BadgeDefinition.QUICK_LEARNER.description
            ))
        }

        if (BadgeDefinition.SPEED_DEMON.id !in earnedBadgeIds &&
            elapsedTimeMs < Constants.SPEED_BONUS_THRESHOLD_MS
        ) {
            newBadges.add(Badge(
                BadgeDefinition.SPEED_DEMON.id,
                BadgeDefinition.SPEED_DEMON.title,
                BadgeDefinition.SPEED_DEMON.description
            ))
        }

        if (BadgeDefinition.STREAK_NOVICE.id !in earnedBadgeIds && profile.currentStreak >= 3) {
            newBadges.add(Badge(
                BadgeDefinition.STREAK_NOVICE.id,
                BadgeDefinition.STREAK_NOVICE.title,
                BadgeDefinition.STREAK_NOVICE.description
            ))
        }

        if (BadgeDefinition.STREAK_MASTER.id !in earnedBadgeIds && profile.currentStreak >= 7) {
            newBadges.add(Badge(
                BadgeDefinition.STREAK_MASTER.id,
                BadgeDefinition.STREAK_MASTER.title,
                BadgeDefinition.STREAK_MASTER.description
            ))
        }

        if (BadgeDefinition.STREAK_LEGEND.id !in earnedBadgeIds && profile.currentStreak >= 30) {
            newBadges.add(Badge(
                BadgeDefinition.STREAK_LEGEND.id,
                BadgeDefinition.STREAK_LEGEND.title,
                BadgeDefinition.STREAK_LEGEND.description
            ))
        }

        if (BadgeDefinition.LEVEL_10.id !in earnedBadgeIds && profile.totalXp >= 10_000) {
            newBadges.add(Badge(
                BadgeDefinition.LEVEL_10.id,
                BadgeDefinition.LEVEL_10.title,
                BadgeDefinition.LEVEL_10.description
            ))
        }

        if (BadgeDefinition.LEVEL_25.id !in earnedBadgeIds && profile.totalXp >= 62_500) {
            newBadges.add(Badge(
                BadgeDefinition.LEVEL_25.id,
                BadgeDefinition.LEVEL_25.title,
                BadgeDefinition.LEVEL_25.description
            ))
        }

        if (BadgeDefinition.LEVEL_50.id !in earnedBadgeIds && profile.totalXp >= 250_000) {
            newBadges.add(Badge(
                BadgeDefinition.LEVEL_50.id,
                BadgeDefinition.LEVEL_50.title,
                BadgeDefinition.LEVEL_50.description
            ))
        }

        return newBadges
    }
}
