package com.hackerrank.app.ui.achievements

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.usecase.BadgeWithProgress
import com.hackerrank.app.domain.usecase.ObserveBadgesUseCase
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

    private val observeBadgesUseCase: ObserveBadgesUseCase = mockk()

    @Test
    fun `initializing ViewModel loads badges and maps earned states and progress correctly`() = runTest {
        val badgeWithProgressList = listOf(
            BadgeWithProgress(Badge(BadgeDefinition.FIRST_STEPS.id, "First Steps", "Complete your first quiz"), isEarned = true, currentProgress = null, targetProgress = null),
            BadgeWithProgress(Badge(BadgeDefinition.QUICK_LEARNER.id, "Quick Learner", "Get a perfect score on any quiz"), isEarned = false, currentProgress = null, targetProgress = null),
            BadgeWithProgress(Badge(BadgeDefinition.SPEED_DEMON.id, "Speed Demon", "Complete a quiz in under 60 seconds"), isEarned = false, currentProgress = null, targetProgress = null),
            BadgeWithProgress(Badge(BadgeDefinition.STREAK_NOVICE.id, "Streak Novice", "Reach a 3-day streak"), isEarned = true, currentProgress = 4, targetProgress = 3),
            BadgeWithProgress(Badge(BadgeDefinition.STREAK_MASTER.id, "Streak Master", "Reach a 7-day streak"), isEarned = false, currentProgress = 4, targetProgress = 7),
            BadgeWithProgress(Badge(BadgeDefinition.STREAK_LEGEND.id, "Streak Legend", "Reach a 30-day streak"), isEarned = false, currentProgress = 4, targetProgress = 30),
            BadgeWithProgress(Badge(BadgeDefinition.ARRAY_ACE.id, "Array Ace", "Master all array quizzes"), isEarned = false, currentProgress = null, targetProgress = null),
            BadgeWithProgress(Badge(BadgeDefinition.TREE_WHISPERER.id, "Tree Whisperer", "Master all tree quizzes"), isEarned = false, currentProgress = null, targetProgress = null),
            BadgeWithProgress(Badge(BadgeDefinition.GRAPH_GURU.id, "Graph Guru", "Master all graph quizzes"), isEarned = false, currentProgress = null, targetProgress = null),
            BadgeWithProgress(Badge(BadgeDefinition.COMPLETIONIST.id, "Completionist", "Master every data structure"), isEarned = false, currentProgress = null, targetProgress = null),
            BadgeWithProgress(Badge(BadgeDefinition.LEVEL_10.id, "Level 10", "Reach level 10"), isEarned = false, currentProgress = 5000, targetProgress = 10000),
            BadgeWithProgress(Badge(BadgeDefinition.LEVEL_25.id, "Level 25", "Reach level 25"), isEarned = false, currentProgress = 5000, targetProgress = 62500),
            BadgeWithProgress(Badge(BadgeDefinition.LEVEL_50.id, "Level 50", "Reach level 50"), isEarned = false, currentProgress = 5000, targetProgress = 250000)
        )

        every { observeBadgesUseCase() } returns flowOf(badgeWithProgressList)

        val viewModel = AchievementsViewModel(observeBadgesUseCase)

        viewModel.uiState.test {
            val state = awaitItem() as AchievementsUiState.Loaded
            assertEquals(BadgeDefinition.entries.size, state.badges.size)

            val firstStepsBadge = state.badges.first { it.badge.id == BadgeDefinition.FIRST_STEPS.id }
            assertTrue(firstStepsBadge.isEarned)
            assertEquals("", firstStepsBadge.progress)

            val streakNoviceBadge = state.badges.first { it.badge.id == BadgeDefinition.STREAK_NOVICE.id }
            assertTrue(streakNoviceBadge.isEarned)
            assertEquals("", streakNoviceBadge.progress)

            val streakMasterBadge = state.badges.first { it.badge.id == BadgeDefinition.STREAK_MASTER.id }
            assertFalse(streakMasterBadge.isEarned)
            assertEquals("4/7", streakMasterBadge.progress)

            val level10Badge = state.badges.first { it.badge.id == BadgeDefinition.LEVEL_10.id }
            assertFalse(level10Badge.isEarned)
            assertEquals("5000/10000", level10Badge.progress)
        }
    }
}
