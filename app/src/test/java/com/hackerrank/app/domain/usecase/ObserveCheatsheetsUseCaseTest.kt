package com.hackerrank.app.domain.usecase

import app.cash.turbine.test
import com.hackerrank.app.domain.model.CheatsheetCategory
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
import com.hackerrank.app.domain.repository.CheatsheetRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ObserveCheatsheetsUseCaseTest {
    private val repository: CheatsheetRepository = mockk()
    private val useCase = ObserveCheatsheetsUseCase(repository)

    private val sampleCheatsheet =
        CheatsheetItem(
            id = "cs1",
            title = "Two Pointers",
            category = CheatsheetCategory.TWO_POINTERS_SLIDING_WINDOW,
            summary = "Two pointers summary",
            whenToUse = listOf("Sorted array"),
            keyTricks = listOf("Left and right"),
            codeTemplate = "fun template() {}",
            timeComplexity = "O(N)",
            spaceComplexity = "O(1)",
            relatedProblemIds = listOf("p001"),
        )

    private val sampleRule =
        ComplexityRule(
            constraint = "N <= 10",
            maxTimeComplexity = "O(N!)",
            algorithms = "Permutations",
        )

    @Test
    fun `combines cheatsheets and bookmarked IDs successfully`() =
        runTest {
            every { repository.getAllCheatsheets() } returns flowOf(listOf(sampleCheatsheet))
            every { repository.getBookmarkedCheatsheetIds() } returns flowOf(setOf("cs1"))
            every { repository.getComplexityRules() } returns listOf(sampleRule)

            useCase().test {
                val item = awaitItem()
                assertEquals(1, item.cheatsheets.size)
                assertEquals("cs1", item.cheatsheets[0].id)
                assertTrue(item.bookmarkedIds.contains("cs1"))
                assertEquals(1, item.complexityRules.size)
                awaitComplete()
            }
        }
}
