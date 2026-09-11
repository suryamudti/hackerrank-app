package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.repository.ProblemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class ProblemsData(
    val allProblems: List<Problem>,
    val solvedIds: Set<String>,
    val bookmarkedIds: Set<String> = emptySet(),
)

class ObserveProblemsUseCase
    @Inject
    constructor(
        private val problemRepository: ProblemRepository,
    ) {
        operator fun invoke(): Flow<ProblemsData> {
            return combine(
                problemRepository.getAllProblems(),
                problemRepository.getSolvedIds(),
                problemRepository.getBookmarkedIds(),
            ) { problems, solvedIds, bookmarkedIds ->
                ProblemsData(
                    allProblems = problems,
                    solvedIds = solvedIds,
                    bookmarkedIds = bookmarkedIds,
                )
            }
        }
    }
