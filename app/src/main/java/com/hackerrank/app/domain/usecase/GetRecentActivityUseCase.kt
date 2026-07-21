package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.RecentActivity
import com.hackerrank.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentActivityUseCase
    @Inject
    constructor(
        private val progressRepository: ProgressRepository,
    ) {
        operator fun invoke(): Flow<List<RecentActivity>> {
            return progressRepository.getRecentActivities()
        }
    }
