package com.hackerrank.app.data.repository

import app.cash.turbine.test
import com.hackerrank.app.data.local.dao.ProblemDao
import com.hackerrank.app.data.local.dao.SolvedProblemDao
import com.hackerrank.app.data.local.entity.ProblemEntity
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.ProblemCategory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProblemRepositoryImplTest {
    private val problemDao: ProblemDao = mockk()
    private val solvedProblemDao: SolvedProblemDao = mockk()
    private val repository = ProblemRepositoryImpl(problemDao, solvedProblemDao)

    private val sampleProblemEntity =
        ProblemEntity(
            id = "prob1",
            title = "Two Sum",
            description = "Find two numbers...",
            inputExample = "nums = [2,7], target = 9",
            outputExample = "[0,1]",
            solutionCode = "fun twoSum()...",
            approachExplanation = "Use map...",
            difficulty = "Medium",
            category = "Hash-Based",
            orderIndex = 1,
        )

    @Test
    fun `getAllProblems maps entity to domain correctly`() =
        runTest {
            every { problemDao.getAllProblems() } returns flowOf(listOf(sampleProblemEntity))

            repository.getAllProblems().test {
                val list = awaitItem()
                assertEquals(1, list.size)
                val domain = list[0]
                assertEquals("prob1", domain.id)
                assertEquals("Two Sum", domain.title)
                assertEquals("Find two numbers...", domain.description)
                assertEquals("nums = [2,7], target = 9", domain.inputExample)
                assertEquals("[0,1]", domain.outputExample)
                assertEquals("fun twoSum()...", domain.solutionCode)
                assertEquals("Use map...", domain.approachExplanation)
                assertEquals(Difficulty.MEDIUM, domain.difficulty)
                assertEquals(ProblemCategory.HASH_BASED, domain.category)
                assertEquals(1, domain.orderIndex)
                awaitComplete()
            }
        }

    @Test
    fun `getProblemById maps existing problem correctly`() =
        runTest {
            every { problemDao.getProblemById("prob1") } returns flowOf(sampleProblemEntity)

            repository.getProblemById("prob1").test {
                val domain = awaitItem()
                assertNotNull(domain)
                assertEquals("Two Sum", domain?.title)
                awaitComplete()
            }
        }

    @Test
    fun `getProblemById returns null when not found`() =
        runTest {
            every { problemDao.getProblemById("unknown") } returns flowOf(null)

            repository.getProblemById("unknown").test {
                val domain = awaitItem()
                assertNull(domain)
                awaitComplete()
            }
        }

    @Test
    fun `getSolvedIds returns set of solved IDs`() =
        runTest {
            every { solvedProblemDao.getSolvedIds() } returns flowOf(listOf("prob1", "prob2"))

            repository.getSolvedIds().test {
                val set = awaitItem()
                assertEquals(2, set.size)
                assertTrue(set.contains("prob1"))
                assertTrue(set.contains("prob2"))
                awaitComplete()
            }
        }

    @Test
    fun `isSolved returns correct value`() =
        runTest {
            every { solvedProblemDao.isSolved("prob1") } returns flowOf(true)

            repository.isSolved("prob1").test {
                assertTrue(awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `markAsSolved inserts solved problem entity`() =
        runTest {
            coEvery { solvedProblemDao.insert(any()) } returns Unit

            repository.markAsSolved("prob1")

            coVerify {
                solvedProblemDao.insert(
                    withArg {
                        assertEquals("prob1", it.problemId)
                    },
                )
            }
        }
}
