package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.repository.CheatsheetRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleCheatsheetBookmarkUseCaseTest {
    private val repository: CheatsheetRepository = mockk()
    private val useCase = ToggleCheatsheetBookmarkUseCase(repository)

    @Test
    fun `invoking useCase delegates to repository`() =
        runTest {
            coEvery { repository.toggleBookmark("cs_test") } returns Unit

            useCase("cs_test")

            coVerify(exactly = 1) { repository.toggleBookmark("cs_test") }
        }
}
