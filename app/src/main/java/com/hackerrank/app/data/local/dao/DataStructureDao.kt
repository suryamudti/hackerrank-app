package com.hackerrank.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackerrank.app.data.local.entity.DataStructureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DataStructureDao {

    @Query("SELECT * FROM data_structures ORDER BY name ASC")
    fun getAllStructures(): Flow<List<DataStructureEntity>>

    @Query("SELECT * FROM data_structures WHERE category = :category ORDER BY name ASC")
    fun getStructuresByCategory(category: String): Flow<List<DataStructureEntity>>

    @Query("SELECT * FROM data_structures WHERE slug = :slug")
    suspend fun getStructureBySlug(slug: String): DataStructureEntity?

    @Query("SELECT * FROM data_structures WHERE id = :id")
    suspend fun getStructureById(id: String): DataStructureEntity?

    @Query("SELECT DISTINCT category FROM data_structures ORDER BY CASE " +
            "WHEN category = 'Linear' THEN 1 " +
            "WHEN category = 'Trees' THEN 2 " +
            "WHEN category = 'Graphs' THEN 3 " +
            "WHEN category = 'Hash-Based' THEN 4 " +
            "WHEN category = 'Other' THEN 5 END")
    fun getAllCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(structures: List<DataStructureEntity>)

    @Query("SELECT COUNT(*) FROM data_structures")
    suspend fun count(): Int
}
