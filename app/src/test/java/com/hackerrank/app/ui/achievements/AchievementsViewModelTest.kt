package com.hackerrank.app.ui.achievements

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AchievementsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val progressRepository: ProgressRepository = mockk()

    @Test
    fun `initializing ViewModel loads badges and maps earned states and progress correctly`() = runTest {
        val userProfile = UserProfile(
            totalXp = 5000,
            currentStreak = 4,
            longestStreak = 4,
            lastActiveDate = "2026-07-04",
            // Earned First Steps and Streak Novice (requires streak 3)
            earnedBadgeIds = listOf(BadgeDefinition.FIRST_STEPS.id, BadgeDefinition.STREAK_NOVICE.id)
        )

        every { progressRepository.getProfile() } returns flowOf(userProfile)

        val viewModel = AchievementsViewModel(progressRepository)

        viewModel.uiState.test {
            // First item might be loading, or directly Ready since UnconfinedTestDispatcher executes flow eagerly
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(BadgeDefinition.entries.size, state.badges.size)

            val firstStepsBadge = state.badges.first { it.badge.id == BadgeDefinition.FIRST_STEPS.id }
            assertTrue(firstStepsBadge.isEarned)
            assertEquals("", firstStepsBadge.progress)

            // Streak novice is earned, so progress should be empty/blank
            val streakNoviceBadge = state.badges.first { it.badge.id == BadgeDefinition.STREAK_NOVICE.id }
            assertTrue(streakNoviceBadge.isEarned)
            assertEquals("", streakNoviceBadge.progress)

            // Streak master requires 7, current streak is 4. Not earned, so progress should be "4/7"
            val streakMasterBadge = state.badges.first { it.badge.id == BadgeDefinition.STREAK_MASTER.id }
            assertFalse(streakMasterBadge.isEarned)
            assertEquals("4/7", streakMasterBadge.progress)

            // Level 10 badge requires 10000 XP, current is 5000. Not earned, progress "5000/10000"
            val level10Badge = state.badges.first { it.badge.id == BadgeDefinition.LEVEL_10.id }
            assertFalse(level10Badge.isEarned)
            assertEquals("5000/10000", level10Badge.progress)
        }
    }
}
