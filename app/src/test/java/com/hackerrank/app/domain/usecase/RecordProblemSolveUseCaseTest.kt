package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.gamification.GamificationEngine
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.StreakInfo
import com.hackerrank.app.domain.repository.ProblemRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RecordProblemSolveUseCaseTest {
    private val problemRepository: ProblemRepository = mockk()
    private val gamificationEngine: GamificationEngine = mockk()
    private val useCase = RecordProblemSolveUseCase(problemRepository, gamificationEngine)

    @Test
    fun `invoke marks problem solved and records problem solved in gamificationEngine`() =
        runTest {
            val expectedResult =
                GamificationResult(
                    xpAwarded = 25,
                    newTotalXp = 125,
                    newLevel = 1,
                    previousLevel = 1,
                    newBadges = emptyList(),
                    streakInfo = StreakInfo(1, 1, true, null, "2026-07-04"),
                )

            coEvery { problemRepository.markAsSolved("prob1") } returns Unit
            coEvery { gamificationEngine.recordProblemSolved(Difficulty.MEDIUM) } returns expectedResult

            val actualResult = useCase("prob1", Difficulty.MEDIUM)

            assertEquals(expectedResult, actualResult)

            coVerify(exactly = 1) {
                problemRepository.markAsSolved("prob1")
                gamificationEngine.recordProblemSolved(Difficulty.MEDIUM)
            }
        }
}
