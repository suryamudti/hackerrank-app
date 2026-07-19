package com.hackerrank.app.ui.browse

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class BrowseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val contentRepository: ContentRepository = mockk()
    private val progressRepository: ProgressRepository = mockk()

    @Test
    fun `initializing ViewModel loads structures and maps progress successfully`() = runTest {
        val structures = listOf(
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
                difficulty = Difficulty.EASY
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
                difficulty = Difficulty.MEDIUM
            )
        )

        val progressList = listOf(
            UserProgress(
                structureId = "1",
                quizzesCompleted = 2,
                totalCorrect = 8,
                totalQuestions = 10,
                bestScore = 4,
                masteryLevel = 80 // masteryPercentage calculates to masteryLevel
            )
        )

        every { contentRepository.getAllStructures() } returns flowOf(structures)
        every { progressRepository.getAllProgress() } returns flowOf(progressList)

        val viewModel = BrowseViewModel(contentRepository, progressRepository)

        viewModel.uiState.test {
            val state = awaitItem() as BrowseUiState.Loaded

            // Verify grouped structures
            assertEquals(2, state.groupedStructures.size)
            assertEquals(1, state.groupedStructures[DataStructureCategory.LINEAR]?.size)
            assertEquals("Linked List", state.groupedStructures[DataStructureCategory.LINEAR]?.get(0)?.name)

            // Verify progress map: progress percentage = masteryLevel / 100f = 0.8f
            assertEquals(0.8f, state.progressMap["1"])
            assertEquals(null, state.progressMap["2"])
        }
    }
}
