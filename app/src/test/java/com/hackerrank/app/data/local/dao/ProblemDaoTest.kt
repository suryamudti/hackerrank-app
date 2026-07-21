package com.hackerrank.app.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hackerrank.app.data.local.HackerRankDatabase
import com.hackerrank.app.data.local.entity.ProblemEntity
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
class ProblemDaoTest {
    private lateinit var db: HackerRankDatabase
    private lateinit var dao: ProblemDao

    @Before
    fun setup() {
        db =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                HackerRankDatabase::class.java,
            ).build()
        dao = db.problemDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getAllProblems_returnsAllProblems() =
        runTest {
            val problems =
                listOf(
                    ProblemEntity("1", "Two Sum", "Find two...", "input", "output", "code", "approach", "Easy", "Hash-Based", 1),
                    ProblemEntity("2", "Reverse Linked List", "Reverse...", "input", "output", "code", "approach", "Medium", "Linked Lists", 2),
                )
            dao.insertAll(problems)

            val result = dao.getAllProblems().first()
            assertEquals(2, result.size)
        }

    @Test
    fun getAllProblems_returnsEmptyWhenNone() =
        runTest {
            val result = dao.getAllProblems().first()
            assertEquals(0, result.size)
        }

    @Test
    fun insertAll_replacesExistingData() =
        runTest {
            val first = listOf(ProblemEntity("1", "Old Title", "desc", "in", "out", "code", "approach", "Easy", "Arrays", 1))
            dao.insertAll(first)
            val second = listOf(ProblemEntity("1", "New Title", "desc", "in", "out", "code", "approach", "Easy", "Arrays", 1))
            dao.insertAll(second)

            val result = dao.getAllProblems().first()
            assertEquals(1, result.size)
            assertEquals("New Title", result[0].title)
        }

    @Test
    fun getProblemById_returnsCorrectProblem() =
        runTest {
            val problems =
                listOf(
                    ProblemEntity("1", "Two Sum", "Find two...", "input", "output", "code", "approach", "Easy", "Hash-Based", 1),
                )
            dao.insertAll(problems)

            val result = dao.getProblemById("1").first()
            assertNotNull(result)
            assertEquals("Two Sum", result?.title)
        }

    @Test
    fun getProblemById_returnsNullForUnknownId() =
        runTest {
            val result = dao.getProblemById("unknown").first()
            assertNull(result)
        }

    @Test
    fun count_returnsCorrectCount() =
        runTest {
            val problems =
                listOf(
                    ProblemEntity("1", "Two Sum", "desc", "in", "out", "code", "approach", "Easy", "Hash-Based", 1),
                    ProblemEntity("2", "Reverse Linked List", "desc", "in", "out", "code", "approach", "Medium", "Linked Lists", 2),
                )
            dao.insertAll(problems)

            val count = dao.count()
            assertEquals(2, count)
        }
}
