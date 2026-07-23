package com.hackerrank.app.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hackerrank.app.data.local.entity.DataStructureEntity
import com.hackerrank.app.data.local.entity.ProblemEntity
import com.hackerrank.app.data.local.entity.QuizQuestionEntity
import com.hackerrank.app.data.local.entity.UserProfileEntity
import com.hackerrank.app.data.local.entity.UserProgressEntity
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.model.QuizQuestion
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress

fun DataStructureEntity.toDomain(gson: Gson): DataStructure {
    val complexityType = object : TypeToken<Map<String, String>>() {}.type
    val complexityTable: Map<String, String> = gson.fromJson(complexityJson, complexityType) ?: emptyMap()

    val listType = object : TypeToken<List<String>>() {}.type
    val whenToUseList: List<String> = gson.fromJson(whenToUseJson, listType) ?: emptyList()

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
        difficulty =
            when {
                difficulty == "Easy" -> Difficulty.EASY
                difficulty == "Medium" -> Difficulty.MEDIUM
                else -> Difficulty.HARD
            },
    )
}

fun UserProgressEntity.toDomain(): UserProgress =
    UserProgress(
        structureId = structureId,
        quizzesCompleted = quizzesCompleted,
        totalCorrect = totalCorrect,
        totalQuestions = totalQuestions,
        bestScore = bestScore,
        masteryLevel = masteryLevel,
    )

fun UserProfileEntity.toDomain(gson: Gson): UserProfile {
    val listType = object : TypeToken<List<String>>() {}.type
    val badges: List<String> = gson.fromJson(earnedBadgeIdsJson, listType) ?: emptyList()

    return UserProfile(
        totalXp = totalXp,
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        lastActiveDate = lastActiveDate,
        earnedBadgeIds = badges,
    )
}

fun QuizQuestionEntity.toDomain(gson: Gson): QuizQuestion {
    val listType = object : TypeToken<List<String>>() {}.type
    val optionsList: List<String> = gson.fromJson(optionsJson, listType) ?: emptyList()

    return QuizQuestion(
        id = id,
        structureId = structureId,
        question = question,
        options = optionsList,
        correctIndex = correctIndex,
        explanation = explanation,
    )
}

fun ProblemEntity.toDomain(): Problem =
    Problem(
        id = id,
        title = title,
        description = description,
        inputExample = inputExample,
        outputExample = outputExample,
        solutionCode = solutionCode,
        approachExplanation = approachExplanation,
        difficulty =
            when (difficulty) {
                "Easy" -> Difficulty.EASY
                "Medium" -> Difficulty.MEDIUM
                "Hard" -> Difficulty.HARD
                else -> Difficulty.EASY
            },
        category =
            when (category) {
                "Arrays" -> ProblemCategory.ARRAYS
                "Strings" -> ProblemCategory.STRINGS
                "Linked Lists" -> ProblemCategory.LINKED_LISTS
                "Stacks & Queues" -> ProblemCategory.STACKS_QUEUES
                "Trees" -> ProblemCategory.TREES
                "Graphs" -> ProblemCategory.GRAPHS
                "Dynamic Programming" -> ProblemCategory.DYNAMIC_PROGRAMMING
                "Sorting & Searching" -> ProblemCategory.SORTING_SEARCHING
                "Hash-Based" -> ProblemCategory.HASH_BASED
                "Recursion & Math" -> ProblemCategory.RECURSION_MATH
                "Bit Manipulation" -> ProblemCategory.BIT_MANIPULATION
                else -> ProblemCategory.MISCELLANEOUS
            },
        orderIndex = orderIndex,
    )
