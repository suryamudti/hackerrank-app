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

class RecordLoginUseCaseTest {
    private val gamificationEngine: GamificationEngine = mockk()
    private val useCase = RecordLoginUseCase(gamificationEngine)

    @Test
    fun `invoke calls recordLogin on gamificationEngine`() =
        runTest {
            val expectedResult =
                GamificationResult(
                    xpAwarded = 10,
                    newTotalXp = 110,
                    newLevel = 1,
                    previousLevel = 1,
                    newBadges = emptyList(),
                    streakInfo = StreakInfo(1, 1, true, null, "2026-07-04"),
                )

            coEvery { gamificationEngine.recordLogin() } returns expectedResult

            val actualResult = useCase()

            assertEquals(expectedResult, actualResult)
            coVerify(exactly = 1) { gamificationEngine.recordLogin() }
        }
}
