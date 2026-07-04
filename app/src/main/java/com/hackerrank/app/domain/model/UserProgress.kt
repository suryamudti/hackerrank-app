package com.hackerrank.app.domain.model

data class UserProgress(
    val structureId: String,
    val quizzesCompleted: Int = 0,
    val totalCorrect: Int = 0,
    val totalQuestions: Int = 0,
    val bestScore: Int = 0,
    val masteryLevel: Int = 0
) {
    val masteryPercentage: Float
        get() = if (totalQuestions > 0) (totalCorrect.toFloat() / totalQuestions.toFloat()) * 100f else 0f
}
