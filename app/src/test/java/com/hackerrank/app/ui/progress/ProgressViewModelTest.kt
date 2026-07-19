package com.hackerrank.app.ui.progress

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.usecase.ObserveProgressOverviewUseCase
import com.hackerrank.app.domain.usecase.ProgressOverview
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProgressViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeProgressOverviewUseCase: ObserveProgressOverviewUseCase = mockk()

    private val progressList = listOf(
        UserProgress(structureId = "1", quizzesCompleted = 2, totalCorrect = 8, totalQuestions = 10, bestScore = 4, masteryLevel = 80),
        UserProgress(structureId = "2", quizzesCompleted = 1, totalCorrect = 2, totalQuestions = 5, bestScore = 2, masteryLevel = 40)
    )

    private val userProfile = UserProfile(
        totalXp = 1000, currentStreak = 2, longestStreak = 5,
        lastActiveDate = "2026-07-04", earnedBadgeIds = listOf("badge1")
    )

    @Test
    fun `initializing ViewModel computes category mastery and mastered structures count`() = runTest {
        val overview = ProgressOverview(
            profile = userProfile,
            allProgress = progressList,
            categoryMastery = mapOf(
                DataStructureCategory.LINEAR to 0.8f,
                DataStructureCategory.TREES to 0.4f
            ),
            totalStructures = 2,
            masteredStructures = 1
        )

        every { observeProgressOverviewUseCase() } returns flowOf(overview)

        val viewModel = ProgressViewModel(observeProgressOverviewUseCase)

        viewModel.uiState.test {
            val state = awaitItem() as ProgressUiState.Loaded
            assertEquals(userProfile, state.profile)
            assertEquals(progressList, state.allProgress)
            assertEquals(2, state.totalStructures)
            assertEquals(1, state.masteredStructures)
            assertEquals(0.8f, state.categoryMastery[DataStructureCategory.LINEAR] ?: 0f, 0.001f)
            assertEquals(0.4f, state.categoryMastery[DataStructureCategory.TREES] ?: 0f, 0.001f)
        }
    }
}
