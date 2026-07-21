package com.hackerrank.app.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hackerrank.app.data.local.HackerRankDatabase
import com.hackerrank.app.data.local.entity.SolvedProblemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SolvedProblemDaoTest {
    private lateinit var db: HackerRankDatabase
    private lateinit var dao: SolvedProblemDao

    @Before
    fun setup() {
        db =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                HackerRankDatabase::class.java,
            ).build()
        dao = db.solvedProblemDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndGetSolvedIds() =
        runTest {
            dao.insert(SolvedProblemEntity("1"))
            dao.insert(SolvedProblemEntity("2"))

            val result = dao.getSolvedIds().first()
            assertEquals(2, result.size)
            assertTrue(result.contains("1"))
            assertTrue(result.contains("2"))
        }

    @Test
    fun getSolvedIds_returnsEmptyWhenNone() =
        runTest {
            val result = dao.getSolvedIds().first()
            assertTrue(result.isEmpty())
        }

    @Test
    fun isSolved_returnsTrueForSolvedProblem() =
        runTest {
            dao.insert(SolvedProblemEntity("1"))

            val result = dao.isSolved("1").first()
            assertTrue(result)
        }

    @Test
    fun isSolved_returnsFalseForUnsolvedProblem() =
        runTest {
            val result = dao.isSolved("unknown").first()
            assertFalse(result)
        }

    @Test
    fun insert_duplicateDoesNotThrow() =
        runTest {
            dao.insert(SolvedProblemEntity("1"))
            dao.insert(SolvedProblemEntity("1"))

            val result = dao.getSolvedIds().first()
            assertEquals(1, result.size)
        }
}
