package com.hackerrank.app.ui.cheatsheet

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hackerrank.app.domain.model.CategoryGuide
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
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
class CheatsheetScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleCheatsheets =
        listOf(
            CheatsheetItem(
                id = "cs1",
                title = "Two Pointers Technique",
                category = ProblemCategory.ARRAYS,
                summary = "Two pointers summary text",
                whenToUse = listOf("Sorted array"),
                keyTricks = listOf("Left and right"),
                codeTemplate = "fun template() {}",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p001"),
            ),
        )

    private val sampleRules =
        listOf(
            ComplexityRule(
                constraint = "N <= 10",
                maxTimeComplexity = "O(N!)",
                algorithms = "Permutations",
            ),
        )

    private val sampleGuides =
        mapOf(
            ProblemCategory.ARRAYS to
                CategoryGuide(
                    category = ProblemCategory.ARRAYS,
                    title = "Arrays Survival Guide",
                    coreConcept = "Contiguous elements in memory",
                    timeSpaceTargets = "O(N) time, O(1) space",
                    essentialKotlinTools = listOf("IntArray"),
                    criticalEdgeCases = listOf("Empty array"),
                    topPatterns = listOf("Two Pointers"),
                ),
        )

    @Test
    fun loadedState_displaysCardsAndSearchField() {
        val viewModel: CheatsheetViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                CheatsheetUiState.Loaded(
                    allCheatsheets = sampleCheatsheets,
                    filteredCheatsheets = sampleCheatsheets,
                    bookmarkedIds = emptySet(),
                    complexityRules = sampleRules,
                    searchQuery = "",
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                CheatsheetScreen(onProblemClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithTag("cheatsheetSearchField").assertIsDisplayed()
        composeTestRule.onNodeWithText("Two Pointers Technique", useUnmergedTree = true).assertExists()
    }

    @Test
    fun categorySelected_displaysCategorySurvivalGuide() {
        val viewModel: CheatsheetViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                CheatsheetUiState.Loaded(
                    allCheatsheets = sampleCheatsheets,
                    filteredCheatsheets = sampleCheatsheets,
                    bookmarkedIds = emptySet(),
                    complexityRules = sampleRules,
                    categoryGuides = sampleGuides,
                    selectedCategory = ProblemCategory.ARRAYS,
                    searchQuery = "",
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                CheatsheetScreen(onProblemClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Arrays Survival Guide", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("Contiguous elements in memory", useUnmergedTree = true).assertExists()
    }

    @Test
    fun emptyState_displaysWhenNoMatches() {
        val viewModel: CheatsheetViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                CheatsheetUiState.Loaded(
                    allCheatsheets = sampleCheatsheets,
                    filteredCheatsheets = emptyList(),
                    bookmarkedIds = emptySet(),
                    complexityRules = sampleRules,
                    searchQuery = "nonexistent",
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                CheatsheetScreen(onProblemClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("No Cheatsheets Found", useUnmergedTree = true).assertExists()
    }
}
