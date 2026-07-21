package com.hackerrank.app.ui.problems

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.*
import androidx.compose.ui.test.onNodeWithText
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.Problem
import com.hackerrank.app.domain.model.ProblemCategory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ProblemDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleProblem =
        Problem(
            id = "1",
            title = "Two Sum",
            description = "Find two numbers that add up to target",
            inputExample = "nums = [2,7], target = 9",
            outputExample = "[0,1]",
            solutionCode = "fun twoSum() { ... }",
            approachExplanation = "Use a hash map to store complements",
            difficulty = Difficulty.EASY,
            category = ProblemCategory.HASH_BASED,
            orderIndex = 1,
        )

    @Test
    fun loadedState_displaysProblemDescription() {
        val viewModel: ProblemDetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProblemDetailUiState.Loaded(
                    problem = sampleProblem,
                    isSolved = false,
                    isSolving = false,
                    solveResult = null,
                    showSolution = false,
                    isDailyChallenge = false,
                    bonusXp = 0,
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                ProblemDetailScreen(problemId = "1", onBackClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Find two numbers that add up to target").assertExists()
    }

    @Test
    fun loadedState_displaysApproachSection() {
        val viewModel: ProblemDetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProblemDetailUiState.Loaded(
                    problem = sampleProblem,
                    isSolved = false,
                    isSolving = false,
                    solveResult = null,
                    showSolution = false,
                    isDailyChallenge = false,
                    bonusXp = 0,
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                ProblemDetailScreen(problemId = "1", onBackClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Use a hash map to store complements").assertExists()
    }

    @Test
    fun solutionToggle_showsCode() {
        val viewModel: ProblemDetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProblemDetailUiState.Loaded(
                    problem = sampleProblem,
                    isSolved = false,
                    isSolving = false,
                    solveResult = null,
                    showSolution = true,
                    isDailyChallenge = false,
                    bonusXp = 0,
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                ProblemDetailScreen(problemId = "1", onBackClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("fun twoSum() { ... }").assertExists()
    }

    @Test
    fun markAsSolvedButton_exists() {
        val viewModel: ProblemDetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProblemDetailUiState.Loaded(
                    problem = sampleProblem,
                    isSolved = false,
                    isSolving = false,
                    solveResult = null,
                    showSolution = false,
                    isDailyChallenge = false,
                    bonusXp = 0,
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                ProblemDetailScreen(problemId = "1", onBackClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Mark as Solved (+10 XP)").assertExists()
    }

    @Test
    fun solvedState_buttonShowsSolved() {
        val viewModel: ProblemDetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProblemDetailUiState.Loaded(
                    problem = sampleProblem,
                    isSolved = true,
                    isSolving = false,
                    solveResult = null,
                    showSolution = false,
                    isDailyChallenge = false,
                    bonusXp = 0,
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                ProblemDetailScreen(problemId = "1", onBackClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Solved!").assertExists()
    }
}
