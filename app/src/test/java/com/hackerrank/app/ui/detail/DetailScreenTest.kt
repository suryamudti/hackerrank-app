package com.hackerrank.app.ui.detail

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.*
import androidx.compose.ui.test.onNodeWithText
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.UserProgress
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
class DetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleStructure =
        DataStructure(
            id = "1",
            name = "Linked List",
            slug = "linked-list",
            category = DataStructureCategory.LINEAR,
            explanation = "A linked list is a linear data structure...",
            complexityTable = mapOf("Access" to "O(n)", "Search" to "O(n)", "Insertion" to "O(1)", "Deletion" to "O(1)"),
            whenToUse = listOf("When you need dynamic size", "When frequent insertions/deletions"),
            diagramRes = null,
            codeExample = "class Node { ... }",
            difficulty = Difficulty.EASY,
        )

    @Test
    fun loadedState_displaysExplanationSection() {
        val viewModel: DetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                DetailUiState.Loaded(structure = sampleStructure, progress = null),
            )

        composeTestRule.setContent {
            MaterialTheme {
                DetailScreen(structureSlug = "linked-list", onBackClick = {}, onQuizClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Explanation").assertExists()
        composeTestRule.onNodeWithText("A linked list is a linear data structure...").assertExists()
    }

    @Test
    fun loadedState_displaysComplexityTable() {
        val viewModel: DetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                DetailUiState.Loaded(structure = sampleStructure, progress = null),
            )

        composeTestRule.setContent {
            MaterialTheme {
                DetailScreen(structureSlug = "linked-list", onBackClick = {}, onQuizClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Complexity Table").assertExists()
    }

    @Test
    fun loadedState_displaysCodeExample() {
        val viewModel: DetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                DetailUiState.Loaded(structure = sampleStructure, progress = null),
            )

        composeTestRule.setContent {
            MaterialTheme {
                DetailScreen(structureSlug = "linked-list", onBackClick = {}, onQuizClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Code Example").assertExists()
    }

    @Test
    fun loadedState_displaysTakeQuizButton() {
        val viewModel: DetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                DetailUiState.Loaded(structure = sampleStructure, progress = null),
            )

        composeTestRule.setContent {
            MaterialTheme {
                DetailScreen(structureSlug = "linked-list", onBackClick = {}, onQuizClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Take Quiz").assertExists()
    }

    @Test
    fun loadedState_displaysMasteryProgress() {
        val progress =
            UserProgress(structureId = "1", quizzesCompleted = 2, totalCorrect = 8, totalQuestions = 10, bestScore = 4, masteryLevel = 80)
        val viewModel: DetailViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns
            MutableStateFlow(
                DetailUiState.Loaded(structure = sampleStructure, progress = progress),
            )

        composeTestRule.setContent {
            MaterialTheme {
                DetailScreen(structureSlug = "linked-list", onBackClick = {}, onQuizClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Progress").assertExists()
    }
}
