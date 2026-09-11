package com.hackerrank.app.data.repository

import com.hackerrank.app.data.local.dao.BookmarkedProblemDao
import com.hackerrank.app.data.local.dao.ProblemDao
import com.hackerrank.app.data.local.dao.SolvedProblemDao
import com.hackerrank.app.data.local.entity.BookmarkedProblemEntity
import com.hackerrank.app.data.local.entity.SolvedProblemEntity
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.repository.ProblemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProblemRepositoryImpl
    @Inject
    constructor(
        private val problemDao: ProblemDao,
        private val solvedProblemDao: SolvedProblemDao,
        private val bookmarkedProblemDao: BookmarkedProblemDao,
    ) : ProblemRepository {
        override fun getAllProblems(): Flow<List<Problem>> =
            problemDao.getAllProblems().map { entities ->
                entities.map { it.toDomain() }
            }

        override fun getProblemById(id: String): Flow<Problem?> = problemDao.getProblemById(id).map { it?.toDomain() }

        override fun getSolvedIds(): Flow<Set<String>> = solvedProblemDao.getSolvedIds().map { it.toSet() }

        override fun isSolved(problemId: String): Flow<Boolean> = solvedProblemDao.isSolved(problemId)

        override suspend fun markAsSolved(problemId: String) {
            solvedProblemDao.insert(SolvedProblemEntity(problemId = problemId))
        }

        override fun getBookmarkedIds(): Flow<Set<String>> = bookmarkedProblemDao.getBookmarkedIds().map { it.toSet() }

        override fun isBookmarked(problemId: String): Flow<Boolean> = bookmarkedProblemDao.isBookmarked(problemId)

        override suspend fun toggleBookmark(problemId: String) {
            if (bookmarkedProblemDao.isBookmarkedSync(problemId)) {
                bookmarkedProblemDao.delete(problemId)
            } else {
                bookmarkedProblemDao.insert(BookmarkedProblemEntity(problemId = problemId))
            }
        }
    }
