package com.hackerrank.app.data.repository

import app.cash.turbine.test
import com.hackerrank.app.data.local.dao.DataStructureDao
import com.hackerrank.app.data.local.entity.DataStructureEntity
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import io.mockk.coEvery
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ContentRepositoryImplTest {

    private val dao: DataStructureDao = mockk()
    private val gson: Gson = mockk()
    private val repository = ContentRepositoryImpl(dao, gson)

    private val sampleEntity = DataStructureEntity(
        id = "1",
        name = "Linked List",
        slug = "linked-list",
        category = "Linear",
        explanation = "A linear data structure...",
        complexityJson = "{\"Access\":\"O(n)\",\"Search\":\"O(n)\"}",
        whenToUseJson = "[\"When size is dynamic\",\"Frequent insertion/deletion\"]",
        diagramRes = "diagram",
        codeExample = "class Node...",
        difficulty = "Easy"
    )

    @Test
    fun `getAllStructures maps entity to domain correctly`() = runTest {
        every { dao.getAllStructures() } returns flowOf(listOf(sampleEntity))

        repository.getAllStructures().test {
            val list = awaitItem()
            assertEquals(1, list.size)
            val domain = list[0]
            assertEquals("1", domain.id)
            assertEquals("Linked List", domain.name)
            assertEquals("linked-list", domain.slug)
            assertEquals(DataStructureCategory.LINEAR, domain.category)
            assertEquals("A linear data structure...", domain.explanation)
            assertEquals("O(n)", domain.complexityTable["Access"])
            assertEquals("O(n)", domain.complexityTable["Search"])
            assertEquals(2, domain.whenToUse.size)
            assertEquals("When size is dynamic", domain.whenToUse[0])
            assertEquals("diagram", domain.diagramRes)
            assertEquals("class Node...", domain.codeExample)
            assertEquals(Difficulty.EASY, domain.difficulty)
            awaitComplete()
        }
    }

    @Test
    fun `getStructuresByCategory filters and maps correctly`() = runTest {
        every { dao.getStructuresByCategory("Linear") } returns flowOf(listOf(sampleEntity))

        repository.getStructuresByCategory(DataStructureCategory.LINEAR).test {
            val list = awaitItem()
            assertEquals(1, list.size)
            assertEquals("Linked List", list[0].name)
            awaitComplete()
        }
    }

    @Test
    fun `getStructureBySlug returns mapped structure when found`() = runTest {
        coEvery { dao.getStructureBySlug("linked-list") } returns sampleEntity

        val result = repository.getStructureBySlug("linked-list")
        assertNotNull(result)
        assertEquals("Linked List", result?.name)
    }

    @Test
    fun `getStructureBySlug returns null when not found`() = runTest {
        coEvery { dao.getStructureBySlug("unknown") } returns null

        val result = repository.getStructureBySlug("unknown")
        assertNull(result)
    }

    @Test
    fun `getAllCategories maps strings to category enums`() = runTest {
        every { dao.getAllCategories() } returns flowOf(listOf("Linear", "Trees", "Non-existent"))

        repository.getAllCategories().test {
            val list = awaitItem()
            assertEquals(2, list.size)
            assertEquals(DataStructureCategory.LINEAR, list[0])
            assertEquals(DataStructureCategory.TREES, list[1])
            awaitComplete()
        }
    }
}
