package com.hackerrank.app.domain.model

data class Problem(
    val id: String,
    val title: String,
    val description: String,
    val inputExample: String,
    val outputExample: String,
    val solutionCode: String,
    val approachExplanation: String,
    val difficulty: Difficulty,
    val category: ProblemCategory,
    val orderIndex: Int
)

enum class ProblemCategory(val displayName: String) {
    ARRAYS("Arrays"),
    STRINGS("Strings"),
    LINKED_LISTS("Linked Lists"),
    STACKS_QUEUES("Stacks & Queues"),
    TREES("Trees"),
    GRAPHS("Graphs"),
    DYNAMIC_PROGRAMMING("Dynamic Programming"),
    SORTING_SEARCHING("Sorting & Searching"),
    HASH_BASED("Hash-Based"),
    RECURSION_MATH("Recursion & Math"),
    BIT_MANIPULATION("Bit Manipulation"),
    MISCELLANEOUS("Miscellaneous")
}
