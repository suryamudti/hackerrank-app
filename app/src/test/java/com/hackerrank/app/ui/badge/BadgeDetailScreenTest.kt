package com.hackerrank.app.ui.badge

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.usecase.BadgeWithProgress
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BadgeDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadedState_displaysBadgeDetailAndProgress() {
        val viewModel: BadgeDetailViewModel = mockk()
        val badgeProgress =
            BadgeWithProgress(
                badge = Badge("streak_novice", "Streak Novice", "Reach a 3-day streak"),
                isEarned = false,
                currentProgress = 2,
                targetProgress = 3,
            )
        every { viewModel.uiState } returns MutableStateFlow(BadgeDetailUiState.Loaded(badgeProgress))
        every { viewModel.loadBadge(any()) } returns Unit

        composeTestRule.setContent {
            MaterialTheme {
                BadgeDetailScreen(
                    badgeId = "streak_novice",
                    onBackClick = {},
                    viewModel = viewModel,
                )
            }
        }

        composeTestRule.onNodeWithText("Streak Novice").assertExists()
        composeTestRule.onNodeWithText("Reach a 3-day streak").assertExists()
        composeTestRule.onNodeWithTag("statusBadge").assertExists()
        composeTestRule.onNodeWithText("🔒 Locked").assertExists()
        composeTestRule.onNodeWithText("Unlocking Progress").assertExists()
        composeTestRule.onNodeWithText("2 / 3 days").assertExists()
    }

    @Test
    fun backButtonClick_triggersOnBackClick() {
        val viewModel: BadgeDetailViewModel = mockk()
        val badgeProgress =
            BadgeWithProgress(
                badge = Badge("first_steps", "First Steps", "Complete your first quiz"),
                isEarned = true,
                currentProgress = null,
                targetProgress = null,
            )
        every { viewModel.uiState } returns MutableStateFlow(BadgeDetailUiState.Loaded(badgeProgress))
        every { viewModel.loadBadge(any()) } returns Unit

        var backClicked = false
        composeTestRule.setContent {
            MaterialTheme {
                BadgeDetailScreen(
                    badgeId = "first_steps",
                    onBackClick = { backClicked = true },
                    viewModel = viewModel,
                )
            }
        }

        composeTestRule.onNodeWithTag("backButton").performClick()
        assertTrue(backClicked)
    }
}
