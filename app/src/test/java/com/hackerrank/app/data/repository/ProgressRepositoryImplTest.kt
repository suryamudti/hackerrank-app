package com.hackerrank.app.data.repository

import app.cash.turbine.test
import android.content.Context
import com.google.gson.Gson
import com.hackerrank.app.data.local.dao.ProfileDao
import com.hackerrank.app.data.local.dao.ProgressDao
import com.hackerrank.app.data.local.entity.UserProfileEntity
import com.hackerrank.app.data.local.entity.UserProgressEntity
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ProgressRepositoryImplTest {

    private val progressDao: ProgressDao = mockk()
    private val profileDao: ProfileDao = mockk()
    private val gson: Gson = Gson()
    private val context: Context = mockk(relaxed = true)
    private val repository = ProgressRepositoryImpl(progressDao, profileDao, gson, context)

    private val sampleProgressEntity = UserProgressEntity(
        structureId = "struct1",
        quizzesCompleted = 5,
        totalCorrect = 15,
        totalQuestions = 20,
        bestScore = 4,
        masteryLevel = 75
    )

    private val sampleProfileEntity = UserProfileEntity(
        id = 1,
        totalXp = 1200,
        currentStreak = 3,
        longestStreak = 5,
        lastActiveDate = "2026-07-04",
        earnedBadgeIdsJson = "[\"badge1\",\"badge2\"]"
    )

    @Test
    fun `getProgressByStructureId maps entity to domain correctly`() = runTest {
        every { progressDao.getProgressByStructureId("struct1") } returns flowOf(sampleProgressEntity)

        repository.getProgressByStructureId("struct1").test {
            val domain = awaitItem()
            assertNotNull(domain)
            assertEquals("struct1", domain?.structureId)
            assertEquals(5, domain?.quizzesCompleted)
            assertEquals(15, domain?.totalCorrect)
            assertEquals(20, domain?.totalQuestions)
            assertEquals(4, domain?.bestScore)
            assertEquals(75, domain?.masteryLevel)
            awaitComplete()
        }
    }

    @Test
    fun `getProgressByStructureId returns null when not found`() = runTest {
        every { progressDao.getProgressByStructureId("unknown") } returns flowOf(null)

        repository.getProgressByStructureId("unknown").test {
            assertNull(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getAllProgress maps entities to domain list correctly`() = runTest {
        every { progressDao.getAllProgress() } returns flowOf(listOf(sampleProgressEntity))

        repository.getAllProgress().test {
            val list = awaitItem()
            assertEquals(1, list.size)
            assertEquals("struct1", list[0].structureId)
            awaitComplete()
        }
    }

    @Test
    fun `upsertProgress calls progressDao upsert`() = runTest {
        coEvery { progressDao.upsert(any()) } returns Unit

        val progress = UserProgress(
            structureId = "struct1",
            quizzesCompleted = 5,
            totalCorrect = 15,
            totalQuestions = 20,
            bestScore = 4,
            masteryLevel = 75
        )

        repository.upsertProgress(progress)

        coVerify {
            progressDao.upsert(withArg {
                assertEquals("struct1", it.structureId)
                assertEquals(5, it.quizzesCompleted)
                assertEquals(15, it.totalCorrect)
                assertEquals(20, it.totalQuestions)
                assertEquals(4, it.bestScore)
                assertEquals(75, it.masteryLevel)
            })
        }
    }

    @Test
    fun `getProfile maps entity to domain correctly`() = runTest {
        every { profileDao.getProfile() } returns flowOf(sampleProfileEntity)

        repository.getProfile().test {
            val domain = awaitItem()
            assertNotNull(domain)
            assertEquals(1200, domain?.totalXp)
            assertEquals(3, domain?.currentStreak)
            assertEquals(5, domain?.longestStreak)
            assertEquals("2026-07-04", domain?.lastActiveDate)
            assertEquals(2, domain?.earnedBadgeIds?.size)
            assertEquals("badge1", domain?.earnedBadgeIds?.get(0))
            assertEquals("badge2", domain?.earnedBadgeIds?.get(1))
            awaitComplete()
        }
    }

    @Test
    fun `getProfileSync maps entity correctly`() = runTest {
        coEvery { profileDao.getProfileSync() } returns sampleProfileEntity

        val domain = repository.getProfileSync()
        assertNotNull(domain)
        assertEquals(1200, domain?.totalXp)
        assertEquals(2, domain?.earnedBadgeIds?.size)
    }

    @Test
    fun `upsertProfile serializes badges and calls profileDao upsert`() = runTest {
        coEvery { profileDao.upsert(any()) } returns Unit

        val profile = UserProfile(
            totalXp = 1500,
            currentStreak = 4,
            longestStreak = 6,
            lastActiveDate = "2026-07-05",
            earnedBadgeIds = listOf("badge1", "badge3")
        )

        repository.upsertProfile(profile)

        coVerify {
            profileDao.upsert(withArg {
                assertEquals(1, it.id)
                assertEquals(1500, it.totalXp)
                assertEquals(4, it.currentStreak)
                assertEquals(6, it.longestStreak)
                assertEquals("2026-07-05", it.lastActiveDate)
                assertEquals("[\"badge1\",\"badge3\"]", it.earnedBadgeIdsJson)
            })
        }
    }

    @Test
    fun `getMasteredCount returns flow of count`() = runTest {
        every { progressDao.getMasteredCount() } returns flowOf(3)

        repository.getMasteredCount().test {
            assertEquals(3, awaitItem())
            awaitComplete()
        }
    }
}
