package com.hackerrank.app.data.repository

import app.cash.turbine.test
import com.hackerrank.app.data.local.dao.BookmarkedCheatsheetDao
import com.hackerrank.app.domain.model.CheatsheetCategory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheatsheetRepositoryImplTest {
    private val dao: BookmarkedCheatsheetDao = mockk(relaxed = true)
    private lateinit var repository: CheatsheetRepositoryImpl

    @Before
    fun setup() {
        repository = CheatsheetRepositoryImpl(dao)
    }

    @Test
    fun `getAllCheatsheets returns all seed items`() =
        runTest {
            repository.getAllCheatsheets().test {
                val list = awaitItem()
                assertTrue(list.isNotEmpty())
                assertTrue(list.any { it.id == "cs_two_pointers" })
                awaitComplete()
            }
        }

    @Test
    fun `getCheatsheetById returns matched item`() =
        runTest {
            repository.getCheatsheetById("cs_sliding_window").test {
                val item = awaitItem()
                assertNotNull(item)
                assertEquals("Sliding Window Pattern", item?.title)
                awaitComplete()
            }
        }

    @Test
    fun `getCheatsheetsByCategory returns category items`() =
        runTest {
            repository.getCheatsheetsByCategory(CheatsheetCategory.BINARY_SEARCH).test {
                val list = awaitItem()
                assertTrue(list.isNotEmpty())
                assertTrue(list.all { it.category == CheatsheetCategory.BINARY_SEARCH })
                awaitComplete()
            }
        }

    @Test
    fun `getBookmarkedCheatsheetIds converts dao list to set`() =
        runTest {
            every { dao.getBookmarkedIds() } returns flowOf(listOf("cs_1", "cs_2"))

            repository.getBookmarkedCheatsheetIds().test {
                val set = awaitItem()
                assertEquals(setOf("cs_1", "cs_2"), set)
                awaitComplete()
            }
        }

    @Test
    fun `toggleBookmark inserts when not bookmarked`() =
        runTest {
            coEvery { dao.isBookmarkedSync("cs_new") } returns false

            repository.toggleBookmark("cs_new")

            coVerify(exactly = 1) { dao.insert(match { it.cheatsheetId == "cs_new" }) }
        }

    @Test
    fun `toggleBookmark deletes when already bookmarked`() =
        runTest {
            coEvery { dao.isBookmarkedSync("cs_existing") } returns true

            repository.toggleBookmark("cs_existing")

            coVerify(exactly = 1) { dao.delete("cs_existing") }
        }
}
