package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProfileRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveProgressOverviewUseCaseTest {
    private val profileRepository: ProfileRepository = mockk()
    private val progressRepository: ProgressRepository = mockk()
    private val contentRepository: ContentRepository = mockk()
    private val useCase = ObserveProgressOverviewUseCase(profileRepository, progressRepository, contentRepository)

    private val structures =
        listOf(
            DataStructure(
                id = "1", name = "Linked List", slug = "linked-list",
                category = DataStructureCategory.LINEAR, explanation = "",
                complexityTable = emptyMap(), whenToUse = emptyList(),
                diagramRes = null, codeExample = "", difficulty = Difficulty.EASY,
            ),
            DataStructure(
                id = "2", name = "BST", slug = "bst",
                category = DataStructureCategory.TREES, explanation = "",
                complexityTable = emptyMap(), whenToUse = emptyList(),
                diagramRes = null, codeExample = "", difficulty = Difficulty.MEDIUM,
            ),
        )

    private val progressList =
        listOf(
            UserProgress(structureId = "1", totalCorrect = 8, totalQuestions = 10, masteryLevel = 80),
            UserProgress(structureId = "2", totalCorrect = 2, totalQuestions = 5, masteryLevel = 40),
        )

    private val userProfile =
        UserProfile(
            totalXp = 1000,
            currentStreak = 2,
            longestStreak = 5,
            lastActiveDate = "2026-07-04",
            earnedBadgeIds = listOf("badge1"),
        )

    @Test
    fun `invoke computes category mastery and mastered count`() =
        runTest {
            every { contentRepository.getAllStructures() } returns flowOf(structures)
            every { profileRepository.getProfile() } returns flowOf(userProfile)
            every { progressRepository.getAllProgress() } returns flowOf(progressList)

            val result = useCase().first()

            assertEquals(userProfile, result.profile)
            assertEquals(progressList, result.allProgress)
            assertEquals(2, result.totalStructures)
            assertEquals(1, result.masteredStructures)
            assertEquals(0.8f, result.categoryMastery[DataStructureCategory.LINEAR] ?: 0f, 0.001f)
            assertEquals(0.4f, result.categoryMastery[DataStructureCategory.TREES] ?: 0f, 0.001f)
        }

    @Test
    fun `invoke handles empty progress`() =
        runTest {
            every { contentRepository.getAllStructures() } returns flowOf(structures)
            every { profileRepository.getProfile() } returns flowOf(null)
            every { progressRepository.getAllProgress() } returns flowOf(emptyList())

            val result = useCase().first()

            assertEquals(2, result.totalStructures)
            assertEquals(0, result.masteredStructures)
            assertEquals(0f, result.categoryMastery[DataStructureCategory.LINEAR] ?: 0f, 0.001f)
            assertEquals(0f, result.categoryMastery[DataStructureCategory.TREES] ?: 0f, 0.001f)
        }
}
