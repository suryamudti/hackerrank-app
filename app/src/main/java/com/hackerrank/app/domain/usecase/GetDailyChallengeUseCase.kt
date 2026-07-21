package com.hackerrank.app.domain.usecase

import com.hackerrank.app.data.remote.DailyChallengeApi
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
        private val dailyChallengeApi: DailyChallengeApi,
    ) {
        private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        suspend operator fun invoke(): DailyChallengeResult {
            val today = LocalDate.now().format(dateFormatter)
            val isCompleted = dailyChallengeRepository.isDailyChallengeCompleted(today)
            val cached = dailyChallengeRepository.getDailyChallengeState().first()

            if (cached != null && cached.date == today) {
                val problem = resolveProblem(cached.problemId)
                return DailyChallengeResult(problem, cached.bonusXp, isCompleted, true)
            }

            val response = dailyChallengeApi.fetchToday()
            if (response != null && response.date == today) {
                dailyChallengeRepository.cacheDailyChallengeResponse(response)
                val problem = resolveProblem(response.problemId)
                return DailyChallengeResult(problem, response.bonusXp, isCompleted, true)
            }

            if (cached != null) {
                val problem = resolveProblem(cached.problemId)
                return DailyChallengeResult(problem, cached.bonusXp, isCompleted, true)
            }

            return DailyChallengeResult(null, 0, isCompleted, false)
        }

        private suspend fun resolveProblem(problemId: String): Problem? {
            return problemRepository.getProblemById(problemId).first()
        }
    }
