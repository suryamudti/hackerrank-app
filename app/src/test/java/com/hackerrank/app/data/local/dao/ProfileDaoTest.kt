package com.hackerrank.app.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hackerrank.app.data.local.HackerRankDatabase
import com.hackerrank.app.data.local.entity.UserProfileEntity
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
class ProfileDaoTest {
    private lateinit var db: HackerRankDatabase
    private lateinit var dao: ProfileDao

    @Before
    fun setup() {
        db =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                HackerRankDatabase::class.java,
            ).build()
        dao = db.profileDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun upsertAndGetProfile() =
        runTest {
            val profile =
                UserProfileEntity(
                    id = 1,
                    totalXp = 1000,
                    currentStreak = 5,
                    longestStreak = 10,
                    lastActiveDate = "2026-07-04",
                    earnedBadgeIdsJson = "[\"badge1\"]",
                )
            dao.upsert(profile)

            val result = dao.getProfile().first()
            assertNotNull(result)
            assertEquals(1000, result?.totalXp)
            assertEquals(5, result?.currentStreak)
        }

    @Test
    fun getProfile_returnsNullWhenNotInserted() =
        runTest {
            val result = dao.getProfile().first()
            assertNull(result)
        }

    @Test
    fun upsert_replacesExistingProfile() =
        runTest {
            dao.upsert(UserProfileEntity(id = 1, totalXp = 500))
            dao.upsert(UserProfileEntity(id = 1, totalXp = 2000))

            val result = dao.getProfile().first()
            assertEquals(2000, result?.totalXp)
        }

    @Test
    fun getProfileSync_returnsSameAsGetProfile() =
        runTest {
            val profile = UserProfileEntity(id = 1, totalXp = 1000, currentStreak = 3)
            dao.upsert(profile)

            val syncResult = dao.getProfileSync()
            val flowResult = dao.getProfile().first()

            assertNotNull(syncResult)
            assertEquals(flowResult?.totalXp, syncResult?.totalXp)
            assertEquals(flowResult?.currentStreak, syncResult?.currentStreak)
        }

    @Test
    fun getProfileSync_returnsNullWhenNoProfile() =
        runTest {
            val result = dao.getProfileSync()
            assertNull(result)
        }
}
