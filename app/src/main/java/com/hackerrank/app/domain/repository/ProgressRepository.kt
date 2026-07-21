package com.hackerrank.app.domain.repository

import com.hackerrank.app.domain.model.RecentActivity
import com.hackerrank.app.domain.model.UserProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun getProgressByStructureId(structureId: String): Flow<UserProgress?>

    fun getAllProgress(): Flow<List<UserProgress>>

    suspend fun getAllProgressSync(): List<UserProgress>

    suspend fun upsertProgress(progress: UserProgress)

    fun getRecentActivities(): Flow<List<RecentActivity>>

    suspend fun insertQuizResult(
        structureId: String,
        score: Int,
        totalQuestions: Int,
        xpEarned: Int,
    )
}
