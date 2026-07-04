package com.hackerrank.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackerrank.app.data.local.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Query("SELECT * FROM user_progress WHERE structure_id = :structureId")
    fun getProgressByStructureId(structureId: String): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<UserProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: UserProgressEntity)

    @Query("SELECT COUNT(*) FROM user_progress WHERE mastery_level >= 100")
    fun getMasteredCount(): Flow<Int>
}
