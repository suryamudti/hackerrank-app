package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProblemRepository
import com.hackerrank.app.domain.repository.ProfileRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class ProblemStats(
    val totalProblems: Int = 0,
    val solvedCount: Int = 0,
    val easySolved: Int = 0,
    val easyTotal: Int = 0,
    val mediumSolved: Int = 0,
    val mediumTotal: Int = 0,
    val hardSolved: Int = 0,
    val hardTotal: Int = 0,
)

data class ProgressOverview(
    val profile: UserProfile?,
    val allProgress: List<UserProgress>,
    val categoryMastery: Map<DataStructureCategory, Float>,
    val totalStructures: Int,
    val masteredStructures: Int,
    val problemStats: ProblemStats = ProblemStats(),
)

class ObserveProgressOverviewUseCase
    @Inject
    constructor(
        private val profileRepository: ProfileRepository,
        private val progressRepository: ProgressRepository,
        private val contentRepository: ContentRepository,
        private val problemRepository: ProblemRepository,
    ) {
        operator fun invoke(): Flow<ProgressOverview> =
            combine(
                contentRepository.getAllStructures(),
                profileRepository.getProfile(),
                progressRepository.getAllProgress(),
                problemRepository.getAllProblems(),
                problemRepository.getSolvedIds(),
            ) { structures, profile, allProgress, allProblems, solvedIds ->
                val totalStructures = structures.size
                val categoryStructIds =
                    structures
                        .groupBy { it.category }
                        .mapValues { (_, structs) -> structs.map { it.id }.toSet() }

                val categoryMastery =
                    categoryStructIds.mapValues { (_, structIds) ->
                        val relevant = allProgress.filter { it.structureId in structIds }
                        if (structIds.isEmpty()) {
                            0f
                        } else {
                            val correct = relevant.sumOf { it.totalCorrect }
                            val total = relevant.sumOf { it.totalQuestions }
                            if (total > 0) correct.toFloat() / total else 0f
                        }
                    }
                val masteredCount = allProgress.count { it.masteryLevel >= 80 }

                val easyTotal = allProblems.count { it.difficulty == Difficulty.EASY }
                val easySolved = allProblems.count { it.difficulty == Difficulty.EASY && it.id in solvedIds }
                val mediumTotal = allProblems.count { it.difficulty == Difficulty.MEDIUM }
                val mediumSolved = allProblems.count { it.difficulty == Difficulty.MEDIUM && it.id in solvedIds }
                val hardTotal = allProblems.count { it.difficulty == Difficulty.HARD }
                val hardSolved = allProblems.count { it.difficulty == Difficulty.HARD && it.id in solvedIds }

                val problemStats =
                    ProblemStats(
                        totalProblems = allProblems.size,
                        solvedCount = solvedIds.size,
                        easySolved = easySolved,
                        easyTotal = easyTotal,
                        mediumSolved = mediumSolved,
                        mediumTotal = mediumTotal,
                        hardSolved = hardSolved,
                        hardTotal = hardTotal,
                    )

                ProgressOverview(
                    profile = profile,
                    allProgress = allProgress,
                    categoryMastery = categoryMastery,
                    totalStructures = totalStructures,
                    masteredStructures = masteredCount,
                    problemStats = problemStats,
                )
            }
    }
