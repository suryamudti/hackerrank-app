package com.hackerrank.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackerrank.app.data.local.entity.QuizQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizQuestionDao {

    @Query("SELECT * FROM quiz_questions WHERE structure_id = :structureId ORDER BY RANDOM()")
    fun getQuestionsByStructureId(structureId: String): Flow<List<QuizQuestionEntity>>

    @Query("SELECT * FROM quiz_questions WHERE id = :id")
    suspend fun getQuestionById(id: String): QuizQuestionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuizQuestionEntity>)

    @Query("SELECT COUNT(*) FROM quiz_questions WHERE structure_id = :structureId")
    suspend fun countByStructureId(structureId: String): Int
}
