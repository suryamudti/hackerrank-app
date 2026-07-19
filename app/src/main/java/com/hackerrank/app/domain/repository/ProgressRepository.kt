package com.hackerrank.app.domain.repository

import com.hackerrank.app.data.remote.DailyChallengeResponse
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun getProgressByStructureId(structureId: String): Flow<UserProgress?>
    fun getAllProgress(): Flow<List<UserProgress>>
    suspend fun upsertProgress(progress: UserProgress)
    fun getProfile(): Flow<UserProfile?>
    suspend fun getProfileSync(): UserProfile?
    suspend fun upsertProfile(profile: UserProfile)
    fun getMasteredCount(): Flow<Int>
    fun getDailyChallengeState(): Flow<DailyChallengeResponse?>
    suspend fun cacheDailyChallengeResponse(response: DailyChallengeResponse)
    suspend fun setDailyChallengeCompleted(date: String)
    suspend fun isDailyChallengeCompleted(date: String): Boolean
}
