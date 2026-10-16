package com.hackerrank.app.domain.repository

import com.hackerrank.app.domain.model.DailyChallenge
import kotlinx.coroutines.flow.Flow

interface DailyChallengeRepository {
    fun getDailyChallengeState(): Flow<DailyChallenge?>

    suspend fun getDailyChallenge(today: String): DailyChallenge?

    suspend fun cacheDailyChallenge(challenge: DailyChallenge)

    suspend fun setDailyChallengeCompleted(date: String)

    suspend fun isDailyChallengeCompleted(date: String): Boolean
}
