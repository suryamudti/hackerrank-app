package com.hackerrank.app.ui.progress

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.UserProfile
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
class ProgressScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadedState_displaysLevelSection() {
        val viewModel: ProgressViewModel = mockk()
        val profile = UserProfile(totalXp = 0, currentStreak = 0, longestStreak = 0, lastActiveDate = "", earnedBadgeIds = emptyList())
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProgressUiState.Loaded(
                    profile = profile,
                    allProgress = emptyList(),
                    categoryMastery = emptyMap(),
                    totalStructures = 0,
                    masteredStructures = 0,
                    recentActivities = emptyList(),
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                ProgressScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Level 0").assertExists()
    }

    @Test
    fun loadedState_displaysStreakSection() {
        val viewModel: ProgressViewModel = mockk()
        val profile =
            UserProfile(totalXp = 1000, currentStreak = 2, longestStreak = 5, lastActiveDate = "2026-07-04", earnedBadgeIds = emptyList())
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProgressUiState.Loaded(
                    profile = profile,
                    allProgress = emptyList(),
                    categoryMastery = emptyMap(),
                    totalStructures = 0,
                    masteredStructures = 0,
                    recentActivities = emptyList(),
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                ProgressScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Streak").assertExists()
    }

    @Test
    fun loadedState_displaysCategoryMastery() {
        val viewModel: ProgressViewModel = mockk()
        val profile =
            UserProfile(totalXp = 100, currentStreak = 1, longestStreak = 1, lastActiveDate = "2026-07-04", earnedBadgeIds = emptyList())
        every { viewModel.uiState } returns
            MutableStateFlow(
                ProgressUiState.Loaded(
                    profile = profile,
                    allProgress = listOf(UserProgress("1", totalCorrect = 8, totalQuestions = 10, masteryLevel = 80)),
                    categoryMastery = mapOf(DataStructureCategory.LINEAR to 0.8f),
                    totalStructures = 1,
                    masteredStructures = 1,
                    recentActivities = emptyList(),
                ),
            )

        composeTestRule.setContent {
            MaterialTheme {
                ProgressScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Category Progress").assertExists()
    }
}
