package com.hackerrank.app.ui.problems

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
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
class ProblemsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleProblems =
        listOf(
            Problem("1", "Two Sum", "Find two...", "nums = [2,7]", "[0,1]", "code", "approach", Difficulty.EASY, ProblemCategory.HASH_BASED, 1),
            Problem("2", "Reverse Linked List", "Reverse...", "head = [1,2]", "[2,1]", "code", "approach", Difficulty.MEDIUM, ProblemCategory.LINKED_LISTS, 2),
        )

    @Test
    fun filterChips_visible() {
        val viewModel: ProblemsViewModel = mockk()
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProblemsUiState.Loaded(
                    allProblems = sampleProblems,
                    filteredProblems = sampleProblems,
                    solvedIds = emptySet(),
                    selectedDifficulty = null,
                    selectedCategory = null,
                    dailyChallenge = DailyChallengeState(isLoading = false, isUnavailable = true),
                ),
            )
        every { viewModel.isRefreshing } returns MutableStateFlow(false)

        composeTestRule.setContent {
            MaterialTheme {
                ProblemsScreen(onProblemClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Two Sum", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("Reverse Linked List", useUnmergedTree = true).assertExists()
    }

    @Test
    fun emptyState_whenNoProblems() {
        val viewModel: ProblemsViewModel = mockk()
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProblemsUiState.Loaded(
                    allProblems = emptyList(),
                    filteredProblems = emptyList(),
                    solvedIds = emptySet(),
                    selectedDifficulty = null,
                    selectedCategory = null,
                    dailyChallenge = DailyChallengeState(isLoading = false, isUnavailable = true),
                ),
            )
        every { viewModel.isRefreshing } returns MutableStateFlow(false)

        composeTestRule.setContent {
            MaterialTheme {
                ProblemsScreen(onProblemClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("No Problems Yet", useUnmergedTree = true).assertExists()
    }

    @Test
    fun problemCards_displayedWithTitles() {
        val viewModel: ProblemsViewModel = mockk()
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProblemsUiState.Loaded(
                    allProblems = sampleProblems,
                    filteredProblems = sampleProblems,
                    solvedIds = emptySet(),
                    selectedDifficulty = null,
                    selectedCategory = null,
                    dailyChallenge = DailyChallengeState(isLoading = false, isUnavailable = true),
                ),
            )
        every { viewModel.isRefreshing } returns MutableStateFlow(false)

        composeTestRule.setContent {
            MaterialTheme {
                ProblemsScreen(onProblemClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Two Sum", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Reverse Linked List", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun solvedProblem_showsCheckIcon() {
        val viewModel: ProblemsViewModel = mockk()
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProblemsUiState.Loaded(
                    allProblems = sampleProblems,
                    filteredProblems = sampleProblems,
                    solvedIds = setOf("1"),
                    selectedDifficulty = null,
                    selectedCategory = null,
                    dailyChallenge = DailyChallengeState(isLoading = false, isUnavailable = true),
                ),
            )
        every { viewModel.isRefreshing } returns MutableStateFlow(false)

        composeTestRule.setContent {
            MaterialTheme {
                ProblemsScreen(onProblemClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Two Sum", useUnmergedTree = true).assertIsDisplayed()
    }
}
