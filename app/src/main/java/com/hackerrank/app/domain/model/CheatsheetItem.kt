package com.hackerrank.app.domain.model

data class CheatsheetItem(
    val id: String,
    val title: String,
    val category: ProblemCategory,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val summary: String,
    val whenToUse: List<String>,
    val keyTricks: List<String>,
    val codeTemplate: String,
    val timeComplexity: String,
    val spaceComplexity: String,
    val relatedProblemIds: List<String> = emptyList(),
)

data class CategoryGuide(
    val category: ProblemCategory,
    val title: String,
    val coreConcept: String,
    val timeSpaceTargets: String,
    val essentialKotlinTools: List<String>,
    val criticalEdgeCases: List<String>,
    val topPatterns: List<String>,
)

data class ComplexityRule(
    val constraint: String,
    val maxTimeComplexity: String,
    val algorithms: String,
)
