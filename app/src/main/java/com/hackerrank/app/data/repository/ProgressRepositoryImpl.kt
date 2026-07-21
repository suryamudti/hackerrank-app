package com.hackerrank.app.data.repository

import android.content.Context
import com.google.gson.Gson
import com.hackerrank.app.data.local.dao.ProfileDao
import com.hackerrank.app.data.local.dao.ProgressDao
import com.hackerrank.app.data.local.dao.QuizResultDao
import com.hackerrank.app.data.remote.DailyChallengeResponse
import com.hackerrank.app.domain.model.RecentActivity
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.DailyChallengeRepository
import com.hackerrank.app.domain.repository.ProfileRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl
    @Inject
    constructor(
        private val progressDao: ProgressDao,
        private val profileDao: ProfileDao,
        private val quizResultDao: QuizResultDao,
        private val gson: Gson,
        @ApplicationContext private val context: Context,
    ) : ProgressRepository, ProfileRepository, DailyChallengeRepository {
        private val prefs = context.getSharedPreferences("daily_challenge_prefs", Context.MODE_PRIVATE)
        private val _dailyChallengeState = MutableStateFlow(loadDailyChallenge())

        override fun getProgressByStructureId(structureId: String): Flow<UserProgress?> {
            return progressDao.getProgressByStructureId(structureId).map { entity ->
                entity?.toDomain()
            }
        }

        override fun getAllProgress(): Flow<List<UserProgress>> {
            return progressDao.getAllProgress().map { entities ->
                entities.map { it.toDomain() }
            }
        }

        override suspend fun getAllProgressSync(): List<UserProgress> {
            return progressDao.getAllProgressSync().map { it.toDomain() }
        }

        override suspend fun upsertProgress(progress: UserProgress) {
            progressDao.upsert(
                com.hackerrank.app.data.local.entity.UserProgressEntity(
                    structureId = progress.structureId,
                    quizzesCompleted = progress.quizzesCompleted,
                    totalCorrect = progress.totalCorrect,
                    totalQuestions = progress.totalQuestions,
                    bestScore = progress.bestScore,
                    masteryLevel = progress.masteryLevel,
                ),
            )
        }

        override fun getProfile(): Flow<UserProfile?> {
            return profileDao.getProfile().map { entity ->
                entity?.toDomain(gson)
            }
        }

        override suspend fun getProfileSync(): UserProfile? {
            return profileDao.getProfileSync()?.toDomain(gson)
        }

        override suspend fun upsertProfile(profile: UserProfile) {
            profileDao.upsert(
                com.hackerrank.app.data.local.entity.UserProfileEntity(
                    id = 1,
                    totalXp = profile.totalXp,
                    currentStreak = profile.currentStreak,
                    longestStreak = profile.longestStreak,
                    lastActiveDate = profile.lastActiveDate,
                    earnedBadgeIdsJson = gson.toJson(profile.earnedBadgeIds),
                ),
            )
        }

        override fun getMasteredCount(): Flow<Int> {
            return progressDao.getMasteredCount()
        }

        override fun getDailyChallengeState(): Flow<DailyChallengeResponse?> = _dailyChallengeState

        override suspend fun setDailyChallengeCompleted(date: String) {
            prefs.edit().putString("completed_date", date).apply()
        }

        override suspend fun isDailyChallengeCompleted(date: String): Boolean {
            return prefs.getString("completed_date", null) == date
        }

        private fun updateDailyChallengeState(response: DailyChallengeResponse?) {
            _dailyChallengeState.value = response
        }

        private fun loadDailyChallenge(): DailyChallengeResponse? {
            val json = prefs.getString("cached_response", null) ?: return null
            return try {
                gson.fromJson(json, DailyChallengeResponse::class.java)
            } catch (_: Exception) {
                null
            }
        }

        override suspend fun cacheDailyChallengeResponse(response: DailyChallengeResponse) {
            prefs.edit().putString("cached_response", gson.toJson(response)).apply()
            _dailyChallengeState.value = response
        }

        override fun getRecentActivities(): Flow<List<RecentActivity>> {
            return quizResultDao.getRecentActivities().map { list ->
                list.map { db ->
                    RecentActivity(
                        id = db.id,
                        structureId = db.structureId,
                        structureName = db.structureName ?: "Unknown",
                        score = db.score,
                        totalQuestions = db.totalQuestions,
                        xpEarned = db.xpEarned,
                        completedAt = db.completedAt,
                    )
                }
            }
        }

        override suspend fun insertQuizResult(
            structureId: String,
            score: Int,
            totalQuestions: Int,
            xpEarned: Int,
        ) {
            val result =
                com.hackerrank.app.data.local.entity.QuizResultEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    structureId = structureId,
                    score = score,
                    totalQuestions = totalQuestions,
                    xpEarned = xpEarned,
                    completedAt = System.currentTimeMillis(),
                )
            quizResultDao.insert(result)
        }
    }
