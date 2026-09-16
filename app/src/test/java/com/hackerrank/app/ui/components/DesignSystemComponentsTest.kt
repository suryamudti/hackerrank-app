package com.hackerrank.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hackerrank.app.core.theme.AppTheme
import com.hackerrank.app.core.theme.HackerRankTheme
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.ui.components.badge.DifficultyBadge
import com.hackerrank.app.ui.components.badge.DifficultyBadgeSize
import com.hackerrank.app.ui.components.badge.DifficultyBadgeVariant
import com.hackerrank.app.ui.components.badge.DifficultyPill
import com.hackerrank.app.ui.components.badge.StatusBadge
import com.hackerrank.app.ui.components.button.AppButton
import com.hackerrank.app.ui.components.button.AppButtonVariant
import com.hackerrank.app.ui.components.button.BookmarkButton
import com.hackerrank.app.ui.components.card.StatCard
import com.hackerrank.app.ui.components.code.CodeBlock
import com.hackerrank.app.ui.components.code.ExampleCard
import com.hackerrank.app.ui.components.header.SectionHeader
import com.hackerrank.app.ui.components.input.AppSearchBar
import com.hackerrank.app.ui.components.loading.LoadingView
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DesignSystemComponentsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appTheme_providesTokens() {
        var easyColor: Color? = null
        var spacingFound = false

        composeTestRule.setContent {
            HackerRankTheme {
                easyColor = AppTheme.semanticColors.easy
                spacingFound = AppTheme.spacing.medium.value > 0
            }
        }

        assertTrue(easyColor != null)
        assertTrue(spacingFound)
    }

    @Test
    fun difficultyBadge_rendersAllDifficultiesAndVariants() {
        composeTestRule.setContent {
            HackerRankTheme {
                DifficultyBadge(difficulty = Difficulty.EASY, variant = DifficultyBadgeVariant.Subtle)
                DifficultyBadge(difficulty = Difficulty.MEDIUM, variant = DifficultyBadgeVariant.Filled)
                DifficultyBadge(difficulty = Difficulty.HARD, variant = DifficultyBadgeVariant.Outlined, size = DifficultyBadgeSize.Small)
            }
        }

        composeTestRule.onNodeWithText("Easy", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Medium", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Hard", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun statusBadge_rendersStatus() {
        composeTestRule.setContent {
            HackerRankTheme {
                StatusBadge(text = "🏆 Earned", testTag = "myStatusBadge")
            }
        }

        composeTestRule.onNodeWithTag("myStatusBadge").assertIsDisplayed()
        composeTestRule.onNodeWithText("🏆 Earned", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun appSearchBar_displaysPlaceholderAndHandlesClear() {
        var query = "Binary Search"
        var clearClicked = false

        composeTestRule.setContent {
            HackerRankTheme {
                AppSearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    placeholder = "Search something...",
                    testTag = "testSearchBar",
                    onClearClick = { clearClicked = true },
                )
            }
        }

        composeTestRule.onNodeWithTag("testSearchBar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Binary Search").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Clear search").assertIsDisplayed().performClick()
        assertTrue(clearClicked)
    }

    @Test
    fun appButton_enabledAndLoadingStates() {
        var clicked = false
        var isLoading by mutableStateOf(false)

        composeTestRule.setContent {
            HackerRankTheme {
                AppButton(
                    text = "Submit Solution",
                    onClick = { clicked = true },
                    isLoading = isLoading,
                    variant = AppButtonVariant.Primary,
                )
            }
        }

        composeTestRule.onNodeWithText("Submit Solution").assertHasClickAction().performClick()
        assertTrue(clicked)

        // Loading state should hide text and disable button
        isLoading = true
        composeTestRule.onNodeWithText("Submit Solution").assertDoesNotExist()
    }

    @Test
    fun bookmarkButton_togglesProperContentDescription() {
        var clicked = false
        var isBookmarked by mutableStateOf(false)

        composeTestRule.setContent {
            HackerRankTheme {
                BookmarkButton(
                    isBookmarked = isBookmarked,
                    onClick = { clicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Bookmark problem").assertHasClickAction().performClick()
        assertTrue(clicked)

        isBookmarked = true
        composeTestRule.onNodeWithContentDescription("Remove bookmark").assertIsDisplayed()
    }

    @Test
    fun codeBlock_rendersSnippetAndCopyAction() {
        var copied = false

        composeTestRule.setContent {
            HackerRankTheme {
                CodeBlock(
                    code = "val x = 42",
                    onCopy = { copied = true },
                )
            }
        }

        composeTestRule.onNodeWithText("val x = 42").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Copy code").assertHasClickAction().performClick()
        assertTrue(copied)
    }

    @Test
    fun exampleCard_rendersInputOutput() {
        composeTestRule.setContent {
            HackerRankTheme {
                ExampleCard(
                    input = "nums = [2, 7, 11, 15], target = 9",
                    output = "[0, 1]",
                )
            }
        }

        composeTestRule.onNodeWithText("nums = [2, 7, 11, 15], target = 9", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("[0, 1]", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun sectionHeader_rendersTitleIconAndAction() {
        composeTestRule.setContent {
            HackerRankTheme {
                SectionHeader(
                    title = "Problem Overview",
                    icon = Icons.Default.Star,
                    action = { Text("Custom Action") },
                )
            }
        }

        composeTestRule.onNodeWithText("Problem Overview", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Custom Action", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun statCard_rendersMetricValueAndLabel() {
        composeTestRule.setContent {
            HackerRankTheme {
                StatCard(label = "Solved", value = "42")
            }
        }

        composeTestRule.onNodeWithText("42").assertIsDisplayed()
        composeTestRule.onNodeWithText("Solved").assertIsDisplayed()
    }

    @Test
    fun difficultyPill_rendersCounts() {
        composeTestRule.setContent {
            HackerRankTheme {
                DifficultyPill(
                    label = "Easy",
                    solved = 12,
                    total = 20,
                    color = Color.Green,
                )
            }
        }

        composeTestRule.onNodeWithText("Easy").assertIsDisplayed()
        composeTestRule.onNodeWithText("12 / 20").assertIsDisplayed()
    }

    @Test
    fun loadingView_rendersLoadingIndicatorTag() {
        composeTestRule.setContent {
            HackerRankTheme {
                LoadingView()
            }
        }

        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
    }
}
