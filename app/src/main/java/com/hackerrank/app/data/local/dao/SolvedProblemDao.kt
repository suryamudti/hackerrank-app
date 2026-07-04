package com.hackerrank.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackerrank.app.data.local.entity.SolvedProblemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SolvedProblemDao {

    @Query("SELECT problemId FROM solved_problems")
    fun getSolvedIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM solved_problems WHERE problemId = :problemId)")
    fun isSolved(problemId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(solved: SolvedProblemEntity)
}
