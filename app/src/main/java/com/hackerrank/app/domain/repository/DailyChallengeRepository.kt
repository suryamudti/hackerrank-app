package com.hackerrank.app.domain.repository

import com.hackerrank.app.data.remote.DailyChallengeResponse
import kotlinx.coroutines.flow.Flow

interface DailyChallengeRepository {
    fun getDailyChallengeState(): Flow<DailyChallengeResponse?>

    suspend fun cacheDailyChallengeResponse(response: DailyChallengeResponse)

    suspend fun setDailyChallengeCompleted(date: String)

    suspend fun isDailyChallengeCompleted(date: String): Boolean
}
