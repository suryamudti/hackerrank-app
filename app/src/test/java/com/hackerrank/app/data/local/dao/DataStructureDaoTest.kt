package com.hackerrank.app.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hackerrank.app.data.local.HackerRankDatabase
import com.hackerrank.app.data.local.entity.DataStructureEntity
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
class DataStructureDaoTest {
    private lateinit var db: HackerRankDatabase
    private lateinit var dao: DataStructureDao

    @Before
    fun setup() {
        db =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                HackerRankDatabase::class.java,
            ).build()
        dao = db.dataStructureDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getAllStructures_returnsAllStructures() =
        runTest {
            val structures =
                listOf(
                    DataStructureEntity("1", "Linked List", "linked-list", "Linear", "desc", "{}", "[]", null, "code", "Easy"),
                    DataStructureEntity("2", "BST", "bst", "Trees", "desc", "{}", "[]", null, "code", "Medium"),
                )
            dao.insertAll(structures)

            val result = dao.getAllStructures().first()
            assertEquals(2, result.size)
        }

    @Test
    fun getStructureBySlug_returnsCorrectStructure() =
        runTest {
            val structures =
                listOf(
                    DataStructureEntity("1", "Linked List", "linked-list", "Linear", "desc", "{}", "[]", null, "code", "Easy"),
                    DataStructureEntity("2", "BST", "bst", "Trees", "desc", "{}", "[]", null, "code", "Medium"),
                )
            dao.insertAll(structures)

            val result = dao.getStructureBySlug("bst")
            assertNotNull(result)
            assertEquals("BST", result?.name)
        }

    @Test
    fun getStructureBySlug_returnsNullForUnknownSlug() =
        runTest {
            val result = dao.getStructureBySlug("unknown")
            assertNull(result)
        }

    @Test
    fun count_returnsCorrectCount() =
        runTest {
            val structures =
                listOf(
                    DataStructureEntity("1", "Linked List", "linked-list", "Linear", "desc", "{}", "[]", null, "code", "Easy"),
                    DataStructureEntity("2", "BST", "bst", "Trees", "desc", "{}", "[]", null, "code", "Medium"),
                )
            dao.insertAll(structures)

            val count = dao.count()
            assertEquals(2, count)
        }

    @Test
    fun count_returnsZeroWhenEmpty() =
        runTest {
            val count = dao.count()
            assertEquals(0, count)
        }
}
