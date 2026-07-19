package com.hackerrank.app.ui.problems

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.data.remote.DailyChallengeApi
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.repository.ProblemRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProblemsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val problemRepository: ProblemRepository = mockk()
    private val progressRepository: ProgressRepository = mockk()
    private val dailyChallengeApi: DailyChallengeApi = mockk()
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    private val problemsList = listOf(
        Problem(
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
        ),
        Problem(
            id = "2",
            title = "Reverse Linked List",
            description = "Reverse...",
            inputExample = "head = [1,2,3]",
            outputExample = "[3,2,1]",
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
        coEvery { progressRepository.isDailyChallengeCompleted(any()) } returns false
        every { progressRepository.getDailyChallengeState() } returns flowOf(null)
        coEvery { dailyChallengeApi.fetchToday() } returns null

        val viewModel = ProblemsViewModel(problemRepository, progressRepository, dailyChallengeApi)

        viewModel.uiState.test {
            val state = awaitItem() as ProblemsUiState.Loaded
            assertEquals(2, state.allProblems.size)
            assertEquals(2, state.filteredProblems.size)
            assertEquals(setOf("1"), state.solvedIds)
        }
    }

    @Test
    fun `selectDifficulty filters problems list`() = runTest {
        every { problemRepository.getAllProblems() } returns flowOf(problemsList)
        every { problemRepository.getSolvedIds() } returns flowOf(emptySet())
        coEvery { progressRepository.isDailyChallengeCompleted(any()) } returns false
        every { progressRepository.getDailyChallengeState() } returns flowOf(null)
        coEvery { dailyChallengeApi.fetchToday() } returns null

        val viewModel = ProblemsViewModel(problemRepository, progressRepository, dailyChallengeApi)

        viewModel.uiState.test {
            var state = awaitItem() as ProblemsUiState.Loaded
            assertNull(state.selectedDifficulty)

            viewModel.selectDifficulty(Difficulty.EASY)
            state = awaitItem() as ProblemsUiState.Loaded
            assertEquals(Difficulty.EASY, state.selectedDifficulty)
            assertEquals(1, state.filteredProblems.size)
            assertEquals("Two Sum", state.filteredProblems[0].title)

            viewModel.selectDifficulty(Difficulty.EASY)
            state = awaitItem() as ProblemsUiState.Loaded
            assertNull(state.selectedDifficulty)
            assertEquals(2, state.filteredProblems.size)
        }
    }

    @Test
    fun `selectCategory filters problems list`() = runTest {
        every { problemRepository.getAllProblems() } returns flowOf(problemsList)
        every { problemRepository.getSolvedIds() } returns flowOf(emptySet())
        coEvery { progressRepository.isDailyChallengeCompleted(any()) } returns false
        every { progressRepository.getDailyChallengeState() } returns flowOf(null)
        coEvery { dailyChallengeApi.fetchToday() } returns null

        val viewModel = ProblemsViewModel(problemRepository, progressRepository, dailyChallengeApi)

        viewModel.uiState.test {
            var state = awaitItem() as ProblemsUiState.Loaded
            assertNull(state.selectedCategory)

            viewModel.selectCategory(ProblemCategory.LINKED_LISTS)
            state = awaitItem() as ProblemsUiState.Loaded
            assertEquals(ProblemCategory.LINKED_LISTS, state.selectedCategory)
            assertEquals(1, state.filteredProblems.size)
            assertEquals("Reverse Linked List", state.filteredProblems[0].title)
        }
    }

    @Test
    fun `dailyChallenge state shows unavailable when no response and no cache`() = runTest {
        val today = LocalDate.now().format(dateFormatter)
        every { problemRepository.getAllProblems() } returns flowOf(problemsList)
        every { problemRepository.getSolvedIds() } returns flowOf(emptySet())
        coEvery { progressRepository.isDailyChallengeCompleted(today) } returns false
        every { progressRepository.getDailyChallengeState() } returns flowOf(null)
        coEvery { dailyChallengeApi.fetchToday() } returns null

        val viewModel = ProblemsViewModel(problemRepository, progressRepository, dailyChallengeApi)

        viewModel.uiState.test {
            val state = awaitItem() as ProblemsUiState.Loaded
            assertTrue(state.dailyChallenge.isUnavailable)
            assertFalse(state.dailyChallenge.isLoading)
        }
    }
}
