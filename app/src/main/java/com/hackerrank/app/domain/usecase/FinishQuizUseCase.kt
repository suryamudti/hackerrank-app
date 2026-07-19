package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.QuizSession
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class FinishQuizResult(
    val updatedProgress: UserProgress,
    val gamificationResult: GamificationResult
)

class FinishQuizUseCase @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val recordQuizCompleteUseCase: RecordQuizCompleteUseCase
) {
    suspend operator fun invoke(
        session: QuizSession,
        structureId: String
    ): FinishQuizResult {
        val existingProgress = progressRepository
            .getProgressByStructureId(structureId)
            .first()

        val newProgress = (existingProgress ?: UserProgress(structureId = structureId)).copy(
            quizzesCompleted = (existingProgress?.quizzesCompleted ?: 0) + 1,
            totalCorrect = (existingProgress?.totalCorrect ?: 0) + session.score,
            totalQuestions = (existingProgress?.totalQuestions ?: 0) + session.totalQuestions,
            bestScore = maxOf(existingProgress?.bestScore ?: 0, session.score),
            masteryLevel = ((existingProgress?.totalCorrect ?: 0) + session.score) * 100 /
                    ((existingProgress?.totalQuestions ?: 0) + session.totalQuestions)
        )
        progressRepository.upsertProgress(newProgress)

        val gamificationResult = recordQuizCompleteUseCase(
            score = session.score,
            totalQuestions = session.totalQuestions,
            elapsedTimeMs = session.elapsedTimeMs,
            structureId = structureId
        )

        return FinishQuizResult(
            updatedProgress = newProgress,
            gamificationResult = gamificationResult
        )
    }
}
