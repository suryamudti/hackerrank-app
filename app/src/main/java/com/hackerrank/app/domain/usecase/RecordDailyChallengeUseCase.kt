package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.gamification.GamificationEngine
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.repository.ProblemRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RecordDailyChallengeUseCase @Inject constructor(
    private val problemRepository: ProblemRepository,
    private val progressRepository: ProgressRepository,
    private val gamificationEngine: GamificationEngine
) {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    suspend operator fun invoke(
        problemId: String,
        difficulty: Difficulty,
        bonusXp: Int
    ): GamificationResult? {
        val today = LocalDate.now().format(dateFormatter)
        if (progressRepository.isDailyChallengeCompleted(today)) return null

        problemRepository.markAsSolved(problemId)
        val result = gamificationEngine.recordDailyChallengeCompleted(difficulty, bonusXp)
        progressRepository.setDailyChallengeCompleted(today)

        return result
    }
}
