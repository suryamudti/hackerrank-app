package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.gamification.GamificationEngine
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.StreakInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RecordQuizCompleteUseCaseTest {

    private val gamificationEngine: GamificationEngine = mockk()
    private val useCase = RecordQuizCompleteUseCase(gamificationEngine)

    @Test
    fun `invoke calls recordQuizComplete on gamificationEngine`() = runTest {
        val expectedResult = GamificationResult(
            xpAwarded = 140,
            newTotalXp = 240,
            newLevel = 1,
            previousLevel = 1,
            newBadges = emptyList(),
            streakInfo = StreakInfo(1, 1, true, null, "2026-07-04")
        )

        coEvery {
            gamificationEngine.recordQuizComplete(
                score = 8,
                totalQuestions = 8,
                elapsedTimeMs = 30_000L,
                structureId = "struct1"
            )
        } returns expectedResult

        val actualResult = useCase(
            score = 8,
            totalQuestions = 8,
            elapsedTimeMs = 30_000L,
            structureId = "struct1"
        )

        assertEquals(expectedResult, actualResult)

        coVerify(exactly = 1) {
            gamificationEngine.recordQuizComplete(
                score = 8,
                totalQuestions = 8,
                elapsedTimeMs = 30_000L,
                structureId = "struct1"
            )
        }
    }
}
