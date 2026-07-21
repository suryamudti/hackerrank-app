package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ObserveBrowseDataUseCaseTest {
    private val contentRepository: ContentRepository = mockk()
    private val progressRepository: ProgressRepository = mockk()
    private val useCase = ObserveBrowseDataUseCase(contentRepository, progressRepository)

    @Test
    fun `invoke groups structures by category and maps progress`() =
        runTest {
            val structures =
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
            val progressList =
                listOf(
                    UserProgress(structureId = "1", totalCorrect = 8, totalQuestions = 10, masteryLevel = 80),
                )

            every { contentRepository.getAllStructures() } returns flowOf(structures)
            every { progressRepository.getAllProgress() } returns flowOf(progressList)

            val result = useCase().first()

            assertEquals(2, result.groupedStructures.size)
            assertEquals(1, result.groupedStructures[DataStructureCategory.LINEAR]?.size)
            assertEquals("Linked List", result.groupedStructures[DataStructureCategory.LINEAR]?.get(0)?.name)
            assertEquals(0.8f, result.progressMap["1"])
            assertNull(result.progressMap["2"])
        }
}
