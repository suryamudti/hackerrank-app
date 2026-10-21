package com.hackerrank.app.ui.cheatsheet

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hackerrank.app.domain.model.CheatsheetCategory
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
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
                category = CheatsheetCategory.TWO_POINTERS_SLIDING_WINDOW,
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
