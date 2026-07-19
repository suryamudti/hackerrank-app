package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class ProgressOverview(
    val profile: UserProfile?,
    val allProgress: List<UserProgress>,
    val categoryMastery: Map<DataStructureCategory, Float>,
    val totalStructures: Int,
    val masteredStructures: Int
)

class ObserveProgressOverviewUseCase @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val contentRepository: ContentRepository
) {
    operator fun invoke(): Flow<ProgressOverview> = flow {
        val structures = contentRepository.getAllStructures().first()
        val totalStructures = structures.size
        val categoryStructIds = structures
            .groupBy { it.category }
            .mapValues { (_, structs) -> structs.map { it.id }.toSet() }

        val upstream = combine(
            progressRepository.getProfile(),
            progressRepository.getAllProgress()
        ) { profile, allProgress ->
            val categoryMastery = categoryStructIds.mapValues { (_, structIds) ->
                val relevant = allProgress.filter { it.structureId in structIds }
                if (structIds.isEmpty()) 0f
                else {
                    val correct = relevant.sumOf { it.totalCorrect }
                    val total = relevant.sumOf { it.totalQuestions }
                    if (total > 0) correct.toFloat() / total else 0f
                }
            }
            val masteredCount = allProgress.count { it.masteryLevel >= 80 }

            ProgressOverview(
                profile = profile,
                allProgress = allProgress,
                categoryMastery = categoryMastery,
                totalStructures = totalStructures,
                masteredStructures = masteredCount
            )
        }
        emitAll(upstream)
    }
}
