package com.hackerrank.app.ui.quiz

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.QuizQuestion
import com.hackerrank.app.domain.model.QuizSession
import com.hackerrank.app.domain.model.StreakInfo
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.QuizRepository
import com.hackerrank.app.domain.usecase.FinishQuizResult
import com.hackerrank.app.domain.usecase.FinishQuizUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class QuizViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val contentRepository: ContentRepository = mockk()
    private val quizRepository: QuizRepository = mockk()
    private val finishQuizUseCase: FinishQuizUseCase = mockk()

    private val viewModel = QuizViewModel(
        contentRepository,
        quizRepository,
        finishQuizUseCase
    )

    private val sampleStructure = DataStructure(
        id = "struct1",
        name = "Linked List",
        slug = "linked-list",
        category = DataStructureCategory.LINEAR,
        explanation = "Linear...",
        complexityTable = emptyMap(),
        whenToUse = emptyList(),
        diagramRes = null,
        codeExample = "",
        difficulty = Difficulty.EASY
    )

    private val sampleQuestions = listOf(
        QuizQuestion("q1", "struct1", "Q1?", listOf("A", "B"), 0, "Exp1"),
        QuizQuestion("q2", "struct1", "Q2?", listOf("A", "B"), 1, "Exp2")
    )

    @Test
    fun `quiz flow loads questions, allows selections, advances, and completes successfully`() = runTest {
        coEvery { contentRepository.getStructureBySlug("linked-list") } returns sampleStructure
        every { quizRepository.getQuestionsByStructureId("struct1") } returns flowOf(sampleQuestions)

        val gamificationResult = GamificationResult(
            xpAwarded = 140,
            newTotalXp = 240,
            newLevel = 1,
            previousLevel = 1,
            newBadges = emptyList(),
            streakInfo = StreakInfo(1, 1, true, null, "2026-07-04")
        )
        coEvery {
            finishQuizUseCase(session = any(), structureId = "struct1")
        } returns FinishQuizResult(
            updatedProgress = UserProgress(structureId = "struct1", quizzesCompleted = 1, totalCorrect = 1, totalQuestions = 2, bestScore = 1, masteryLevel = 50),
            gamificationResult = gamificationResult
        )

        // 1. Load Quiz
        viewModel.loadQuiz("linked-list")

        viewModel.uiState.test {
            // Loading, then Ready
            var state = awaitItem()
            assertTrue(state is QuizState.Ready)
            val readySession = (state as QuizState.Ready).session
            assertEquals(2, readySession.questions.size)
            assertEquals(0, readySession.currentIndex)

            // 2. Select answer for Q1 (Correct Option 0)
            viewModel.selectAnswer("q1", 0)
            state = awaitItem()
            assertTrue(state is QuizState.Answering)
            val answeringState = state as QuizState.Answering
            assertEquals(0, answeringState.selectedAnswer)
            assertTrue(answeringState.showExplanation)

            // 3. Move to next question
            viewModel.nextQuestion()
            state = awaitItem()
            assertTrue(state is QuizState.Ready)
            assertEquals(1, (state as QuizState.Ready).session.currentIndex)

            // 4. Select answer for Q2 (Incorrect Option 0, correct is 1)
            viewModel.selectAnswer("q2", 0)
            state = awaitItem()
            assertTrue(state is QuizState.Answering)

            // 5. Next question (triggers finish since it's the last question)
            viewModel.nextQuestion()
            state = awaitItem()
            assertTrue(state is QuizState.Completed)
            val completedState = state as QuizState.Completed
            assertEquals(1, completedState.session.score) // 1 out of 2 correct
            assertEquals(2, completedState.session.totalQuestions)
            assertTrue(completedState.session.isCompleted)
            assertEquals(gamificationResult, completedState.gamificationResult)

            // Verify finishQuizUseCase was called
            coVerify(exactly = 1) {
                finishQuizUseCase(session = any(), structureId = "struct1")
            }
        }
    }

    @Test
    fun `retry shuffles questions and resets session`() = runTest {
        coEvery { contentRepository.getStructureBySlug("linked-list") } returns sampleStructure
        every { quizRepository.getQuestionsByStructureId("struct1") } returns flowOf(sampleQuestions)

        coEvery {
            finishQuizUseCase(session = any(), structureId = "struct1")
        } returns FinishQuizResult(
            updatedProgress = UserProgress(structureId = "struct1"),
            gamificationResult = mockk()
        )

        viewModel.loadQuiz("linked-list")
        viewModel.selectAnswer("q1", 0)
        viewModel.nextQuestion()
        viewModel.selectAnswer("q2", 0)
        viewModel.nextQuestion() // Completes quiz

        viewModel.uiState.test {
            var state = awaitItem()
            assertTrue(state is QuizState.Completed)

            // Retry
            viewModel.retry()
            state = awaitItem()
            assertTrue(state is QuizState.Ready)
            val newSession = (state as QuizState.Ready).session
            assertEquals(0, newSession.currentIndex)
            assertTrue(newSession.answers.isEmpty())
            assertFalse(newSession.isCompleted)
        }
    }
}
