package com.hackerrank.app.ui.progress

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.UserProfile
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

class ProgressViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val progressRepository: ProgressRepository = mockk()
    private val contentRepository: ContentRepository = mockk()

    private val structures = listOf(
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

    private val progressList = listOf(
        UserProgress(
            structureId = "1",
            quizzesCompleted = 2,
            totalCorrect = 8,
            totalQuestions = 10,
            bestScore = 4,
            masteryLevel = 80
        ),
        UserProgress(
            structureId = "2",
            quizzesCompleted = 1,
            totalCorrect = 2,
            totalQuestions = 5,
            bestScore = 2,
            masteryLevel = 40
        )
    )

    private val userProfile = UserProfile(
        totalXp = 1000,
        currentStreak = 2,
        longestStreak = 5,
        lastActiveDate = "2026-07-04",
        earnedBadgeIds = listOf("badge1")
    )

    @Test
    fun `initializing ViewModel computes category mastery and mastered structures count`() = runTest {
        every { contentRepository.getAllStructures() } returns flowOf(structures)
        every { progressRepository.getProfile() } returns flowOf(userProfile)
        every { progressRepository.getAllProgress() } returns flowOf(progressList)

        val viewModel = ProgressViewModel(progressRepository, contentRepository)

        viewModel.uiState.test {
            val state = awaitItem() as ProgressUiState.Loaded
            assertEquals(userProfile, state.profile)
            assertEquals(progressList, state.allProgress)
            assertEquals(2, state.totalStructures)

            // Mastery count where masteryLevel >= 80. "1" is 80 (mastered), "2" is 40 (not mastered).
            assertEquals(1, state.masteredStructures)

            // Category mastery check:
            // LINEAR: 8 correct / 10 total = 0.8
            // TREES: 2 correct / 5 total = 0.4
            assertEquals(0.8f, state.categoryMastery[DataStructureCategory.LINEAR] ?: 0f, 0.001f)
            assertEquals(0.4f, state.categoryMastery[DataStructureCategory.TREES] ?: 0f, 0.001f)
        }
    }
}
