package com.hackerrank.app.data.remote

import com.hackerrank.app.domain.model.DailyChallenge

data class DailyChallengeResponse(
    val date: String,
    val problemId: String,
    val bonusXp: Int,
) {
    fun toDomain(): DailyChallenge =
        DailyChallenge(
            date = date,
            problemId = problemId,
            bonusXp = bonusXp,
        )
}
