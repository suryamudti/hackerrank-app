package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.repository.ProfileRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ObserveBadgesUseCaseTest {
    private val profileRepository: ProfileRepository = mockk()
    private val progressRepository: ProgressRepository = mockk()
    private val useCase = ObserveBadgesUseCase(profileRepository, progressRepository)

    @Test
    fun `invoke evaluates earned badges and provides progress`() =
        runTest {
            val profile =
                UserProfile(
                    totalXp = 5000,
                    currentStreak = 4,
                    longestStreak = 4,
                    lastActiveDate = "2026-07-04",
                    earnedBadgeIds = listOf(BadgeDefinition.FIRST_STEPS.id, BadgeDefinition.STREAK_NOVICE.id),
                )

            every { profileRepository.getProfile() } returns flowOf(profile)
            every { progressRepository.getAllProgress() } returns flowOf(emptyList())

            val badges = useCase().first()

            assertEquals(BadgeDefinition.entries.size, badges.size)

            val firstSteps = badges.first { it.badge.id == BadgeDefinition.FIRST_STEPS.id }
            assertTrue(firstSteps.isEarned)

            val streakNovice = badges.first { it.badge.id == BadgeDefinition.STREAK_NOVICE.id }
            assertTrue(streakNovice.isEarned)

            val streakMaster = badges.first { it.badge.id == BadgeDefinition.STREAK_MASTER.id }
            assertFalse(streakMaster.isEarned)
            assertEquals(4, streakMaster.currentProgress)
            assertEquals(7, streakMaster.targetProgress)

            val level10 = badges.first { it.badge.id == BadgeDefinition.LEVEL_10.id }
            assertFalse(level10.isEarned)
            assertEquals(5000, level10.currentProgress)
            assertEquals(10000, level10.targetProgress)
        }

    @Test
    fun `invoke returns default progress for non-progress badges`() =
        runTest {
            val profile = UserProfile()

            every { profileRepository.getProfile() } returns flowOf(profile)
            every { progressRepository.getAllProgress() } returns flowOf(emptyList())

            val badges = useCase().first()

            val firstSteps = badges.first { it.badge.id == BadgeDefinition.FIRST_STEPS.id }
            assertEquals(0, firstSteps.currentProgress)
            assertEquals(1, firstSteps.targetProgress)

            val quickLearner = badges.first { it.badge.id == BadgeDefinition.QUICK_LEARNER.id }
            assertEquals(0, quickLearner.currentProgress)
            assertEquals(1, quickLearner.targetProgress)
        }

    @Test
    fun `invoke handles null profile`() =
        runTest {
            every { profileRepository.getProfile() } returns flowOf(null)
            every { progressRepository.getAllProgress() } returns flowOf(emptyList())

            val badges = useCase().first()

            assertFalse(badges.all { it.isEarned })

            val streakNovice = badges.first { it.badge.id == BadgeDefinition.STREAK_NOVICE.id }
            assertEquals(0, streakNovice.currentProgress)
            assertEquals(3, streakNovice.targetProgress)
        }
}
