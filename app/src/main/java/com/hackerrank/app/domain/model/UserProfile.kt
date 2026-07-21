package com.hackerrank.app.domain.model

data class UserProfile(
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActiveDate: String? = null,
    val earnedBadgeIds: List<String> = emptyList(),
)
