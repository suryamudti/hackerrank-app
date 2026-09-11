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
    fun `invoke combines problems, solved IDs, and bookmarked IDs`() =
        runTest {
            every { problemRepository.getAllProblems() } returns flowOf(problems)
            every { problemRepository.getSolvedIds() } returns flowOf(setOf("1"))
            every { problemRepository.getBookmarkedIds() } returns flowOf(setOf("2"))

            val result = useCase().first()

            assertEquals(2, result.allProblems.size)
            assertEquals(setOf("1"), result.solvedIds)
            assertEquals(setOf("2"), result.bookmarkedIds)
        }

    @Test
    fun `invoke returns empty solved and bookmarked set when none`() =
        runTest {
            every { problemRepository.getAllProblems() } returns flowOf(problems)
            every { problemRepository.getSolvedIds() } returns flowOf(emptySet())
            every { problemRepository.getBookmarkedIds() } returns flowOf(emptySet())

            val result = useCase().first()

            assertEquals(2, result.allProblems.size)
            assertEquals(emptySet<String>(), result.solvedIds)
            assertEquals(emptySet<String>(), result.bookmarkedIds)
        }
}
