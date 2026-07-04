package com.hackerrank.app.domain.model

data class DataStructure(
    val id: String,
    val name: String,
    val slug: String,
    val category: DataStructureCategory,
    val explanation: String,
    val complexityTable: Map<String, String>,
    val whenToUse: List<String>,
    val diagramRes: String?,
    val codeExample: String,
    val difficulty: Difficulty
)

enum class DataStructureCategory(val displayName: String) {
    LINEAR("Linear"),
    TREES("Trees"),
    GRAPHS("Graphs"),
    HASH_BASED("Hash-Based"),
    OTHER("Other")
}

enum class Difficulty(val displayName: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard")
}
