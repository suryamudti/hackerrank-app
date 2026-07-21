package com.hackerrank.app.data.repository

import com.google.gson.Gson
import com.hackerrank.app.data.local.dao.DataStructureDao
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepositoryImpl
    @Inject
    constructor(
        private val dao: DataStructureDao,
        private val gson: Gson,
    ) : ContentRepository {
        override fun getAllStructures(): Flow<List<DataStructure>> {
            return dao.getAllStructures().map { entities ->
                entities.map { it.toDomain(gson) }
            }
        }

        override fun getStructuresByCategory(category: DataStructureCategory): Flow<List<DataStructure>> {
            return dao.getStructuresByCategory(category.displayName).map { entities ->
                entities.map { it.toDomain(gson) }
            }
        }

        override suspend fun getStructureBySlug(slug: String): DataStructure? {
            return dao.getStructureBySlug(slug)?.toDomain(gson)
        }

        override fun getAllCategories(): Flow<List<DataStructureCategory>> {
            return dao.getAllCategories().map { categories ->
                categories.mapNotNull { name ->
                    DataStructureCategory.entries.find { it.displayName == name }
                }
            }
        }
    }
