package com.hackerrank.app.domain.usecase

import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.core.Constants
import com.hackerrank.app.domain.gamification.GamificationEngine
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.StreakInfo
import com.hackerrank.app.domain.repository.ProblemRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecordDailyChallengeUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val problemRepository: ProblemRepository = mockk()
    private val progressRepository: ProgressRepository = mockk()
    private val gamificationEngine: GamificationEngine = mockk()
    private val useCase = RecordDailyChallengeUseCase(
        problemRepository,
        progressRepository,
        gamificationEngine
    )
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @Test
    fun `invoke records daily challenge and returns result when not already completed`() = runTest {
        val today = LocalDate.now().format(dateFormatter)
        val expectedResult = GamificationResult(
            xpAwarded = 10 + Constants.DAILY_CHALLENGE_BONUS_XP,
            newTotalXp = 140,
            newLevel = 1,
            previousLevel = 1,
            newBadges = emptyList(),
            streakInfo = StreakInfo(1, 1, true, null, today)
        )

        coEvery { progressRepository.isDailyChallengeCompleted(today) } returns false
        coEvery { problemRepository.markAsSolved("p1") } returns Unit
        coEvery { gamificationEngine.recordDailyChallengeCompleted(Difficulty.EASY, Constants.DAILY_CHALLENGE_BONUS_XP) } returns expectedResult
        coEvery { progressRepository.setDailyChallengeCompleted(today) } returns Unit

        val result = useCase("p1", Difficulty.EASY, Constants.DAILY_CHALLENGE_BONUS_XP)

        assertNotNull(result)
        assertEquals(expectedResult, result)

        coVerify { problemRepository.markAsSolved("p1") }
        coVerify { gamificationEngine.recordDailyChallengeCompleted(Difficulty.EASY, Constants.DAILY_CHALLENGE_BONUS_XP) }
        coVerify { progressRepository.setDailyChallengeCompleted(today) }
    }

    @Test
    fun `invoke returns null when already completed today`() = runTest {
        val today = LocalDate.now().format(dateFormatter)

        coEvery { progressRepository.isDailyChallengeCompleted(today) } returns true

        val result = useCase("p1", Difficulty.EASY, Constants.DAILY_CHALLENGE_BONUS_XP)

        assertNull(result)
    }
}
