package com.hackerrank.app.domain.usecase

import com.hackerrank.app.data.remote.DailyChallengeApi
import com.hackerrank.app.data.remote.DailyChallengeResponse
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.repository.DailyChallengeRepository
import com.hackerrank.app.domain.repository.ProblemRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
    private val dailyChallengeApi: DailyChallengeApi = mockk()
    private val useCase = GetDailyChallengeUseCase(dailyChallengeRepository, problemRepository, dailyChallengeApi)

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val today = LocalDate.now().format(dateFormatter)
    private val problem =
        Problem(
            id = "1", title = "Two Sum", description = "", inputExample = "", outputExample = "",
            solutionCode = "", approachExplanation = "", difficulty = Difficulty.EASY,
            category = ProblemCategory.HASH_BASED, orderIndex = 1,
        )

    @Test
    fun `returns cached challenge when available and fresh`() =
        runTest {
            coEvery { dailyChallengeRepository.isDailyChallengeCompleted(today) } returns false
            every { dailyChallengeRepository.getDailyChallengeState() } returns
                flowOf(
                    DailyChallengeResponse(date = today, problemId = "1", bonusXp = 10),
                )
            every { problemRepository.getProblemById("1") } returns flowOf(problem)

            val result = useCase()

            assertEquals(problem, result.problem)
            assertEquals(10, result.bonusXp)
            assertFalse(result.isCompleted)
            assertTrue(result.isAvailable)
        }

    @Test
    fun `fetches from API when cache is stale and caches response`() =
        runTest {
            val yesterday = LocalDate.now().minusDays(1).format(dateFormatter)
            coEvery { dailyChallengeRepository.isDailyChallengeCompleted(today) } returns false
            every { dailyChallengeRepository.getDailyChallengeState() } returns
                flowOf(
                    DailyChallengeResponse(date = yesterday, problemId = "1", bonusXp = 10),
                )
            coEvery { dailyChallengeApi.fetchToday() } returns
                DailyChallengeResponse(date = today, problemId = "1", bonusXp = 20)
            coEvery { dailyChallengeRepository.cacheDailyChallengeResponse(any()) } returns Unit
            every { problemRepository.getProblemById("1") } returns flowOf(problem)

            val result = useCase()

            assertEquals(problem, result.problem)
            assertEquals(20, result.bonusXp)
            assertTrue(result.isAvailable)
            coVerify { dailyChallengeRepository.cacheDailyChallengeResponse(any()) }
        }

    @Test
    fun `falls back to stale cache when API fails`() =
        runTest {
            val yesterday = LocalDate.now().minusDays(1).format(dateFormatter)
            coEvery { dailyChallengeRepository.isDailyChallengeCompleted(today) } returns false
            every { dailyChallengeRepository.getDailyChallengeState() } returns
                flowOf(
                    DailyChallengeResponse(date = yesterday, problemId = "1", bonusXp = 10),
                )
            coEvery { dailyChallengeApi.fetchToday() } returns null
            every { problemRepository.getProblemById("1") } returns flowOf(problem)

            val result = useCase()

            assertEquals(problem, result.problem)
            assertEquals(10, result.bonusXp)
            assertTrue(result.isAvailable)
        }

    @Test
    fun `returns unavailable when nothing is available`() =
        runTest {
            coEvery { dailyChallengeRepository.isDailyChallengeCompleted(today) } returns false
            every { dailyChallengeRepository.getDailyChallengeState() } returns flowOf(null)
            coEvery { dailyChallengeApi.fetchToday() } returns null

            val result = useCase()

            assertEquals(null, result.problem)
            assertEquals(0, result.bonusXp)
            assertFalse(result.isAvailable)
        }
}
