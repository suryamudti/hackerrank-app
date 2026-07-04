package com.hackerrank.app.ui.detail

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val contentRepository: ContentRepository = mockk()
    private val progressRepository: ProgressRepository = mockk()
    private val viewModel = DetailViewModel(contentRepository, progressRepository)

    @Test
    fun `loadStructure fetches structure details and progress correctly`() = runTest {
        val structure = DataStructure(
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
        )

        val progress = UserProgress(
            structureId = "1",
            quizzesCompleted = 1,
            totalCorrect = 4,
            totalQuestions = 5,
            bestScore = 4,
            masteryLevel = 80
        )

        coEvery { contentRepository.getStructureBySlug("linked-list") } returns structure
        every { progressRepository.getProgressByStructureId("1") } returns flowOf(progress)
        every { progressRepository.getProfile() } returns flowOf(UserProfile())

        viewModel.loadStructure("linked-list")

        viewModel.uiState.test {
            // First item emitted could be initial loading state, or populated depending on collection
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(structure, state.structure)
            assertEquals(progress, state.progress)
        }
    }
}
