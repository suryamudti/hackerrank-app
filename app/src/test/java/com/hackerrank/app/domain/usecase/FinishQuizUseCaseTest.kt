package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.QuizQuestion
import com.hackerrank.app.domain.model.QuizSession
import com.hackerrank.app.domain.model.StreakInfo
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FinishQuizUseCaseTest {

    private val progressRepository: ProgressRepository = mockk()
    private val recordQuizCompleteUseCase: RecordQuizCompleteUseCase = mockk()
    private val useCase = FinishQuizUseCase(progressRepository, recordQuizCompleteUseCase)

    private val questions = listOf(
        QuizQuestion("q1", "struct1", "Q1?", listOf("A", "B"), 0, "Exp1"),
        QuizQuestion("q2", "struct1", "Q2?", listOf("A", "B"), 1, "Exp2")
    )

    @Test
    fun `invoke persists new progress and records gamification`() = runTest {
        val session = QuizSession(questions = questions, answers = mapOf("q1" to 0, "q2" to 0),
            endTimeNanos = System.nanoTime(), isCompleted = true)

        every { progressRepository.getProgressByStructureId("struct1") } returns flowOf(null)
        coEvery { progressRepository.upsertProgress(any()) } returns Unit

        val gamificationResult = GamificationResult(
            xpAwarded = 140, newTotalXp = 240, newLevel = 1, previousLevel = 1,
            newBadges = emptyList(), streakInfo = StreakInfo(1, 1, true, null, "2026-07-04")
        )
        coEvery {
            recordQuizCompleteUseCase(score = 1, totalQuestions = 2, elapsedTimeMs = any(), structureId = "struct1")
        } returns gamificationResult

        val result = useCase(session, "struct1")

        assertEquals("struct1", result.updatedProgress.structureId)
        assertEquals(1, result.updatedProgress.quizzesCompleted)
        assertEquals(1, result.updatedProgress.totalCorrect)
        assertEquals(2, result.updatedProgress.totalQuestions)
        assertEquals(1, result.updatedProgress.bestScore)
        assertEquals(50, result.updatedProgress.masteryLevel)
        assertEquals(gamificationResult, result.gamificationResult)

        coVerify { progressRepository.upsertProgress(any()) }
        coVerify { recordQuizCompleteUseCase(score = 1, totalQuestions = 2, elapsedTimeMs = any(), structureId = "struct1") }
    }

    @Test
    fun `invoke merges with existing progress`() = runTest {
        val session = QuizSession(questions = questions, answers = mapOf("q1" to 0, "q2" to 1),
            endTimeNanos = System.nanoTime(), isCompleted = true)

        val existing = UserProgress(structureId = "struct1", quizzesCompleted = 2, totalCorrect = 8, totalQuestions = 10, bestScore = 4, masteryLevel = 80)

        every { progressRepository.getProgressByStructureId("struct1") } returns flowOf(existing)
        coEvery { progressRepository.upsertProgress(any()) } returns Unit

        val gamificationResult = GamificationResult(
            xpAwarded = 100, newTotalXp = 500, newLevel = 2, previousLevel = 1,
            newBadges = emptyList(), streakInfo = StreakInfo(2, 2, true, null, "2026-07-05")
        )
        coEvery {
            recordQuizCompleteUseCase(score = 2, totalQuestions = 2, elapsedTimeMs = any(), structureId = "struct1")
        } returns gamificationResult

        val result = useCase(session, "struct1")

        assertEquals(3, result.updatedProgress.quizzesCompleted)
        assertEquals(10, result.updatedProgress.totalCorrect)
        assertEquals(12, result.updatedProgress.totalQuestions)
        assertEquals(4, result.updatedProgress.bestScore) // max(4, 2) = 4
        assertEquals(83, result.updatedProgress.masteryLevel) // 10/12 -> 83%
    }
}
