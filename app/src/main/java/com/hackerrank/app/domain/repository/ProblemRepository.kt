package com.hackerrank.app.domain.repository

import com.hackerrank.app.domain.model.Problem
import kotlinx.coroutines.flow.Flow

interface ProblemRepository {
    fun getAllProblems(): Flow<List<Problem>>
    fun getProblemById(id: String): Flow<Problem?>
    fun getSolvedIds(): Flow<Set<String>>
    fun isSolved(problemId: String): Flow<Boolean>
    suspend fun markAsSolved(problemId: String)
}
