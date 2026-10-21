package com.hackerrank.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackerrank.app.data.local.entity.BookmarkedCheatsheetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedCheatsheetDao {
    @Query("SELECT cheatsheetId FROM bookmarked_cheatsheets ORDER BY bookmarkedAt DESC")
    fun getBookmarkedIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_cheatsheets WHERE cheatsheetId = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_cheatsheets WHERE cheatsheetId = :id)")
    suspend fun isBookmarkedSync(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BookmarkedCheatsheetEntity)

    @Query("DELETE FROM bookmarked_cheatsheets WHERE cheatsheetId = :id")
    suspend fun delete(id: String)
}
