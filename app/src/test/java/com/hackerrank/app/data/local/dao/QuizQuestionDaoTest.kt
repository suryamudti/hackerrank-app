package com.hackerrank.app.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hackerrank.app.data.local.HackerRankDatabase
import com.hackerrank.app.data.local.entity.DataStructureEntity
import com.hackerrank.app.data.local.entity.QuizQuestionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class QuizQuestionDaoTest {
    private lateinit var db: HackerRankDatabase
    private lateinit var quizQuestionDao: QuizQuestionDao
    private lateinit var dataStructureDao: DataStructureDao

    @Before
    fun setup() {
        db =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                HackerRankDatabase::class.java,
            ).build()
        quizQuestionDao = db.quizQuestionDao()
        dataStructureDao = db.dataStructureDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getQuestionsByStructureId_returnsQuestionsForStructure() =
        runTest {
            dataStructureDao.insertAll(
                listOf(DataStructureEntity("struct1", "Linked List", "linked-list", "Linear", "desc", "{}", "[]", null, "code", "Easy")),
            )
            val questions =
                listOf(
                    QuizQuestionEntity("q1", "struct1", "Q1?", "[\"A\",\"B\"]", 0, "Exp1"),
                    QuizQuestionEntity("q2", "struct1", "Q2?", "[\"A\",\"B\"]", 1, "Exp2"),
                )
            quizQuestionDao.insertAll(questions)

            val result = quizQuestionDao.getQuestionsByStructureId("struct1").first()
            assertEquals(2, result.size)
            assertEquals("Q1?", result[0].question)
        }

    @Test
    fun getQuestionsByStructureId_returnsEmptyForUnknownStructure() =
        runTest {
            val result = quizQuestionDao.getQuestionsByStructureId("unknown").first()
            assertEquals(0, result.size)
        }

    @Test
    fun getQuestionById_returnsCorrectQuestion() =
        runTest {
            dataStructureDao.insertAll(
                listOf(DataStructureEntity("struct1", "Linked List", "linked-list", "Linear", "desc", "{}", "[]", null, "code", "Easy")),
            )
            quizQuestionDao.insertAll(
                listOf(QuizQuestionEntity("q1", "struct1", "Q1?", "[\"A\",\"B\"]", 0, "Exp1")),
            )

            val result = quizQuestionDao.getQuestionById("q1")
            assertNotNull(result)
            assertEquals("Q1?", result?.question)
        }

    @Test
    fun getQuestionById_returnsNullForUnknownId() =
        runTest {
            val result = quizQuestionDao.getQuestionById("unknown")
            assertNull(result)
        }

    @Test
    fun countByStructureId_returnsCorrectCount() =
        runTest {
            dataStructureDao.insertAll(
                listOf(DataStructureEntity("struct1", "Linked List", "linked-list", "Linear", "desc", "{}", "[]", null, "code", "Easy")),
            )
            quizQuestionDao.insertAll(
                listOf(
                    QuizQuestionEntity("q1", "struct1", "Q1?", "[\"A\",\"B\"]", 0, "Exp1"),
                    QuizQuestionEntity("q2", "struct1", "Q2?", "[\"A\",\"B\"]", 1, "Exp2"),
                ),
            )

            val count = quizQuestionDao.countByStructureId("struct1")
            assertEquals(2, count)
        }
}
