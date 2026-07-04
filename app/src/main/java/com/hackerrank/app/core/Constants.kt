package com.hackerrank.app.core

object Constants {
    // XP & Levels
    const val BASE_QUIZ_XP = 50
    const val PERFECT_SCORE_BONUS = 50
    const val DAILY_LOGIN_XP = 10
    const val DAILY_QUIZ_XP = 20
    const val SPEED_BONUS_MAX = 20
    const val SPEED_BONUS_THRESHOLD_MS = 60_000L // 60 seconds

    // Streak Milestone XP
    val streakMilestones = mapOf(
        3 to 100,
        7 to 250,
        14 to 350,
        30 to 500
    )

    // Level formula: level = floor(sqrt(totalXP / 100))
    fun getLevel(totalXp: Int): Int = kotlin.math.floor(kotlin.math.sqrt((totalXp / 100).toDouble())).toInt()

    fun getXpForNextLevel(level: Int): Int = (level + 1) * (level + 1) * 100

    fun getXpProgress(totalXp: Int): Pair<Int, Int> {
        val level = getLevel(totalXp)
        val currentLevelXp = level * level * 100
        val nextLevelXp = (level + 1) * (level + 1) * 100
        return Pair(totalXp - currentLevelXp, nextLevelXp - currentLevelXp)
    }

    // Problem XP
    const val PROBLEM_EASY_XP = 10
    const val PROBLEM_MEDIUM_XP = 25
    const val PROBLEM_HARD_XP = 50

    // Category order for display
    val categoryOrder = listOf(
        "Linear", "Trees", "Graphs", "Hash-Based", "Other"
    )

    // Quiz config
    const val QUESTIONS_PER_QUIZ = 8
}
