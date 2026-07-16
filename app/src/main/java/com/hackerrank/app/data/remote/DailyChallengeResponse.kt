package com.hackerrank.app.data.remote

data class DailyChallengeResponse(
    val date: String,
    val problemId: String,
    val bonusXp: Int
)
