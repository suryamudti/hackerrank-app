package com.hackerrank.app.domain.model

data class GamificationResult(
    val xpAwarded: Int,
    val newTotalXp: Int,
    val newLevel: Int,
    val previousLevel: Int,
    val newBadges: List<Badge>,
    val streakInfo: StreakInfo
)

data class StreakInfo(
    val currentStreak: Int,
    val longestStreak: Int,
    val isNewStreakDay: Boolean,
    val streakMilestoneReached: Int?, // 3, 7, 14, or 30 if milestone reached
    val lastActiveDate: String? = null
)

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val iconRes: String? = null
)
