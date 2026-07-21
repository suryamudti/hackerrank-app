package com.hackerrank.app.ui.quiz

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.hackerrank.app.domain.model.QuizQuestion
import com.hackerrank.app.domain.model.QuizSession
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class QuizScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleQuestions =
        listOf(
            QuizQuestion("q1", "struct1", "What is a linked list?", listOf("A", "B", "C", "D"), 0, "A linked list is..."),
            QuizQuestion("q2", "struct1", "What is a BST?", listOf("A", "B", "C", "D"), 1, "BST is..."),
        )

    @Test
    fun readyState_displaysQuestionText() {
        val viewModel: QuizViewModel = mockk(relaxed = true)
        val session = QuizSession(questions = sampleQuestions)
        every { viewModel.uiState } returns MutableStateFlow(QuizState.Ready(session))

        composeTestRule.setContent {
            MaterialTheme {
                QuizScreen(structureSlug = "linked-list", onBackClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("What is a linked list?").assertExists()
    }

    @Test
    fun readyState_displaysOptionButtons() {
        val viewModel: QuizViewModel = mockk(relaxed = true)
        val session = QuizSession(questions = sampleQuestions)
        every { viewModel.uiState } returns MutableStateFlow(QuizState.Ready(session))

        composeTestRule.setContent {
            MaterialTheme {
                QuizScreen(structureSlug = "linked-list", onBackClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("A. A").assertExists()
        composeTestRule.onNodeWithText("B. B").assertExists()
        composeTestRule.onNodeWithText("C. C").assertExists()
        composeTestRule.onNodeWithText("D. D").assertExists()
    }

    @Test
    fun answeringState_showsExplanation() {
        val viewModel: QuizViewModel = mockk(relaxed = true)
        val session = QuizSession(questions = sampleQuestions, answers = mapOf("q1" to 0))
        every { viewModel.uiState } returns
            MutableStateFlow(
                QuizState.Answering(session = session, selectedAnswer = 0, showExplanation = true),
            )

        composeTestRule.setContent {
            MaterialTheme {
                QuizScreen(structureSlug = "linked-list", onBackClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("A linked list is...").assertExists()
    }

    @Test
    fun completedState_showsScore() {
        val viewModel: QuizViewModel = mockk(relaxed = true)
        val session =
            QuizSession(
                questions = sampleQuestions,
                answers = mapOf("q1" to 0, "q2" to 1),
                endTimeNanos = System.nanoTime(),
                isCompleted = true,
            )
        every { viewModel.uiState } returns
            MutableStateFlow(
                QuizState.Completed(session = session, gamificationResult = null),
            )

        composeTestRule.setContent {
            MaterialTheme {
                QuizScreen(structureSlug = "linked-list", onBackClick = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("2/2").assertExists()
    }
}
