package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.repository.ProfileRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class BadgeWithProgress(
    val badge: Badge,
    val isEarned: Boolean,
    val currentProgress: Int?,
    val targetProgress: Int?,
)

class ObserveBadgesUseCase
    @Inject
    constructor(
        private val profileRepository: ProfileRepository,
        private val progressRepository: ProgressRepository,
    ) {
        operator fun invoke(): Flow<List<BadgeWithProgress>> {
            return combine(
                profileRepository.getProfile(),
                progressRepository.getAllProgress(),
            ) { profile, progressList ->
                val earnedBadgeIds = profile?.earnedBadgeIds ?: emptyList()
                BadgeDefinition.entries.map { def ->
                    val isEarned = earnedBadgeIds.contains(def.id)
                    val (current, target) =
                        when (def) {
                            BadgeDefinition.FIRST_STEPS -> (if (isEarned) 1 else 0) to 1
                            BadgeDefinition.QUICK_LEARNER -> (if (isEarned) 1 else 0) to 1
                            BadgeDefinition.SPEED_DEMON -> (if (isEarned) 1 else 0) to 1
                            BadgeDefinition.STREAK_NOVICE -> (profile?.currentStreak ?: 0) to 3
                            BadgeDefinition.STREAK_MASTER -> (profile?.currentStreak ?: 0) to 7
                            BadgeDefinition.STREAK_LEGEND -> (profile?.currentStreak ?: 0) to 30
                            BadgeDefinition.LEVEL_10 -> (profile?.totalXp ?: 0) to 10000
                            BadgeDefinition.LEVEL_25 -> (profile?.totalXp ?: 0) to 62500
                            BadgeDefinition.LEVEL_50 -> (profile?.totalXp ?: 0) to 250000
                            BadgeDefinition.ARRAY_ACE -> {
                                val ids = listOf("array", "linked_list", "stack", "queue")
                                progressList.count { it.structureId in ids && it.masteryLevel >= 80 } to 4
                            }
                            BadgeDefinition.TREE_WHISPERER -> {
                                val ids = listOf("binary_tree", "bst", "avl_tree", "heap", "trie")
                                progressList.count { it.structureId in ids && it.masteryLevel >= 80 } to 5
                            }
                            BadgeDefinition.GRAPH_GURU -> {
                                val ids = listOf("graph", "weighted_graph", "graph_algorithms")
                                progressList.count { it.structureId in ids && it.masteryLevel >= 80 } to 3
                            }
                            BadgeDefinition.COMPLETIONIST -> {
                                val ids =
                                    listOf(
                                        "array", "linked_list", "stack", "queue",
                                        "binary_tree", "bst", "avl_tree", "heap", "trie",
                                        "graph", "weighted_graph", "graph_algorithms",
                                        "hash_table", "hash_set", "disjoint_set", "segment_tree",
                                    )
                                progressList.count { it.structureId in ids && it.masteryLevel >= 80 } to 16
                            }
                        }
                    BadgeWithProgress(
                        badge = Badge(def.id, def.title, def.description),
                        isEarned = isEarned,
                        currentProgress = current,
                        targetProgress = target,
                    )
                }
            }
        }
    }
