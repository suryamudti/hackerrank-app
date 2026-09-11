package com.hackerrank.app.ui.browse

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.usecase.BrowseData
import com.hackerrank.app.domain.usecase.ObserveBrowseDataUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BrowseViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeBrowseDataUseCase: ObserveBrowseDataUseCase = mockk()

    @Test
    fun `initializing ViewModel loads structures and maps progress successfully`() =
        runTest {
            val structures =
                listOf(
                    DataStructure(
                        id = "1",
                        name = "Linked List",
                        slug = "linked-list",
                        category = DataStructureCategory.LINEAR,
                        explanation = "Linear...",
                        complexityTable = emptyMap(),
                        whenToUse = emptyList(),
                        diagramRes = null,
                        codeExample = "",
                        difficulty = Difficulty.EASY,
                    ),
                    DataStructure(
                        id = "2",
                        name = "BST",
                        slug = "bst",
                        category = DataStructureCategory.TREES,
                        explanation = "Tree...",
                        complexityTable = emptyMap(),
                        whenToUse = emptyList(),
                        diagramRes = null,
                        codeExample = "",
                        difficulty = Difficulty.MEDIUM,
                    ),
                )

            val browseData =
                BrowseData(
                    groupedStructures =
                        mapOf(
                            DataStructureCategory.LINEAR to listOf(structures[0]),
                            DataStructureCategory.TREES to listOf(structures[1]),
                        ),
                    progressMap = mapOf("1" to 0.8f),
                )

            every { observeBrowseDataUseCase() } returns flowOf(browseData)

            val viewModel = BrowseViewModel(observeBrowseDataUseCase)

            viewModel.uiState.test {
                val state = awaitItem() as BrowseUiState.Loaded

                assertEquals(2, state.groupedStructures.size)
                assertEquals(1, state.groupedStructures[DataStructureCategory.LINEAR]?.size)
                assertEquals("Linked List", state.groupedStructures[DataStructureCategory.LINEAR]?.get(0)?.name)

                assertEquals(0.8f, state.progressMap["1"])
                assertEquals(null, state.progressMap["2"])
            }
        }

    @Test
    fun `onSearchQueryChanged filters grouped structures by name or explanation`() =
        runTest {
            val structures =
                listOf(
                    DataStructure(
                        id = "1",
                        name = "Linked List",
                        slug = "linked-list",
                        category = DataStructureCategory.LINEAR,
                        explanation = "Sequence of elements",
                        complexityTable = emptyMap(),
                        whenToUse = emptyList(),
                        diagramRes = null,
                        codeExample = "",
                        difficulty = Difficulty.EASY,
                    ),
                    DataStructure(
                        id = "2",
                        name = "Binary Search Tree",
                        slug = "bst",
                        category = DataStructureCategory.TREES,
                        explanation = "Hierarchical tree structure",
                        complexityTable = emptyMap(),
                        whenToUse = emptyList(),
                        diagramRes = null,
                        codeExample = "",
                        difficulty = Difficulty.MEDIUM,
                    ),
                )

            val browseData =
                BrowseData(
                    groupedStructures =
                        mapOf(
                            DataStructureCategory.LINEAR to listOf(structures[0]),
                            DataStructureCategory.TREES to listOf(structures[1]),
                        ),
                    progressMap = emptyMap(),
                )

            every { observeBrowseDataUseCase() } returns flowOf(browseData)

            val viewModel = BrowseViewModel(observeBrowseDataUseCase)

            viewModel.uiState.test {
                var state = awaitItem() as BrowseUiState.Loaded
                assertEquals(2, state.groupedStructures.size)

                viewModel.onSearchQueryChanged("tree")
                state = awaitItem() as BrowseUiState.Loaded
                assertEquals(1, state.groupedStructures.size)
                assertEquals(1, state.groupedStructures[DataStructureCategory.TREES]?.size)
                assertEquals("Binary Search Tree", state.groupedStructures[DataStructureCategory.TREES]?.get(0)?.name)

                viewModel.onSearchQueryChanged("")
                state = awaitItem() as BrowseUiState.Loaded
                assertEquals(2, state.groupedStructures.size)
            }
        }
}
