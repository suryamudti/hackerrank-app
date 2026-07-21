package com.hackerrank.app.ui.detail

import app.cash.turbine.test
import com.hackerrank.app.MainDispatcherRule
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.usecase.ObserveStructureDetailUseCase
import com.hackerrank.app.domain.usecase.StructureDetailData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeStructureDetailUseCase: ObserveStructureDetailUseCase = mockk()
    private val viewModel = DetailViewModel(observeStructureDetailUseCase)

    @Test
    fun `unknown slug returns null structure and stays in Loading`() =
        runTest {
            every { observeStructureDetailUseCase("unknown") } returns
                flowOf(
                    StructureDetailData(structure = null, progress = null),
                )

            viewModel.loadStructure("unknown")

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is DetailUiState.Loading)
            }
        }

    @Test
    fun `loadStructure fetches structure details and progress correctly`() =
        runTest {
            val structure =
                DataStructure(
                    id = "1",
                    name = "Linked List",
                    slug = "linked-list",
                    category = DataStructureCategory.LINEAR,
                    explanation = "Linear...",
                    complexityTable = emptyMap(),
                    whenToUse = emptyList(),
                    diagramRes = null,
                    codeExample = "",
                    difficulty = Difficulty.EASY,
                )

            val progress =
                UserProgress(
                    structureId = "1",
                    quizzesCompleted = 1,
                    totalCorrect = 4,
                    totalQuestions = 5,
                    bestScore = 4,
                    masteryLevel = 80,
                )

            every { observeStructureDetailUseCase("linked-list") } returns
                flowOf(
                    StructureDetailData(structure = structure, progress = progress),
                )

            viewModel.loadStructure("linked-list")

            viewModel.uiState.test {
                val state = awaitItem() as DetailUiState.Loaded
                assertEquals(structure, state.structure)
                assertEquals(progress, state.progress)
            }
        }
}
