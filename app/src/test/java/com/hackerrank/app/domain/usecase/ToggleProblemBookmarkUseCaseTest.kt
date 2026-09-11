package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.repository.ProblemRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleProblemBookmarkUseCaseTest {
    private val problemRepository: ProblemRepository = mockk()
    private val useCase = ToggleProblemBookmarkUseCase(problemRepository)

    @Test
    fun `invoke calls toggleBookmark on repository`() =
        runTest {
            coEvery { problemRepository.toggleBookmark("prob1") } returns Unit

            useCase("prob1")

            coVerify { problemRepository.toggleBookmark("prob1") }
        }
}
