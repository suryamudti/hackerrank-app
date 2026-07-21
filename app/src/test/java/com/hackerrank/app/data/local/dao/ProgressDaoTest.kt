package com.hackerrank.app.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hackerrank.app.data.local.HackerRankDatabase
import com.hackerrank.app.data.local.entity.UserProgressEntity
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
class ProgressDaoTest {
    private lateinit var db: HackerRankDatabase
    private lateinit var dao: ProgressDao

    @Before
    fun setup() {
        db =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                HackerRankDatabase::class.java,
            ).build()
        dao = db.progressDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun upsertAndGetProgressByStructureId() =
        runTest {
            val progress =
                UserProgressEntity("struct1", quizzesCompleted = 2, totalCorrect = 8, totalQuestions = 10, bestScore = 4, masteryLevel = 80)
            dao.upsert(progress)

            val result = dao.getProgressByStructureId("struct1").first()
            assertNotNull(result)
            assertEquals(2, result?.quizzesCompleted)
            assertEquals(80, result?.masteryLevel)
        }

    @Test
    fun getProgressByStructureId_returnsNullForUnknown() =
        runTest {
            val result = dao.getProgressByStructureId("unknown").first()
            assertNull(result)
        }

    @Test
    fun upsert_updatesExistingProgress() =
        runTest {
            val progress = UserProgressEntity("struct1", quizzesCompleted = 1, masteryLevel = 50)
            dao.upsert(progress)
            val updated = UserProgressEntity("struct1", quizzesCompleted = 2, masteryLevel = 80)
            dao.upsert(updated)

            val result = dao.getProgressByStructureId("struct1").first()
            assertEquals(2, result?.quizzesCompleted)
            assertEquals(80, result?.masteryLevel)
        }

    @Test
    fun getAllProgress_returnsAll() =
        runTest {
            val list =
                listOf(
                    UserProgressEntity("struct1", quizzesCompleted = 2, masteryLevel = 80),
                    UserProgressEntity("struct2", quizzesCompleted = 1, masteryLevel = 50),
                )
            list.forEach { dao.upsert(it) }

            val result = dao.getAllProgress().first()
            assertEquals(2, result.size)
        }

    @Test
    fun getAllProgress_returnsEmptyWhenNone() =
        runTest {
            val result = dao.getAllProgress().first()
            assertEquals(0, result.size)
        }

    @Test
    fun getMasteredCount_returnsCorrectCount() =
        runTest {
            val list =
                listOf(
                    UserProgressEntity("struct1", masteryLevel = 120),
                    UserProgressEntity("struct2", masteryLevel = 100),
                    UserProgressEntity("struct3", masteryLevel = 50),
                )
            list.forEach { dao.upsert(it) }

            val count = dao.getMasteredCount().first()
            assertEquals(2, count)
        }
}
