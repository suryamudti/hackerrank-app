package com.hackerrank.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_structures")
data class DataStructureEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "explanation") val explanation: String,
    @ColumnInfo(name = "complexity_json") val complexityJson: String,
    @ColumnInfo(name = "when_to_use_json") val whenToUseJson: String,
    @ColumnInfo(name = "diagram_res") val diagramRes: String?,
    @ColumnInfo(name = "code_example") val codeExample: String,
    @ColumnInfo(name = "difficulty") val difficulty: String
)
