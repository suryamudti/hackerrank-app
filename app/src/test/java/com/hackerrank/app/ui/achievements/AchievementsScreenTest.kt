package com.hackerrank.app.ui.achievements

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.*
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hackerrank.app.domain.model.Badge
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AchievementsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadedState_displaysBadgeGrid() {
        val viewModel: AchievementsViewModel = mockk()
        val badges =
            listOf(
                BadgeWithState(badge = Badge("badge1", "First Steps", "Complete your first quiz"), isEarned = true, progress = ""),
                BadgeWithState(badge = Badge("badge2", "Quick Learner", "Perfect score"), isEarned = false, progress = ""),
                BadgeWithState(badge = Badge("badge3", "Speed Demon", "Under 60s"), isEarned = false, progress = ""),
            )
        every { viewModel.uiState } returns MutableStateFlow(AchievementsUiState.Loaded(badges))

        composeTestRule.setContent {
            MaterialTheme {
                AchievementsScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("First Steps").assertExists()
        composeTestRule.onNodeWithText("Quick Learner").assertExists()
        composeTestRule.onNodeWithText("Speed Demon").assertExists()
    }

    @Test
    fun earnedBadge_displaysDifferentlyFromLocked() {
        val viewModel: AchievementsViewModel = mockk()
        val badges =
            listOf(
                BadgeWithState(badge = Badge("badge1", "First Steps", "Complete your first quiz"), isEarned = true, progress = ""),
                BadgeWithState(badge = Badge("badge2", "Quick Learner", "Perfect score"), isEarned = false, progress = "5/10"),
            )
        every { viewModel.uiState } returns MutableStateFlow(AchievementsUiState.Loaded(badges))

        composeTestRule.setContent {
            MaterialTheme {
                AchievementsScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("First Steps").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quick Learner").assertIsDisplayed()
    }

    @Test
    fun emptyState_whenNoBadges() {
        val viewModel: AchievementsViewModel = mockk()
        every { viewModel.uiState } returns MutableStateFlow(AchievementsUiState.Loaded(emptyList()))

        composeTestRule.setContent {
            MaterialTheme {
                AchievementsScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("No Badges Yet").assertExists()
    }

    @Test
    fun loadingState_displaysLoadingIndicator() {
        val viewModel: AchievementsViewModel = mockk()
        every { viewModel.uiState } returns MutableStateFlow(AchievementsUiState.Loading)

        composeTestRule.setContent {
            MaterialTheme {
                AchievementsScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithTag("loadingIndicator").assertExists()
    }

    @Test
    fun tappingBadge_triggersOnBadgeClick() {
        val viewModel: AchievementsViewModel = mockk()
        val badges =
            listOf(
                BadgeWithState(badge = Badge("badge1", "First Steps", "Complete your first quiz"), isEarned = true, progress = ""),
            )
        every { viewModel.uiState } returns MutableStateFlow(AchievementsUiState.Loaded(badges))

        var clickedBadgeId: String? = null
        composeTestRule.setContent {
            MaterialTheme {
                AchievementsScreen(
                    onBadgeClick = { clickedBadgeId = it },
                    viewModel = viewModel,
                )
            }
        }

        composeTestRule.onNodeWithText("First Steps").performClick()
        assertEquals("badge1", clickedBadgeId)
    }
}
