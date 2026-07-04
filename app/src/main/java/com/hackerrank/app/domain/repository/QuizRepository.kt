package com.hackerrank.app.domain.repository

import com.hackerrank.app.domain.model.QuizQuestion
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getQuestionsByStructureId(structureId: String): Flow<List<QuizQuestion>>
}
