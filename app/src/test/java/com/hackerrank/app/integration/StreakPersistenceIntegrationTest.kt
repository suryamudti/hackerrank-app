package com.hackerrank.app.integration

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.hackerrank.app.core.Constants
import com.hackerrank.app.data.local.HackerRankDatabase
import com.hackerrank.app.data.local.entity.UserProfileEntity
import com.hackerrank.app.data.repository.ProgressRepositoryImpl
import com.hackerrank.app.domain.gamification.GamificationEngine
import com.hackerrank.app.domain.usecase.RecordLoginUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
class StreakPersistenceIntegrationTest {
    private lateinit var db: HackerRankDatabase
    private lateinit var progressRepository: ProgressRepositoryImpl
    private lateinit var gamificationEngine: GamificationEngine
    private lateinit var recordLoginUseCase: RecordLoginUseCase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room.inMemoryDatabaseBuilder(context, HackerRankDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        progressRepository =
            ProgressRepositoryImpl(
                progressDao = db.progressDao(),
                profileDao = db.profileDao(),
                quizResultDao = db.quizResultDao(),
                gson = Gson(),
                context = context,
            )
        gamificationEngine =
            GamificationEngine(
                profileRepository = progressRepository,
                progressRepository = progressRepository,
            )
        recordLoginUseCase = RecordLoginUseCase(gamificationEngine)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun streakIncrementsAndResetsCorrectly() =
        runTest {
            val todayStr = LocalDate.now().toString()
            val yesterdayStr = LocalDate.now().minusDays(1).toString()
            val twoDaysAgoStr = LocalDate.now().minusDays(2).toString()

            val result1 = recordLoginUseCase()
            assertEquals(1, result1.streakInfo.currentStreak)
            assertEquals(todayStr, result1.streakInfo.lastActiveDate)
            assertEquals(Constants.DAILY_LOGIN_XP, result1.xpAwarded)

            val profileDb1 = progressRepository.getProfile().first()
            assertEquals(1, profileDb1?.currentStreak)
            assertEquals(todayStr, profileDb1?.lastActiveDate)

            db.profileDao().upsert(
                UserProfileEntity(
                    id = 1,
                    totalXp = profileDb1?.totalXp ?: 10,
                    currentStreak = 1,
                    longestStreak = 1,
                    lastActiveDate = yesterdayStr,
                    earnedBadgeIdsJson = "[]",
                ),
            )

            val result2 = recordLoginUseCase()
            assertEquals(2, result2.streakInfo.currentStreak)
            assertEquals(todayStr, result2.streakInfo.lastActiveDate)

            val profileDb2 = progressRepository.getProfile().first()
            assertEquals(2, profileDb2?.currentStreak)

            db.profileDao().upsert(
                UserProfileEntity(
                    id = 1,
                    totalXp = profileDb2?.totalXp ?: 20,
                    currentStreak = 2,
                    longestStreak = 2,
                    lastActiveDate = yesterdayStr,
                    earnedBadgeIdsJson = "[]",
                ),
            )

            val result3 = recordLoginUseCase()
            assertEquals(3, result3.streakInfo.currentStreak)

            db.profileDao().upsert(
                UserProfileEntity(
                    id = 1,
                    totalXp = 300,
                    currentStreak = 3,
                    longestStreak = 3,
                    lastActiveDate = twoDaysAgoStr,
                    earnedBadgeIdsJson = "[]",
                ),
            )

            val result4 = recordLoginUseCase()
            assertEquals(1, result4.streakInfo.currentStreak)
        }
}
