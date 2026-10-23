package com.hackerrank.app.ui.cheatsheet

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.CategoryGuide
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.usecase.CheatsheetsData
import com.hackerrank.app.domain.usecase.ObserveCheatsheetsUseCase
import com.hackerrank.app.domain.usecase.ToggleCheatsheetBookmarkUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheatsheetViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeCheatsheetsUseCase: ObserveCheatsheetsUseCase = mockk()
    private val toggleCheatsheetBookmarkUseCase: ToggleCheatsheetBookmarkUseCase = mockk(relaxed = true)

    private val sampleItems =
        listOf(
            CheatsheetItem(
                id = "cs1",
                title = "Two Pointers Technique",
                category = ProblemCategory.ARRAYS,
                summary = "Two pointers summary",
                whenToUse = listOf("Sorted array"),
                keyTricks = listOf("Left and right bounds"),
                codeTemplate = "code1",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p001"),
            ),
            CheatsheetItem(
                id = "cs2",
                title = "Binary Search",
                category = ProblemCategory.SORTING_SEARCHING,
                summary = "Binary search halving space",
                whenToUse = listOf("Monotonic condition"),
                keyTricks = listOf("mid calculation"),
                codeTemplate = "code2",
                timeComplexity = "O(log N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p079"),
            ),
        )

    private val sampleGuides =
        mapOf(
            ProblemCategory.ARRAYS to
                CategoryGuide(
                    category = ProblemCategory.ARRAYS,
                    title = "Arrays Survival Guide",
                    coreConcept = "Contiguous memory layout",
                    timeSpaceTargets = "O(N) time, O(1) space",
                    essentialKotlinTools = listOf("IntArray", "indices"),
                    criticalEdgeCases = listOf("Empty array", "Duplicates"),
                    topPatterns = listOf("Two Pointers", "Prefix Sum"),
                ),
        )

    private val sampleData =
        CheatsheetsData(
            cheatsheets = sampleItems,
            bookmarkedIds = setOf("cs1"),
            complexityRules = listOf(ComplexityRule("N <= 10", "O(N!)", "Backtracking")),
            categoryGuides = sampleGuides,
        )

    @Before
    fun setup() {
        every { observeCheatsheetsUseCase() } returns flowOf(sampleData)
    }

    @Test
    fun `initial uiState is loaded with all items`() =
        runTest {
            val viewModel = CheatsheetViewModel(SavedStateHandle(), observeCheatsheetsUseCase, toggleCheatsheetBookmarkUseCase)

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is CheatsheetUiState.Loaded)
                val loaded = state as CheatsheetUiState.Loaded
                assertEquals(2, loaded.allCheatsheets.size)
                assertEquals(2, loaded.filteredCheatsheets.size)
                assertTrue(loaded.bookmarkedIds.contains("cs1"))
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `savedStateHandle category initializes selectedCategory`() =
        runTest {
            val handle = SavedStateHandle(mapOf("category" to "ARRAYS"))
            val viewModel = CheatsheetViewModel(handle, observeCheatsheetsUseCase, toggleCheatsheetBookmarkUseCase)

            viewModel.uiState.test {
                val state = awaitItem() as CheatsheetUiState.Loaded
                assertEquals(ProblemCategory.ARRAYS, state.selectedCategory)
                assertEquals(1, state.filteredCheatsheets.size)
                assertEquals("cs1", state.filteredCheatsheets[0].id)
                assertNotNull(state.activeCategoryGuide)
                assertEquals("Arrays Survival Guide", state.activeCategoryGuide?.title)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `search filters items by title and summary`() =
        runTest {
            val viewModel = CheatsheetViewModel(SavedStateHandle(), observeCheatsheetsUseCase, toggleCheatsheetBookmarkUseCase)

            viewModel.uiState.test {
                awaitItem() // initial
                viewModel.onSearchQueryChanged("Binary")

                val state = awaitItem() as CheatsheetUiState.Loaded
                assertEquals(1, state.filteredCheatsheets.size)
                assertEquals("cs2", state.filteredCheatsheets[0].id)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `category selection filters items by category`() =
        runTest {
            val viewModel = CheatsheetViewModel(SavedStateHandle(), observeCheatsheetsUseCase, toggleCheatsheetBookmarkUseCase)

            viewModel.uiState.test {
                awaitItem() // initial
                viewModel.onCategorySelected(ProblemCategory.SORTING_SEARCHING)

                val state = awaitItem() as CheatsheetUiState.Loaded
                assertEquals(1, state.filteredCheatsheets.size)
                assertEquals(ProblemCategory.SORTING_SEARCHING, state.filteredCheatsheets[0].category)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `bookmark filter displays only saved items`() =
        runTest {
            val viewModel = CheatsheetViewModel(SavedStateHandle(), observeCheatsheetsUseCase, toggleCheatsheetBookmarkUseCase)

            viewModel.uiState.test {
                awaitItem() // initial
                viewModel.onToggleBookmarkedOnly()

                val state = awaitItem() as CheatsheetUiState.Loaded
                assertEquals(1, state.filteredCheatsheets.size)
                assertEquals("cs1", state.filteredCheatsheets[0].id)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `toggleCardExpanded expands and collapses card`() =
        runTest {
            val viewModel = CheatsheetViewModel(SavedStateHandle(), observeCheatsheetsUseCase, toggleCheatsheetBookmarkUseCase)

            viewModel.uiState.test {
                val initial = awaitItem() as CheatsheetUiState.Loaded
                assertFalse(initial.expandedCardIds.contains("cs1"))

                viewModel.toggleCardExpanded("cs1")
                val expanded = awaitItem() as CheatsheetUiState.Loaded
                assertTrue(expanded.expandedCardIds.contains("cs1"))

                viewModel.toggleCardExpanded("cs1")
                val collapsed = awaitItem() as CheatsheetUiState.Loaded
                assertFalse(collapsed.expandedCardIds.contains("cs1"))
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `toggleComplexityGuide flips guide visibility`() =
        runTest {
            val viewModel = CheatsheetViewModel(SavedStateHandle(), observeCheatsheetsUseCase, toggleCheatsheetBookmarkUseCase)

            viewModel.uiState.test {
                val initial = awaitItem() as CheatsheetUiState.Loaded
                assertTrue(initial.showComplexityGuide)

                viewModel.toggleComplexityGuide()
                val toggled = awaitItem() as CheatsheetUiState.Loaded
                assertFalse(toggled.showComplexityGuide)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `toggleCategoryGuide flips category guide visibility`() =
        runTest {
            val viewModel = CheatsheetViewModel(SavedStateHandle(), observeCheatsheetsUseCase, toggleCheatsheetBookmarkUseCase)

            viewModel.uiState.test {
                val initial = awaitItem() as CheatsheetUiState.Loaded
                assertTrue(initial.showCategoryGuide)

                viewModel.toggleCategoryGuide()
                val toggled = awaitItem() as CheatsheetUiState.Loaded
                assertFalse(toggled.showCategoryGuide)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `toggleBookmark triggers use case`() =
        runTest {
            val viewModel = CheatsheetViewModel(SavedStateHandle(), observeCheatsheetsUseCase, toggleCheatsheetBookmarkUseCase)

            viewModel.toggleBookmark("cs1")

            coVerify(exactly = 1) { toggleCheatsheetBookmarkUseCase("cs1") }
        }
}
