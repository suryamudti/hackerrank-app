package com.hackerrank.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackerrank.app.data.local.entity.BookmarkedProblemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedProblemDao {
    @Query("SELECT problemId FROM bookmarked_problems")
    fun getBookmarkedIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_problems WHERE problemId = :problemId)")
    fun isBookmarked(problemId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_problems WHERE problemId = :problemId)")
    suspend fun isBookmarkedSync(problemId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmarked: BookmarkedProblemEntity)

    @Query("DELETE FROM bookmarked_problems WHERE problemId = :problemId")
    suspend fun delete(problemId: String)
}
