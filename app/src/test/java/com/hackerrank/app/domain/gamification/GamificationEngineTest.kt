package com.hackerrank.app.domain.gamification

import com.hackerrank.app.core.Constants
import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GamificationEngineTest {

    private val progressRepository: ProgressRepository = mockk()
    private val engine = GamificationEngine(progressRepository)
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @Test
    fun `recordLogin updates streak and daily login XP`() = runTest {
        val yesterday = LocalDate.now().minusDays(1).format(dateFormatter)
        val initialProfile = UserProfile(
            totalXp = 100,
            currentStreak = 2,
            longestStreak = 2,
            lastActiveDate = yesterday,
            earnedBadgeIds = emptyList()
        )

        coEvery { progressRepository.getProfileSync() } returns initialProfile
        coEvery { progressRepository.upsertProfile(any()) } returns Unit

        val result = engine.recordLogin()

        assertEquals(Constants.DAILY_LOGIN_XP, result.xpAwarded)
        assertEquals(110, result.newTotalXp)
        assertEquals(3, result.streakInfo.currentStreak)
        assertTrue(result.streakInfo.isNewStreakDay)

        coVerify {
            progressRepository.upsertProfile(withArg {
                assertEquals(110, it.totalXp)
                assertEquals(3, it.currentStreak)
                assertEquals(3, it.longestStreak)
                assertEquals(LocalDate.now().format(dateFormatter), it.lastActiveDate)
            })
        }
    }

    @Test
    fun `recordQuizComplete calculates correct XP and badges`() = runTest {
        val today = LocalDate.now().format(dateFormatter)
        val initialProfile = UserProfile(
            totalXp = 100,
            currentStreak = 1,
            longestStreak = 1,
            lastActiveDate = today, // Streak won't increment since already active today
            earnedBadgeIds = emptyList()
        )

        coEvery { progressRepository.getProfileSync() } returns initialProfile
        every { progressRepository.getAllProgress() } returns flowOf(listOf(UserProgress("struct1", quizzesCompleted = 1)))
        coEvery { progressRepository.upsertProfile(any()) } returns Unit

        // perfect score (50 bonus), under speed threshold (20 bonus), daily quiz (20) + base (50) = 140 XP
        val result = engine.recordQuizComplete(
            score = 8,
            totalQuestions = 8,
            elapsedTimeMs = 30_000L,
            structureId = "struct1"
        )

        assertEquals(140, result.xpAwarded)
        assertEquals(240, result.newTotalXp)
        assertEquals(1, result.streakInfo.currentStreak)
        assertFalse(result.streakInfo.isNewStreakDay)

        // Should award First Steps (completed quiz), Quick Learner (perfect score), Speed Demon (< 60s)
        val badgeIds = result.newBadges.map { it.id }
        assertEquals(3, badgeIds.size)
        assertTrue(badgeIds.contains(BadgeDefinition.FIRST_STEPS.id))
        assertTrue(badgeIds.contains(BadgeDefinition.QUICK_LEARNER.id))
        assertTrue(badgeIds.contains(BadgeDefinition.SPEED_DEMON.id))
    }

    @Test
    fun `recordQuizComplete awards streak milestone XP bonus`() = runTest {
        val yesterday = LocalDate.now().minusDays(1).format(dateFormatter)
        // Set streak to 2, so completing today will reach 3 (milestone: 3 -> 100 XP bonus)
        val initialProfile = UserProfile(
            totalXp = 100,
            currentStreak = 2,
            longestStreak = 2,
            lastActiveDate = yesterday,
            earnedBadgeIds = emptyList()
        )

        coEvery { progressRepository.getProfileSync() } returns initialProfile
        every { progressRepository.getAllProgress() } returns flowOf(emptyList())
        coEvery { progressRepository.upsertProfile(any()) } returns Unit

        // non-perfect score (50 base + 20 daily quiz) + milestone bonus (100) = 170 XP
        val result = engine.recordQuizComplete(
            score = 6,
            totalQuestions = 8,
            elapsedTimeMs = 90_000L,
            structureId = "struct1"
        )

        assertEquals(170, result.xpAwarded)
        assertEquals(270, result.newTotalXp)
        assertEquals(3, result.streakInfo.currentStreak)
        assertEquals(3, result.streakInfo.streakMilestoneReached)
    }

    @Test
    fun `recordDailyChallengeCompleted awards problem XP plus bonus XP`() = runTest {
        val today = LocalDate.now().format(dateFormatter)
        val initialProfile = UserProfile(
            totalXp = 100,
            currentStreak = 1,
            longestStreak = 1,
            lastActiveDate = today,
            earnedBadgeIds = emptyList()
        )

        coEvery { progressRepository.getProfileSync() } returns initialProfile
        coEvery { progressRepository.upsertProfile(any()) } returns Unit

        val result = engine.recordDailyChallengeCompleted(Difficulty.EASY, Constants.DAILY_CHALLENGE_BONUS_XP)

        val expectedXp = Constants.PROBLEM_EASY_XP + Constants.DAILY_CHALLENGE_BONUS_XP
        assertEquals(expectedXp, result.xpAwarded)
        assertEquals(100 + expectedXp, result.newTotalXp)

        coVerify {
            progressRepository.upsertProfile(withArg {
                assertEquals(100 + expectedXp, it.totalXp)
            })
        }
    }

    @Test
    fun `recordProblemSolved awards correct XP based on difficulty`() = runTest {
        val today = LocalDate.now().format(dateFormatter)
        val initialProfile = UserProfile(
            totalXp = 100,
            currentStreak = 1,
            longestStreak = 1,
            lastActiveDate = today,
            earnedBadgeIds = emptyList()
        )

        coEvery { progressRepository.getProfileSync() } returns initialProfile
        coEvery { progressRepository.upsertProfile(any()) } returns Unit

        val resultEasy = engine.recordProblemSolved(Difficulty.EASY)
        assertEquals(Constants.PROBLEM_EASY_XP, resultEasy.xpAwarded)

        val resultMedium = engine.recordProblemSolved(Difficulty.MEDIUM)
        assertEquals(Constants.PROBLEM_MEDIUM_XP, resultMedium.xpAwarded)

        val resultHard = engine.recordProblemSolved(Difficulty.HARD)
        assertEquals(Constants.PROBLEM_HARD_XP, resultHard.xpAwarded)
    }

    @Test
    fun `updateStreak resets streak if last active is before yesterday`() = runTest {
        val twoDaysAgo = LocalDate.now().minusDays(2).format(dateFormatter)
        val initialProfile = UserProfile(
            totalXp = 100,
            currentStreak = 5,
            longestStreak = 5,
            lastActiveDate = twoDaysAgo,
            earnedBadgeIds = emptyList()
        )

        coEvery { progressRepository.getProfileSync() } returns initialProfile
        coEvery { progressRepository.upsertProfile(any()) } returns Unit

        val result = engine.recordLogin()

        assertEquals(1, result.streakInfo.currentStreak)
        assertEquals(5, result.streakInfo.longestStreak) // longest streak doesn't decrease
    }

    @Test
    fun `recordQuizComplete awards ARRAY_ACE when all linear structures are mastered`() = runTest {
        val today = LocalDate.now().format(dateFormatter)
        val initialProfile = UserProfile(
            totalXp = 100,
            currentStreak = 1,
            longestStreak = 1,
            lastActiveDate = today,
            earnedBadgeIds = emptyList()
        )

        coEvery { progressRepository.getProfileSync() } returns initialProfile
        val progressList = listOf(
            UserProgress("array", masteryLevel = 85),
            UserProgress("linked_list", masteryLevel = 90),
            UserProgress("stack", masteryLevel = 80),
            UserProgress("queue", masteryLevel = 80)
        )
        every { progressRepository.getAllProgress() } returns flowOf(progressList)
        coEvery { progressRepository.upsertProfile(any()) } returns Unit

        val result = engine.recordQuizComplete(
            score = 8,
            totalQuestions = 8,
            elapsedTimeMs = 90_000L,
            structureId = "queue"
        )

        val badgeIds = result.newBadges.map { it.id }
        assertTrue(badgeIds.contains(BadgeDefinition.ARRAY_ACE.id))
    }

    @Test
    fun `recordQuizComplete awards COMPLETIONIST when all structures are mastered`() = runTest {
        val today = LocalDate.now().format(dateFormatter)
        val initialProfile = UserProfile(
            totalXp = 100,
            currentStreak = 1,
            longestStreak = 1,
            lastActiveDate = today,
            earnedBadgeIds = emptyList()
        )

        coEvery { progressRepository.getProfileSync() } returns initialProfile
        val allIds = listOf(
            "array", "linked_list", "stack", "queue",
            "binary_tree", "bst", "avl_tree", "heap", "trie",
            "graph", "weighted_graph", "graph_algorithms",
            "hash_table", "hash_set",
            "disjoint_set", "segment_tree"
        )
        val progressList = allIds.map { UserProgress(it, masteryLevel = 80) }
        every { progressRepository.getAllProgress() } returns flowOf(progressList)
        coEvery { progressRepository.upsertProfile(any()) } returns Unit

        val result = engine.recordQuizComplete(
            score = 8,
            totalQuestions = 8,
            elapsedTimeMs = 90_000L,
            structureId = "segment_tree"
        )

        val badgeIds = result.newBadges.map { it.id }
        assertTrue(badgeIds.contains(BadgeDefinition.COMPLETIONIST.id))
        assertTrue(badgeIds.contains(BadgeDefinition.ARRAY_ACE.id))
        assertTrue(badgeIds.contains(BadgeDefinition.TREE_WHISPERER.id))
        assertTrue(badgeIds.contains(BadgeDefinition.GRAPH_GURU.id))
    }
}
