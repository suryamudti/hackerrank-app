package com.hackerrank.app.domain.gamification

import com.hackerrank.app.core.Constants
import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.GamificationResult
import com.hackerrank.app.domain.model.StreakInfo
import com.hackerrank.app.domain.model.UserProfile
import com.hackerrank.app.domain.repository.ProfileRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamificationEngine
    @Inject
    constructor(
        private val profileRepository: ProfileRepository,
        private val progressRepository: ProgressRepository,
    ) {
        private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        suspend fun recordQuizComplete(
            score: Int,
            totalQuestions: Int,
            elapsedTimeMs: Long,
            structureId: String,
        ): GamificationResult {
            val profile = profileRepository.getProfileSync() ?: UserProfile()
            val isPerfect = score == totalQuestions

            // Calculate XP
            var xpGained = Constants.BASE_QUIZ_XP
            if (isPerfect) xpGained += Constants.PERFECT_SCORE_BONUS
            if (elapsedTimeMs < Constants.SPEED_BONUS_THRESHOLD_MS) {
                xpGained += Constants.SPEED_BONUS_MAX
            }
            xpGained += Constants.DAILY_QUIZ_XP

            // Update streak
            val streakResult = updateStreak(profile)
            if (streakResult.streakMilestoneReached != null) {
                xpGained += Constants.streakMilestones[streakResult.streakMilestoneReached] ?: 0
            }

            val oldLevel = Constants.getLevel(profile.totalXp)
            val newTotalXp = profile.totalXp + xpGained
            val newLevel = Constants.getLevel(newTotalXp)

            // Evaluate badges
            val allProgress = progressRepository.getAllProgressSync()
            val currentBadges = profile.earnedBadgeIds.toMutableSet()
            val newBadges =
                evaluateBadges(
                    profile = profile.copy(totalXp = newTotalXp, currentStreak = streakResult.currentStreak),
                    isPerfect = isPerfect,
                    elapsedTimeMs = elapsedTimeMs,
                    structureId = structureId,
                    allProgress = allProgress,
                    earnedBadgeIds = currentBadges,
                )
            currentBadges.addAll(newBadges.map { it.id })

            // Persist
            val updatedProfile =
                profile.copy(
                    totalXp = newTotalXp,
                    currentStreak = streakResult.currentStreak,
                    longestStreak = streakResult.longestStreak,
                    lastActiveDate = streakResult.lastActiveDate,
                    earnedBadgeIds = currentBadges.toList(),
                )
            profileRepository.upsertProfile(updatedProfile)

            return GamificationResult(
                xpAwarded = xpGained,
                newTotalXp = newTotalXp,
                newLevel = newLevel,
                previousLevel = oldLevel,
                newBadges = newBadges,
                streakInfo = streakResult,
            )
        }

        suspend fun recordProblemSolved(difficulty: Difficulty): GamificationResult {
            val xpGained =
                when (difficulty) {
                    Difficulty.EASY -> Constants.PROBLEM_EASY_XP
                    Difficulty.MEDIUM -> Constants.PROBLEM_MEDIUM_XP
                    Difficulty.HARD -> Constants.PROBLEM_HARD_XP
                }
            val profile = profileRepository.getProfileSync() ?: UserProfile()
            val streakResult = updateStreak(profile)
            val oldLevel = Constants.getLevel(profile.totalXp)
            val newTotalXp = profile.totalXp + xpGained
            val newLevel = Constants.getLevel(newTotalXp)

            val currentBadges = profile.earnedBadgeIds.toMutableSet()
            val newBadges =
                evaluateBadges(
                    profile = profile.copy(totalXp = newTotalXp, currentStreak = streakResult.currentStreak),
                    isPerfect = false,
                    elapsedTimeMs = 0L,
                    structureId = "",
                    allProgress = emptyList(),
                    earnedBadgeIds = currentBadges,
                )
            currentBadges.addAll(newBadges.map { it.id })

            val updatedProfile =
                profile.copy(
                    totalXp = newTotalXp,
                    currentStreak = streakResult.currentStreak,
                    longestStreak = streakResult.longestStreak,
                    lastActiveDate = streakResult.lastActiveDate,
                    earnedBadgeIds = currentBadges.toList(),
                )
            profileRepository.upsertProfile(updatedProfile)

            return GamificationResult(
                xpAwarded = xpGained,
                newTotalXp = newTotalXp,
                newLevel = newLevel,
                previousLevel = oldLevel,
                newBadges = newBadges,
                streakInfo = streakResult,
            )
        }

        suspend fun recordDailyChallengeCompleted(
            difficulty: Difficulty,
            bonusXp: Int,
        ): GamificationResult {
            val problemXp =
                when (difficulty) {
                    Difficulty.EASY -> Constants.PROBLEM_EASY_XP
                    Difficulty.MEDIUM -> Constants.PROBLEM_MEDIUM_XP
                    Difficulty.HARD -> Constants.PROBLEM_HARD_XP
                }
            val totalXpGained = problemXp + bonusXp
            val profile = profileRepository.getProfileSync() ?: UserProfile()
            val streakResult = updateStreak(profile)
            val oldLevel = Constants.getLevel(profile.totalXp)
            val newTotalXp = profile.totalXp + totalXpGained
            val newLevel = Constants.getLevel(newTotalXp)

            val currentBadges = profile.earnedBadgeIds.toMutableSet()
            val newBadges =
                evaluateBadges(
                    profile = profile.copy(totalXp = newTotalXp, currentStreak = streakResult.currentStreak),
                    isPerfect = false,
                    elapsedTimeMs = 0L,
                    structureId = "",
                    allProgress = emptyList(),
                    earnedBadgeIds = currentBadges,
                )
            currentBadges.addAll(newBadges.map { it.id })

            val updatedProfile =
                profile.copy(
                    totalXp = newTotalXp,
                    currentStreak = streakResult.currentStreak,
                    longestStreak = streakResult.longestStreak,
                    lastActiveDate = streakResult.lastActiveDate,
                    earnedBadgeIds = currentBadges.toList(),
                )
            profileRepository.upsertProfile(updatedProfile)

            return GamificationResult(
                xpAwarded = totalXpGained,
                newTotalXp = newTotalXp,
                newLevel = newLevel,
                previousLevel = oldLevel,
                newBadges = newBadges,
                streakInfo = streakResult,
            )
        }

        suspend fun recordLogin(): GamificationResult {
            val profile = profileRepository.getProfileSync() ?: UserProfile()
            val todayStr = LocalDate.now().format(dateFormatter)

            if (profile.lastActiveDate == todayStr) {
                val streakResult = updateStreak(profile)
                return GamificationResult(
                    xpAwarded = 0,
                    newTotalXp = profile.totalXp,
                    newLevel = Constants.getLevel(profile.totalXp),
                    previousLevel = Constants.getLevel(profile.totalXp),
                    newBadges = emptyList(),
                    streakInfo = streakResult,
                )
            }

            val streakResult = updateStreak(profile)
            val oldLevel = Constants.getLevel(profile.totalXp)
            val newTotalXp = profile.totalXp + Constants.DAILY_LOGIN_XP
            val newLevel = Constants.getLevel(newTotalXp)

            val updatedProfile =
                profile.copy(
                    totalXp = newTotalXp,
                    currentStreak = streakResult.currentStreak,
                    longestStreak = streakResult.longestStreak,
                    lastActiveDate = streakResult.lastActiveDate,
                )
            profileRepository.upsertProfile(updatedProfile)

            return GamificationResult(
                xpAwarded = Constants.DAILY_LOGIN_XP,
                newTotalXp = newTotalXp,
                newLevel = newLevel,
                previousLevel = oldLevel,
                newBadges = emptyList(),
                streakInfo = streakResult,
            )
        }

        private fun updateStreak(profile: UserProfile): StreakInfo {
            val today = LocalDate.now()
            val todayStr = today.format(dateFormatter)

            if (profile.lastActiveDate == todayStr) {
                return StreakInfo(
                    currentStreak = profile.currentStreak,
                    longestStreak = profile.longestStreak,
                    isNewStreakDay = false,
                    streakMilestoneReached = null,
                    lastActiveDate = todayStr,
                )
            }

            val yesterday = today.minusDays(1)
            val yesterdayStr = yesterday.format(dateFormatter)

            val newStreak =
                if (profile.lastActiveDate == yesterdayStr) {
                    profile.currentStreak + 1
                } else {
                    1
                }

            val newLongestStreak = maxOf(profile.longestStreak, newStreak)

            // Check streak milestones
            val milestoneReached = Constants.streakMilestones.keys.find { it == newStreak }

            return StreakInfo(
                currentStreak = newStreak,
                longestStreak = newLongestStreak,
                isNewStreakDay = true,
                streakMilestoneReached = milestoneReached,
                lastActiveDate = todayStr,
            )
        }

        private suspend fun evaluateBadges(
            profile: UserProfile,
            isPerfect: Boolean,
            elapsedTimeMs: Long,
            structureId: String,
            allProgress: List<com.hackerrank.app.domain.model.UserProgress>,
            earnedBadgeIds: Set<String>,
        ): List<Badge> {
            val newBadges = mutableListOf<Badge>()

            if (BadgeDefinition.FIRST_STEPS.id !in earnedBadgeIds &&
                allProgress.any { it.quizzesCompleted > 0 }
            ) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.FIRST_STEPS.id,
                        BadgeDefinition.FIRST_STEPS.title,
                        BadgeDefinition.FIRST_STEPS.description,
                    ),
                )
            }

            if (BadgeDefinition.QUICK_LEARNER.id !in earnedBadgeIds && isPerfect) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.QUICK_LEARNER.id,
                        BadgeDefinition.QUICK_LEARNER.title,
                        BadgeDefinition.QUICK_LEARNER.description,
                    ),
                )
            }

            if (BadgeDefinition.SPEED_DEMON.id !in earnedBadgeIds &&
                elapsedTimeMs < Constants.SPEED_BONUS_THRESHOLD_MS
            ) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.SPEED_DEMON.id,
                        BadgeDefinition.SPEED_DEMON.title,
                        BadgeDefinition.SPEED_DEMON.description,
                    ),
                )
            }

            if (BadgeDefinition.STREAK_NOVICE.id !in earnedBadgeIds && profile.currentStreak >= 3) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.STREAK_NOVICE.id,
                        BadgeDefinition.STREAK_NOVICE.title,
                        BadgeDefinition.STREAK_NOVICE.description,
                    ),
                )
            }

            if (BadgeDefinition.STREAK_MASTER.id !in earnedBadgeIds && profile.currentStreak >= 7) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.STREAK_MASTER.id,
                        BadgeDefinition.STREAK_MASTER.title,
                        BadgeDefinition.STREAK_MASTER.description,
                    ),
                )
            }

            if (BadgeDefinition.STREAK_LEGEND.id !in earnedBadgeIds && profile.currentStreak >= 30) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.STREAK_LEGEND.id,
                        BadgeDefinition.STREAK_LEGEND.title,
                        BadgeDefinition.STREAK_LEGEND.description,
                    ),
                )
            }

            if (BadgeDefinition.LEVEL_10.id !in earnedBadgeIds && profile.totalXp >= 10_000) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.LEVEL_10.id,
                        BadgeDefinition.LEVEL_10.title,
                        BadgeDefinition.LEVEL_10.description,
                    ),
                )
            }

            if (BadgeDefinition.LEVEL_25.id !in earnedBadgeIds && profile.totalXp >= 62_500) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.LEVEL_25.id,
                        BadgeDefinition.LEVEL_25.title,
                        BadgeDefinition.LEVEL_25.description,
                    ),
                )
            }

            if (BadgeDefinition.LEVEL_50.id !in earnedBadgeIds && profile.totalXp >= 250_000) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.LEVEL_50.id,
                        BadgeDefinition.LEVEL_50.title,
                        BadgeDefinition.LEVEL_50.description,
                    ),
                )
            }

            // ARRAY_ACE: Master all array (Linear) quizzes
            val linearIds = listOf("array", "linked_list", "stack", "queue")
            val linearProgress = allProgress.filter { it.structureId in linearIds }
            if (BadgeDefinition.ARRAY_ACE.id !in earnedBadgeIds &&
                linearProgress.size == linearIds.size &&
                linearProgress.all { it.masteryLevel >= 80 }
            ) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.ARRAY_ACE.id,
                        BadgeDefinition.ARRAY_ACE.title,
                        BadgeDefinition.ARRAY_ACE.description,
                    ),
                )
            }

            // TREE_WHISPERER: Master all tree quizzes
            val treeIds = listOf("binary_tree", "bst", "avl_tree", "heap", "trie")
            val treeProgress = allProgress.filter { it.structureId in treeIds }
            if (BadgeDefinition.TREE_WHISPERER.id !in earnedBadgeIds &&
                treeProgress.size == treeIds.size &&
                treeProgress.all { it.masteryLevel >= 80 }
            ) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.TREE_WHISPERER.id,
                        BadgeDefinition.TREE_WHISPERER.title,
                        BadgeDefinition.TREE_WHISPERER.description,
                    ),
                )
            }

            // GRAPH_GURU: Master all graph quizzes
            val graphIds = listOf("graph", "weighted_graph", "graph_algorithms")
            val graphProgress = allProgress.filter { it.structureId in graphIds }
            if (BadgeDefinition.GRAPH_GURU.id !in earnedBadgeIds &&
                graphProgress.size == graphIds.size &&
                graphProgress.all { it.masteryLevel >= 80 }
            ) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.GRAPH_GURU.id,
                        BadgeDefinition.GRAPH_GURU.title,
                        BadgeDefinition.GRAPH_GURU.description,
                    ),
                )
            }

            // COMPLETIONIST: Master every data structure
            val allIds =
                listOf(
                    "array", "linked_list", "stack", "queue",
                    "binary_tree", "bst", "avl_tree", "heap", "trie",
                    "graph", "weighted_graph", "graph_algorithms",
                    "hash_table", "hash_set",
                    "disjoint_set", "segment_tree",
                )
            val completedProgress = allProgress.filter { it.structureId in allIds }
            if (BadgeDefinition.COMPLETIONIST.id !in earnedBadgeIds &&
                completedProgress.size == allIds.size &&
                completedProgress.all { it.masteryLevel >= 80 }
            ) {
                newBadges.add(
                    Badge(
                        BadgeDefinition.COMPLETIONIST.id,
                        BadgeDefinition.COMPLETIONIST.title,
                        BadgeDefinition.COMPLETIONIST.description,
                    ),
                )
            }

            return newBadges
        }
    }
