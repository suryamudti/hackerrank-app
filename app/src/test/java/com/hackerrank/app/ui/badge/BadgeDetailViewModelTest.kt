package com.hackerrank.app.ui.badge

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.usecase.BadgeWithProgress
import com.hackerrank.app.domain.usecase.ObserveBadgesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BadgeDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeBadgesUseCase: ObserveBadgesUseCase = mockk()

    @Test
    fun `loadBadge loads correct badge state successfully`() =
        runTest {
            val badges =
                listOf(
                    BadgeWithProgress(
                        Badge("first_steps", "First Steps", "Complete your first quiz"),
                        isEarned = true,
                        currentProgress = 1,
                        targetProgress = 1,
                    ),
                    BadgeWithProgress(
                        Badge("quick_learner", "Quick Learner", "Perfect score"),
                        isEarned = false,
                        currentProgress = 0,
                        targetProgress = 1,
                    ),
                )
            every { observeBadgesUseCase() } returns flowOf(badges)

            val viewModel = BadgeDetailViewModel(observeBadgesUseCase)
            viewModel.loadBadge("first_steps")

            viewModel.uiState.test {
                val state = awaitItem() as BadgeDetailUiState.Loaded
                assertEquals("first_steps", state.badgeProgress.badge.id)
                assertEquals("First Steps", state.badgeProgress.badge.title)
                assertEquals(true, state.badgeProgress.isEarned)
            }
        }

    @Test
    fun `loadBadge exposes error when badge not found`() =
        runTest {
            every { observeBadgesUseCase() } returns flowOf(emptyList())

            val viewModel = BadgeDetailViewModel(observeBadgesUseCase)
            viewModel.loadBadge("unknown_badge")

            viewModel.uiState.test {
                val state = awaitItem() as BadgeDetailUiState.Error
                assertEquals("Badge not found", state.message)
            }
        }
}
