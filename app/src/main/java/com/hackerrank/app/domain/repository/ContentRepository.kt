package com.hackerrank.app.domain.repository

import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun getAllStructures(): Flow<List<DataStructure>>
    fun getStructuresByCategory(category: DataStructureCategory): Flow<List<DataStructure>>
    suspend fun getStructureBySlug(slug: String): DataStructure?
    fun getAllCategories(): Flow<List<DataStructureCategory>>
}
