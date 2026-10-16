package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.DailyChallenge
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.repository.DailyChallengeRepository
import com.hackerrank.app.domain.repository.ProblemRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetDailyChallengeUseCaseTest {
    private val dailyChallengeRepository: DailyChallengeRepository = mockk()
    private val problemRepository: ProblemRepository = mockk()
    private val useCase = GetDailyChallengeUseCase(dailyChallengeRepository, problemRepository)

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val today = LocalDate.now().format(dateFormatter)
    private val problem =
        Problem(
            id = "1",
            title = "Two Sum",
            description = "",
            inputExample = "",
            outputExample = "",
            solutionCode = "",
            approachExplanation = "",
            difficulty = Difficulty.EASY,
            category = ProblemCategory.HASH_BASED,
            orderIndex = 1,
        )

    @Test
    fun `returns available challenge when repository returns challenge for today`() =
        runTest {
            coEvery { dailyChallengeRepository.isDailyChallengeCompleted(today) } returns false
            coEvery { dailyChallengeRepository.getDailyChallenge(today) } returns
                DailyChallenge(date = today, problemId = "1", bonusXp = 10)
            every { problemRepository.getProblemById("1") } returns flowOf(problem)

            val result = useCase()

            assertEquals(problem, result.problem)
            assertEquals(10, result.bonusXp)
            assertFalse(result.isCompleted)
            assertTrue(result.isAvailable)
        }

    @Test
    fun `returns completed status correctly when challenge was completed`() =
        runTest {
            coEvery { dailyChallengeRepository.isDailyChallengeCompleted(today) } returns true
            coEvery { dailyChallengeRepository.getDailyChallenge(today) } returns
                DailyChallenge(date = today, problemId = "1", bonusXp = 25)
            every { problemRepository.getProblemById("1") } returns flowOf(problem)

            val result = useCase()

            assertEquals(problem, result.problem)
            assertEquals(25, result.bonusXp)
            assertTrue(result.isCompleted)
            assertTrue(result.isAvailable)
        }

    @Test
    fun `returns unavailable when repository returns null`() =
        runTest {
            coEvery { dailyChallengeRepository.isDailyChallengeCompleted(today) } returns false
            coEvery { dailyChallengeRepository.getDailyChallenge(today) } returns null

            val result = useCase()

            assertEquals(null, result.problem)
            assertEquals(0, result.bonusXp)
            assertFalse(result.isAvailable)
        }
}
