package com.hackerrank.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solved_problems")
data class SolvedProblemEntity(
    @PrimaryKey val problemId: String,
    val solvedAt: Long = System.currentTimeMillis(),
)
