package com.hackerrank.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "structure_id") val structureId: String,
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "total_questions") val totalQuestions: Int,
    @ColumnInfo(name = "xp_earned") val xpEarned: Int,
    @ColumnInfo(name = "completed_at") val completedAt: Long,
)
