package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.gamification.GamificationEngine
import com.hackerrank.app.domain.model.GamificationResult
import javax.inject.Inject

class RecordQuizCompleteUseCase
    @Inject
    constructor(
        private val gamificationEngine: GamificationEngine,
    ) {
        suspend operator fun invoke(
            score: Int,
            totalQuestions: Int,
            elapsedTimeMs: Long,
            structureId: String,
        ): GamificationResult {
            return gamificationEngine.recordQuizComplete(
                score = score,
                totalQuestions = totalQuestions,
                elapsedTimeMs = elapsedTimeMs,
                structureId = structureId,
            )
        }
    }
