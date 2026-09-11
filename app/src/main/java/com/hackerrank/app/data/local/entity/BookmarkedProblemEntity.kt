package com.hackerrank.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_problems")
data class BookmarkedProblemEntity(
    @PrimaryKey val problemId: String,
    val bookmarkedAt: Long = System.currentTimeMillis(),
)
