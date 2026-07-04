package com.hackerrank.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "problems")
data class ProblemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val solutionCode: String,
    val approachExplanation: String,
    val difficulty: String,
    val category: String,
    val orderIndex: Int
)
