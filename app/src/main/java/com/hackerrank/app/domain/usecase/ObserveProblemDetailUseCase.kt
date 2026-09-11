package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.repository.ProblemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class ProblemDetailData(
    val problem: Problem?,
    val isSolved: Boolean,
    val isBookmarked: Boolean = false,
)

class ObserveProblemDetailUseCase
    @Inject
    constructor(
        private val problemRepository: ProblemRepository,
    ) {
        operator fun invoke(problemId: String): Flow<ProblemDetailData> {
            return combine(
                problemRepository.getProblemById(problemId),
                problemRepository.isSolved(problemId),
                problemRepository.isBookmarked(problemId),
            ) { problem, isSolved, isBookmarked ->
                ProblemDetailData(
                    problem = problem,
                    isSolved = isSolved,
                    isBookmarked = isBookmarked,
                )
            }
        }
    }
