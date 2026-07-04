package com.hackerrank.app.data.repository

import com.hackerrank.app.data.local.dao.DataStructureDao
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.repository.ContentRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepositoryImpl @Inject constructor(
    private val dao: DataStructureDao
) : ContentRepository {

    private val gson = Gson()

    override fun getAllStructures(): Flow<List<DataStructure>> {
        return dao.getAllStructures().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getStructuresByCategory(category: DataStructureCategory): Flow<List<DataStructure>> {
        return dao.getStructuresByCategory(category.displayName).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getStructureBySlug(slug: String): DataStructure? {
        return dao.getStructureBySlug(slug)?.toDomain()
    }

    override fun getAllCategories(): Flow<List<DataStructureCategory>> {
        return dao.getAllCategories().map { categories ->
            categories.mapNotNull { name ->
                DataStructureCategory.entries.find { it.displayName == name }
            }
        }
    }

    private fun com.hackerrank.app.data.local.entity.DataStructureEntity.toDomain(): DataStructure {
        val complexityType = object : TypeToken<Map<String, String>>() {}.type
        val complexityTable: Map<String, String> = gson.fromJson(complexityJson, complexityType)

        val listType = object : TypeToken<List<String>>() {}.type
        val whenToUseList: List<String> = gson.fromJson(whenToUseJson, listType)

        return DataStructure(
            id = id,
            name = name,
            slug = slug,
            category = DataStructureCategory.entries.find { it.displayName == category } ?: DataStructureCategory.OTHER,
            explanation = explanation,
            complexityTable = complexityTable,
            whenToUse = whenToUseList,
            diagramRes = diagramRes,
            codeExample = codeExample,
            difficulty = when {
                difficulty == "Easy" -> Difficulty.EASY
                difficulty == "Medium" -> Difficulty.MEDIUM
                else -> Difficulty.HARD
            }
        )
    }
}
