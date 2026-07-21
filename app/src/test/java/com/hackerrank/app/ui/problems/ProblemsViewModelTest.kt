package com.hackerrank.app.ui.problems

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.usecase.DailyChallengeResult
import com.hackerrank.app.domain.usecase.GetDailyChallengeUseCase
import com.hackerrank.app.domain.usecase.ObserveProblemsUseCase
import com.hackerrank.app.domain.usecase.ProblemsData
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

class ProblemsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeProblemsUseCase: ObserveProblemsUseCase = mockk()
    private val getDailyChallengeUseCase: GetDailyChallengeUseCase = mockk()

    private val problemsList =
        listOf(
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
                orderIndex = 1,
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
                orderIndex = 2,
            ),
        )

    @Test
    fun `initializing ViewModel loads all problems and solved IDs`() =
        runTest {
            every { observeProblemsUseCase() } returns
                flowOf(
                    ProblemsData(allProblems = problemsList, solvedIds = setOf("1")),
                )
            coEvery { getDailyChallengeUseCase() } returns
                DailyChallengeResult(
                    problem = problemsList[0], bonusXp = 10, isCompleted = false, isAvailable = true,
                )

            val viewModel = ProblemsViewModel(observeProblemsUseCase, getDailyChallengeUseCase)

            viewModel.uiState.test {
                val state = awaitItem() as ProblemsUiState.Loaded
                assertEquals(2, state.allProblems.size)
                assertEquals(2, state.filteredProblems.size)
                assertEquals(setOf("1"), state.solvedIds)
            }
        }

    @Test
    fun `selectDifficulty filters problems list`() =
        runTest {
            every { observeProblemsUseCase() } returns
                flowOf(
                    ProblemsData(allProblems = problemsList, solvedIds = emptySet()),
                )
            coEvery { getDailyChallengeUseCase() } returns
                DailyChallengeResult(
                    problem = problemsList[0], bonusXp = 10, isCompleted = false, isAvailable = true,
                )

            val viewModel = ProblemsViewModel(observeProblemsUseCase, getDailyChallengeUseCase)

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
    fun `selectCategory filters problems list`() =
        runTest {
            every { observeProblemsUseCase() } returns
                flowOf(
                    ProblemsData(allProblems = problemsList, solvedIds = emptySet()),
                )
            coEvery { getDailyChallengeUseCase() } returns
                DailyChallengeResult(
                    problem = problemsList[0], bonusXp = 10, isCompleted = false, isAvailable = true,
                )

            val viewModel = ProblemsViewModel(observeProblemsUseCase, getDailyChallengeUseCase)

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
    fun `combined difficulty and category filter produces correct subset`() =
        runTest {
            val problems =
                listOf(
                    Problem("1", "Two Sum", "desc", "in", "out", "code", "approach", Difficulty.EASY, ProblemCategory.HASH_BASED, 1),
                    Problem("2", "Array Sum", "desc", "in", "out", "code", "approach", Difficulty.EASY, ProblemCategory.ARRAYS, 2),
                    Problem("3", "Linked List Reverse", "desc", "in", "out", "code", "approach", Difficulty.MEDIUM, ProblemCategory.LINKED_LISTS, 3),
                )
            every { observeProblemsUseCase() } returns
                flowOf(
                    ProblemsData(allProblems = problems, solvedIds = emptySet()),
                )
            coEvery { getDailyChallengeUseCase() } returns
                DailyChallengeResult(
                    problem = problems[0], bonusXp = 10, isCompleted = false, isAvailable = true,
                )

            val viewModel = ProblemsViewModel(observeProblemsUseCase, getDailyChallengeUseCase)

            viewModel.uiState.test {
                var state = awaitItem() as ProblemsUiState.Loaded
                assertEquals(3, state.filteredProblems.size)

                viewModel.selectDifficulty(Difficulty.EASY)
                state = awaitItem() as ProblemsUiState.Loaded
                assertEquals(Difficulty.EASY, state.selectedDifficulty)
                assertEquals(2, state.filteredProblems.size)

                viewModel.selectCategory(ProblemCategory.ARRAYS)
                state = awaitItem() as ProblemsUiState.Loaded
                assertEquals(ProblemCategory.ARRAYS, state.selectedCategory)
                assertEquals(1, state.filteredProblems.size)
                assertEquals("Array Sum", state.filteredProblems[0].title)

                viewModel.selectDifficulty(Difficulty.EASY)
                state = awaitItem() as ProblemsUiState.Loaded
                assertNull(state.selectedDifficulty)
                assertEquals(ProblemCategory.ARRAYS, state.selectedCategory)
                assertEquals(1, state.filteredProblems.size)
            }
        }

    @Test
    fun `error scenario when use case throws becomes Error`() =
        runTest {
            every { observeProblemsUseCase() } throws RuntimeException("Network error")
            coEvery { getDailyChallengeUseCase() } throws RuntimeException("Network error")

            val viewModel = ProblemsViewModel(observeProblemsUseCase, getDailyChallengeUseCase)

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue("Expected Error but got $state", state is ProblemsUiState.Error)
                assertEquals("Network error", (state as ProblemsUiState.Error).message)
            }
        }

    @Test
    fun `dailyChallenge state shows unavailable when no response and no cache`() =
        runTest {
            every { observeProblemsUseCase() } returns
                flowOf(
                    ProblemsData(allProblems = problemsList, solvedIds = emptySet()),
                )
            coEvery { getDailyChallengeUseCase() } returns
                DailyChallengeResult(
                    problem = null, bonusXp = 0, isCompleted = false, isAvailable = false,
                )

            val viewModel = ProblemsViewModel(observeProblemsUseCase, getDailyChallengeUseCase)

            viewModel.uiState.test {
                val state = awaitItem() as ProblemsUiState.Loaded
                assertTrue(state.dailyChallenge.isUnavailable)
                assertFalse(state.dailyChallenge.isLoading)
            }
        }

    @Test
    fun `refresh triggers reload flow`() =
        runTest {
            every { observeProblemsUseCase() } returns
                flowOf(
                    ProblemsData(allProblems = problemsList, solvedIds = setOf("1")),
                )
            coEvery { getDailyChallengeUseCase() } returns
                DailyChallengeResult(
                    problem = problemsList[0], bonusXp = 10, isCompleted = false, isAvailable = true,
                )

            val viewModel = ProblemsViewModel(observeProblemsUseCase, getDailyChallengeUseCase)

            viewModel.refresh()

            viewModel.isRefreshing.test {
                val val1 = awaitItem()
                testScheduler.advanceTimeBy(1200)
                val val2 = awaitItem()
                assertFalse(val2)
            }
        }
}
