package com.hackerrank.app.data.repository

import com.google.gson.Gson
import com.hackerrank.app.data.local.dao.QuizQuestionDao
import com.hackerrank.app.domain.model.QuizQuestion
import com.hackerrank.app.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepositoryImpl
    @Inject
    constructor(
        private val dao: QuizQuestionDao,
        private val gson: Gson,
    ) : QuizRepository {
        override fun getQuestionsByStructureId(structureId: String): Flow<List<QuizQuestion>> {
            return dao.getQuestionsByStructureId(structureId).map { entities ->
                entities.shuffled().map { it.toDomain(gson) }
            }
        }
    }
