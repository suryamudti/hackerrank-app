package com.hackerrank.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    @ColumnInfo(name = "structure_id") val structureId: String,
    @ColumnInfo(name = "quizzes_completed") val quizzesCompleted: Int = 0,
    @ColumnInfo(name = "total_correct") val totalCorrect: Int = 0,
    @ColumnInfo(name = "total_questions") val totalQuestions: Int = 0,
    @ColumnInfo(name = "best_score") val bestScore: Int = 0,
    @ColumnInfo(name = "mastery_level") val masteryLevel: Int = 0
)
