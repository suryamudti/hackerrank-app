package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.gamification.GamificationEngine
import com.hackerrank.app.domain.model.GamificationResult
import javax.inject.Inject

class RecordLoginUseCase
    @Inject
    constructor(
        private val gamificationEngine: GamificationEngine,
    ) {
        suspend operator fun invoke(): GamificationResult {
            return gamificationEngine.recordLogin()
        }
    }
