package com.hackerrank.app.domain.model

data class CheatsheetItem(
    val id: String,
    val title: String,
    val category: CheatsheetCategory,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val summary: String,
    val whenToUse: List<String>,
    val keyTricks: List<String>,
    val codeTemplate: String,
    val timeComplexity: String,
    val spaceComplexity: String,
    val relatedProblemIds: List<String> = emptyList(),
)

enum class CheatsheetCategory(val displayName: String) {
    ARRAYS_STRINGS("Arrays & Strings"),
    TWO_POINTERS_SLIDING_WINDOW("Two Pointers & Window"),
    BINARY_SEARCH("Binary Search"),
    STACKS_QUEUES("Stacks & Queues"),
    TREES_GRAPHS("Trees & Graphs"),
    DYNAMIC_PROGRAMMING("Dynamic Programming"),
    BIT_MATH("Bit & Math"),
    BACKTRACKING_GREEDY("Backtracking & Greedy"),
}

data class ComplexityRule(
    val constraint: String,
    val maxTimeComplexity: String,
    val algorithms: String,
)
