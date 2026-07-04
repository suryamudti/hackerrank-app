package com.hackerrank.app.data.repository

import com.hackerrank.app.data.local.dao.ProblemDao
import com.hackerrank.app.data.local.dao.SolvedProblemDao
import com.hackerrank.app.data.local.entity.SolvedProblemEntity
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.repository.ProblemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProblemRepositoryImpl @Inject constructor(
    private val problemDao: ProblemDao,
    private val solvedProblemDao: SolvedProblemDao
) : ProblemRepository {

    override fun getAllProblems(): Flow<List<Problem>> =
        problemDao.getAllProblems().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getProblemById(id: String): Flow<Problem?> =
        problemDao.getProblemById(id).map { it?.toDomain() }

    override fun getSolvedIds(): Flow<Set<String>> =
        solvedProblemDao.getSolvedIds().map { it.toSet() }

    override fun isSolved(problemId: String): Flow<Boolean> =
        solvedProblemDao.isSolved(problemId)

    override suspend fun markAsSolved(problemId: String) {
        solvedProblemDao.insert(SolvedProblemEntity(problemId = problemId))
    }

    private fun com.hackerrank.app.data.local.entity.ProblemEntity.toDomain() = Problem(
        id = id,
        title = title,
        description = description,
        solutionCode = solutionCode,
        approachExplanation = approachExplanation,
        difficulty = when (difficulty) {
            "Easy" -> Difficulty.EASY
            "Medium" -> Difficulty.MEDIUM
            "Hard" -> Difficulty.HARD
            else -> Difficulty.EASY
        },
        category = when (category) {
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
        orderIndex = orderIndex
    )
}
