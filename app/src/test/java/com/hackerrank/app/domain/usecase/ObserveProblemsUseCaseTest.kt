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
import org.junit.Test

class ObserveProblemsUseCaseTest {
    private val problemRepository: ProblemRepository = mockk()
    private val useCase = ObserveProblemsUseCase(problemRepository)

    private val problems =
        listOf(
            Problem(
                id = "1", title = "Two Sum", description = "", inputExample = "", outputExample = "",
                solutionCode = "", approachExplanation = "", difficulty = Difficulty.EASY,
                category = ProblemCategory.HASH_BASED, orderIndex = 1,
            ),
            Problem(
                id = "2", title = "Reverse Linked List", description = "", inputExample = "", outputExample = "",
                solutionCode = "", approachExplanation = "", difficulty = Difficulty.MEDIUM,
                category = ProblemCategory.LINKED_LISTS, orderIndex = 2,
            ),
        )

    @Test
    fun `invoke combines problems and solved IDs`() =
        runTest {
            every { problemRepository.getAllProblems() } returns flowOf(problems)
            every { problemRepository.getSolvedIds() } returns flowOf(setOf("1"))

            val result = useCase().first()

            assertEquals(2, result.allProblems.size)
            assertEquals(setOf("1"), result.solvedIds)
        }

    @Test
    fun `invoke returns empty solved set when none solved`() =
        runTest {
            every { problemRepository.getAllProblems() } returns flowOf(problems)
            every { problemRepository.getSolvedIds() } returns flowOf(emptySet())

            val result = useCase().first()

            assertEquals(2, result.allProblems.size)
            assertEquals(emptySet<String>(), result.solvedIds)
        }
}
