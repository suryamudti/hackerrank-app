package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.repository.ProblemRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ObserveProblemDetailUseCaseTest {

    private val problemRepository: ProblemRepository = mockk()
    private val useCase = ObserveProblemDetailUseCase(problemRepository)

    private val problem = Problem(
        id = "1", title = "Two Sum", description = "", inputExample = "", outputExample = "",
        solutionCode = "", approachExplanation = "", difficulty = Difficulty.EASY,
        category = ProblemCategory.HASH_BASED, orderIndex = 1
    )

    @Test
    fun `invoke returns problem and solved status`() = runTest {
        every { problemRepository.getProblemById("1") } returns flowOf(problem)
        every { problemRepository.isSolved("1") } returns flowOf(true)

        val result = useCase("1").first()

        assertEquals(problem, result.problem)
        assertTrue(result.isSolved)
    }

    @Test
    fun `invoke returns unsolved status when not solved`() = runTest {
        every { problemRepository.getProblemById("1") } returns flowOf(problem)
        every { problemRepository.isSolved("1") } returns flowOf(false)

        val result = useCase("1").first()

        assertEquals(problem, result.problem)
        assertFalse(result.isSolved)
    }

    @Test
    fun `invoke returns null problem when not found`() = runTest {
        every { problemRepository.getProblemById("1") } returns flowOf(null)
        every { problemRepository.isSolved("1") } returns flowOf(false)

        val result = useCase("1").first()

        assertNull(result.problem)
        assertFalse(result.isSolved)
    }
}
