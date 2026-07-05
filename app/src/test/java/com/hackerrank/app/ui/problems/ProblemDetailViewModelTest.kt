package com.hackerrank.app.ui.problems

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.model.StreakInfo
import com.hackerrank.app.domain.repository.ProblemRepository
import com.hackerrank.app.domain.usecase.RecordProblemSolveUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProblemDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val problemRepository: ProblemRepository = mockk()
    private val recordProblemSolveUseCase: RecordProblemSolveUseCase = mockk()
    private val viewModel = ProblemDetailViewModel(problemRepository, recordProblemSolveUseCase)

    private val sampleProblem = Problem(
        id = "1",
        title = "Two Sum",
        description = "Find two...",
        inputExample = "nums = [2,7], target = 9",
        outputExample = "[0,1]",
        solutionCode = "code",
        approachExplanation = "explanation",
        difficulty = Difficulty.EASY,
        category = ProblemCategory.HASH_BASED,
        orderIndex = 1
    )

    @Test
    fun `loadProblem fetches details and unsolved status correctly`() = runTest {
        every { problemRepository.getProblemById("1") } returns flowOf(sampleProblem)
        every { problemRepository.isSolved("1") } returns flowOf(false)

        viewModel.loadProblem("1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(sampleProblem, state.problem)
            assertFalse(state.isSolved)
        }
    }

    @Test
    fun `loadProblem preserves input and output examples in uiState`() = runTest {
        every { problemRepository.getProblemById("1") } returns flowOf(sampleProblem)
        every { problemRepository.isSolved("1") } returns flowOf(false)

        viewModel.loadProblem("1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertNotNull(state.problem)
            assertEquals("nums = [2,7], target = 9", state.problem?.inputExample)
            assertEquals("[0,1]", state.problem?.outputExample)
        }
    }

    @Test
    fun `solve triggers RecordProblemSolveUseCase and updates state successfully`() = runTest {
        every { problemRepository.getProblemById("1") } returns flowOf(sampleProblem)
        every { problemRepository.isSolved("1") } returns flowOf(false)

        val gamificationResult = GamificationResult(
            xpAwarded = 10,
            newTotalXp = 110,
            newLevel = 1,
            previousLevel = 1,
            newBadges = emptyList(),
            streakInfo = StreakInfo(1, 1, true, null, "2026-07-04")
        )

        coEvery { recordProblemSolveUseCase("1", Difficulty.EASY) } returns gamificationResult

        viewModel.loadProblem("1")
        viewModel.solve()

        viewModel.uiState.test {
            // First item will be final state after solve
            val state = awaitItem()
            assertTrue(state.isSolved)
            assertFalse(state.isSolving)
            assertEquals(gamificationResult, state.solveResult)
        }
    }

    @Test
    fun `toggleSolution toggles visibility boolean`() = runTest {
        every { problemRepository.getProblemById("1") } returns flowOf(sampleProblem)
        every { problemRepository.isSolved("1") } returns flowOf(false)

        viewModel.loadProblem("1")

        viewModel.uiState.test {
            var state = awaitItem()
            assertFalse(state.showSolution)

            viewModel.toggleSolution()
            state = awaitItem()
            assertTrue(state.showSolution)

            viewModel.toggleSolution()
            state = awaitItem()
            assertFalse(state.showSolution)
        }
    }

    @Test
    fun `clearSolveResult clears gamification solveResult`() = runTest {
        every { problemRepository.getProblemById("1") } returns flowOf(sampleProblem)
        every { problemRepository.isSolved("1") } returns flowOf(false)

        val gamificationResult = GamificationResult(
            xpAwarded = 10,
            newTotalXp = 110,
            newLevel = 1,
            previousLevel = 1,
            newBadges = emptyList(),
            streakInfo = StreakInfo(1, 1, true, null, "2026-07-04")
        )
        coEvery { recordProblemSolveUseCase("1", Difficulty.EASY) } returns gamificationResult

        viewModel.loadProblem("1")
        viewModel.solve()

        viewModel.uiState.test {
            var state = awaitItem()
            assertNotNull(state.solveResult)

            viewModel.clearSolveResult()
            state = awaitItem()
            assertNull(state.solveResult)
        }
    }
}
