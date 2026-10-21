package com.hackerrank.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_cheatsheets")
data class BookmarkedCheatsheetEntity(
    @PrimaryKey val cheatsheetId: String,
    val bookmarkedAt: Long = System.currentTimeMillis(),
)
