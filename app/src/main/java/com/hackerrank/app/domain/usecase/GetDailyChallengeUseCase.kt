package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.repository.DailyChallengeRepository
import com.hackerrank.app.domain.repository.ProblemRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class DailyChallengeResult(
    val problem: Problem?,
    val bonusXp: Int,
    val isCompleted: Boolean,
    val isAvailable: Boolean,
)

class GetDailyChallengeUseCase
    @Inject
    constructor(
        private val dailyChallengeRepository: DailyChallengeRepository,
        private val problemRepository: ProblemRepository,
    ) {
        private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        suspend operator fun invoke(): DailyChallengeResult {
            val today = LocalDate.now().format(dateFormatter)
            val isCompleted = dailyChallengeRepository.isDailyChallengeCompleted(today)
            val challenge = dailyChallengeRepository.getDailyChallenge(today)

            if (challenge != null) {
                val problem = problemRepository.getProblemById(challenge.problemId).first()
                return DailyChallengeResult(
                    problem = problem,
                    bonusXp = challenge.bonusXp,
                    isCompleted = isCompleted,
                    isAvailable = true,
                )
            }

            return DailyChallengeResult(
                problem = null,
                bonusXp = 0,
                isCompleted = isCompleted,
                isAvailable = false,
            )
        }
    }
