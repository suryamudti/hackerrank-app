package com.hackerrank.app.ui.problems

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.repository.ProblemRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ProblemsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val problemRepository: ProblemRepository = mockk()

    private val problemsList = listOf(
        Problem(
            id = "1",
            title = "Two Sum",
            description = "Find two...",
            solutionCode = "code",
            approachExplanation = "explanation",
            difficulty = Difficulty.EASY,
            category = ProblemCategory.HASH_BASED,
            orderIndex = 1
        ),
        Problem(
            id = "2",
            title = "Reverse Linked List",
            description = "Reverse...",
            solutionCode = "code",
            approachExplanation = "explanation",
            difficulty = Difficulty.MEDIUM,
            category = ProblemCategory.LINKED_LISTS,
            orderIndex = 2
        )
    )

    @Test
    fun `initializing ViewModel loads all problems and solved IDs`() = runTest {
        every { problemRepository.getAllProblems() } returns flowOf(problemsList)
        every { problemRepository.getSolvedIds() } returns flowOf(setOf("1"))

        val viewModel = ProblemsViewModel(problemRepository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.allProblems.size)
            assertEquals(2, state.filteredProblems.size)
            assertEquals(setOf("1"), state.solvedIds)
        }
    }

    @Test
    fun `selectDifficulty filters problems list`() = runTest {
        every { problemRepository.getAllProblems() } returns flowOf(problemsList)
        every { problemRepository.getSolvedIds() } returns flowOf(emptySet())

        val viewModel = ProblemsViewModel(problemRepository)

        viewModel.uiState.test {
            var state = awaitItem()
            assertNull(state.selectedDifficulty)

            // Select EASY difficulty
            viewModel.selectDifficulty(Difficulty.EASY)
            state = awaitItem()
            assertEquals(Difficulty.EASY, state.selectedDifficulty)
            assertEquals(1, state.filteredProblems.size)
            assertEquals("Two Sum", state.filteredProblems[0].title)

            // Select EASY again to clear it
            viewModel.selectDifficulty(Difficulty.EASY)
            state = awaitItem()
            assertNull(state.selectedDifficulty)
            assertEquals(2, state.filteredProblems.size)
        }
    }

    @Test
    fun `selectCategory filters problems list`() = runTest {
        every { problemRepository.getAllProblems() } returns flowOf(problemsList)
        every { problemRepository.getSolvedIds() } returns flowOf(emptySet())

        val viewModel = ProblemsViewModel(problemRepository)

        viewModel.uiState.test {
            var state = awaitItem()
            assertNull(state.selectedCategory)

            // Select LINKED_LISTS category
            viewModel.selectCategory(ProblemCategory.LINKED_LISTS)
            state = awaitItem()
            assertEquals(ProblemCategory.LINKED_LISTS, state.selectedCategory)
            assertEquals(1, state.filteredProblems.size)
            assertEquals("Reverse Linked List", state.filteredProblems[0].title)
        }
    }
}
