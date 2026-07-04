package com.hackerrank.app.data.repository

import com.hackerrank.app.data.local.dao.ProfileDao
import com.hackerrank.app.data.local.dao.ProgressDao
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ProgressRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val progressDao: ProgressDao,
    private val profileDao: ProfileDao
) : ProgressRepository {

    private val gson = Gson()

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

    override suspend fun upsertProgress(progress: UserProgress) {
        progressDao.upsert(
            com.hackerrank.app.data.local.entity.UserProgressEntity(
                structureId = progress.structureId,
                quizzesCompleted = progress.quizzesCompleted,
                totalCorrect = progress.totalCorrect,
                totalQuestions = progress.totalQuestions,
                bestScore = progress.bestScore,
                masteryLevel = progress.masteryLevel
            )
        )
    }

    override fun getProfile(): Flow<UserProfile?> {
        return profileDao.getProfile().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getProfileSync(): UserProfile? {
        return profileDao.getProfileSync()?.toDomain()
    }

    override suspend fun upsertProfile(profile: UserProfile) {
        profileDao.upsert(
            com.hackerrank.app.data.local.entity.UserProfileEntity(
                id = 1,
                totalXp = profile.totalXp,
                currentStreak = profile.currentStreak,
                longestStreak = profile.longestStreak,
                lastActiveDate = profile.lastActiveDate,
                earnedBadgeIdsJson = gson.toJson(profile.earnedBadgeIds)
            )
        )
    }

    override fun getMasteredCount(): Flow<Int> {
        return progressDao.getMasteredCount()
    }

    private fun com.hackerrank.app.data.local.entity.UserProgressEntity.toDomain(): UserProgress {
        return UserProgress(
            structureId = structureId,
            quizzesCompleted = quizzesCompleted,
            totalCorrect = totalCorrect,
            totalQuestions = totalQuestions,
            bestScore = bestScore,
            masteryLevel = masteryLevel
        )
    }

    private fun com.hackerrank.app.data.local.entity.UserProfileEntity.toDomain(): UserProfile {
        val listType = object : TypeToken<List<String>>() {}.type
        val badges: List<String> = gson.fromJson(earnedBadgeIdsJson, listType)

        return UserProfile(
            totalXp = totalXp,
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            lastActiveDate = lastActiveDate,
            earnedBadgeIds = badges
        )
    }
}
