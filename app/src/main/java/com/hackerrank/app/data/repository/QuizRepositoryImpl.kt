package com.hackerrank.app.data.repository

import com.hackerrank.app.data.local.dao.QuizQuestionDao
import com.hackerrank.app.domain.model.QuizQuestion
import com.hackerrank.app.domain.repository.QuizRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepositoryImpl @Inject constructor(
    private val dao: QuizQuestionDao
) : QuizRepository {

    private val gson = Gson()

    override fun getQuestionsByStructureId(structureId: String): Flow<List<QuizQuestion>> {
        return dao.getQuestionsByStructureId(structureId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun com.hackerrank.app.data.local.entity.QuizQuestionEntity.toDomain(): QuizQuestion {
        val listType = object : TypeToken<List<String>>() {}.type
        val optionsList: List<String> = gson.fromJson(optionsJson, listType)

        return QuizQuestion(
            id = id,
            structureId = structureId,
            question = question,
            options = optionsList,
            correctIndex = correctIndex,
            explanation = explanation
        )
    }
}
