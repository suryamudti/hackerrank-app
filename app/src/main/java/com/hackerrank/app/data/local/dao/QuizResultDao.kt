package com.hackerrank.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackerrank.app.data.local.entity.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Query(
        """
        SELECT r.id, r.structure_id as structureId, ds.name as structureName, r.score, r.total_questions as totalQuestions, r.xp_earned as xpEarned, r.completed_at as completedAt
        FROM quiz_results r
        LEFT JOIN data_structures ds ON r.structure_id = ds.id
        ORDER BY r.completed_at DESC
        LIMIT 5
    """,
    )
    fun getRecentActivities(): Flow<List<RecentActivityDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: QuizResultEntity)
}

data class RecentActivityDb(
    val id: String,
    val structureId: String,
    val structureName: String?,
    val score: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val completedAt: Long,
)
