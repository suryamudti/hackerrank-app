package com.hackerrank.app.domain.repository

import com.hackerrank.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<UserProfile?>

    suspend fun getProfileSync(): UserProfile?

    suspend fun upsertProfile(profile: UserProfile)

    fun getMasteredCount(): Flow<Int>
}
