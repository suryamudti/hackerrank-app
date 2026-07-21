package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.gamification.GamificationEngine
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.repository.ProblemRepository
import javax.inject.Inject

class RecordProblemSolveUseCase
    @Inject
    constructor(
        private val problemRepository: ProblemRepository,
        private val gamificationEngine: GamificationEngine,
    ) {
        suspend operator fun invoke(
            problemId: String,
            difficulty: Difficulty,
        ): GamificationResult {
            problemRepository.markAsSolved(problemId)
            return gamificationEngine.recordProblemSolved(difficulty)
        }
    }
