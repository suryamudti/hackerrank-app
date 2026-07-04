package com.hackerrank.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackerrank.app.data.local.entity.ProblemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemDao {

    @Query("SELECT * FROM problems ORDER BY orderIndex ASC")
    fun getAllProblems(): Flow<List<ProblemEntity>>

    @Query("SELECT * FROM problems WHERE category = :category ORDER BY orderIndex ASC")
    fun getProblemsByCategory(category: String): Flow<List<ProblemEntity>>

    @Query("SELECT * FROM problems WHERE id = :id")
    fun getProblemById(id: String): Flow<ProblemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(problems: List<ProblemEntity>)

    @Query("SELECT COUNT(*) FROM problems")
    fun count(): Flow<Int>
}
