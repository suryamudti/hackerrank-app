package com.hackerrank.app.data.repository

import app.cash.turbine.test
import com.hackerrank.app.data.local.dao.QuizQuestionDao
import com.hackerrank.app.data.local.entity.QuizQuestionEntity
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class QuizRepositoryImplTest {

    private val dao: QuizQuestionDao = mockk()
    private val repository = QuizRepositoryImpl(dao)

    private val sampleQuestionEntity = QuizQuestionEntity(
        id = "q1",
        structureId = "struct1",
        question = "What is a Linked List?",
        optionsJson = "[\"Option A\",\"Option B\",\"Option C\"]",
        correctIndex = 1,
        explanation = "A linked list is..."
    )

    @Test
    fun `getQuestionsByStructureId maps entity to domain correctly`() = runTest {
        every { dao.getQuestionsByStructureId("struct1") } returns flowOf(listOf(sampleQuestionEntity))

        repository.getQuestionsByStructureId("struct1").test {
            val list = awaitItem()
            assertEquals(1, list.size)
            val domain = list[0]
            assertEquals("q1", domain.id)
            assertEquals("struct1", domain.structureId)
            assertEquals("What is a Linked List?", domain.question)
            assertEquals(3, domain.options.size)
            assertEquals("Option A", domain.options[0])
            assertEquals("Option B", domain.options[1])
            assertEquals("Option C", domain.options[2])
            assertEquals(1, domain.correctIndex)
            assertEquals("A linked list is...", domain.explanation)
            awaitComplete()
        }
    }
}
