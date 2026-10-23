package com.hackerrank.app.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hackerrank.app.R
import com.hackerrank.app.domain.model.Badge
import com.hackerrank.app.domain.model.BadgeDefinition
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.ProblemCategory

@Composable
fun DataStructureCategory.localizedName(): String =
    stringResource(
        when (this) {
            DataStructureCategory.LINEAR -> R.string.category_linear
            DataStructureCategory.TREES -> R.string.category_trees
            DataStructureCategory.GRAPHS -> R.string.category_graphs
            DataStructureCategory.HASH_BASED -> R.string.category_hash_based
            DataStructureCategory.OTHER -> R.string.category_other
        },
    )

@Composable
fun Difficulty.localizedName(): String =
    stringResource(
        when (this) {
            Difficulty.EASY -> R.string.difficulty_easy
            Difficulty.MEDIUM -> R.string.difficulty_medium
            Difficulty.HARD -> R.string.difficulty_hard
        },
    )

@Composable
fun ProblemCategory.localizedName(): String =
    stringResource(
        when (this) {
            ProblemCategory.ARRAYS -> R.string.pc_arrays
            ProblemCategory.STRINGS -> R.string.pc_strings
            ProblemCategory.LINKED_LISTS -> R.string.pc_linked_lists
            ProblemCategory.STACKS_QUEUES -> R.string.pc_stacks_queues
            ProblemCategory.TREES -> R.string.pc_trees
            ProblemCategory.GRAPHS -> R.string.pc_graphs
            ProblemCategory.DYNAMIC_PROGRAMMING -> R.string.pc_dynamic_programming
            ProblemCategory.SORTING_SEARCHING -> R.string.pc_sorting_searching
            ProblemCategory.HASH_BASED -> R.string.pc_hash_based
            ProblemCategory.RECURSION_MATH -> R.string.pc_recursion_math
            ProblemCategory.BIT_MANIPULATION -> R.string.pc_bit_manipulation
            ProblemCategory.MISCELLANEOUS -> R.string.pc_miscellaneous
        },
    )

@Composable
fun BadgeDefinition.localizedTitle(): String =
    stringResource(
        when (this) {
            BadgeDefinition.FIRST_STEPS -> R.string.badge_first_steps_title
            BadgeDefinition.QUICK_LEARNER -> R.string.badge_quick_learner_title
            BadgeDefinition.SPEED_DEMON -> R.string.badge_speed_demon_title
            BadgeDefinition.STREAK_NOVICE -> R.string.badge_streak_novice_title
            BadgeDefinition.STREAK_MASTER -> R.string.badge_streak_master_title
            BadgeDefinition.STREAK_LEGEND -> R.string.badge_streak_legend_title
            BadgeDefinition.ARRAY_ACE -> R.string.badge_array_ace_title
            BadgeDefinition.TREE_WHISPERER -> R.string.badge_tree_whisperer_title
            BadgeDefinition.GRAPH_GURU -> R.string.badge_graph_guru_title
            BadgeDefinition.COMPLETIONIST -> R.string.badge_completionist_title
            BadgeDefinition.LEVEL_10 -> R.string.badge_level_10_title
            BadgeDefinition.LEVEL_25 -> R.string.badge_level_25_title
            BadgeDefinition.LEVEL_50 -> R.string.badge_level_50_title
        },
    )

@Composable
fun BadgeDefinition.localizedDescription(): String =
    stringResource(
        when (this) {
            BadgeDefinition.FIRST_STEPS -> R.string.badge_first_steps_desc
            BadgeDefinition.QUICK_LEARNER -> R.string.badge_quick_learner_desc
            BadgeDefinition.SPEED_DEMON -> R.string.badge_speed_demon_desc
            BadgeDefinition.STREAK_NOVICE -> R.string.badge_streak_novice_desc
            BadgeDefinition.STREAK_MASTER -> R.string.badge_streak_master_desc
            BadgeDefinition.STREAK_LEGEND -> R.string.badge_streak_legend_desc
            BadgeDefinition.ARRAY_ACE -> R.string.badge_array_ace_desc
            BadgeDefinition.TREE_WHISPERER -> R.string.badge_tree_whisperer_desc
            BadgeDefinition.GRAPH_GURU -> R.string.badge_graph_guru_desc
            BadgeDefinition.COMPLETIONIST -> R.string.badge_completionist_desc
            BadgeDefinition.LEVEL_10 -> R.string.badge_level_10_desc
            BadgeDefinition.LEVEL_25 -> R.string.badge_level_25_desc
            BadgeDefinition.LEVEL_50 -> R.string.badge_level_50_desc
        },
    )

@Composable
fun Badge.localizedTitle(): String {
    val def = BadgeDefinition.fromId(id)
    return if (def != null) def.localizedTitle() else title
}

@Composable
fun Badge.localizedDescription(): String {
    val def = BadgeDefinition.fromId(id)
    return if (def != null) def.localizedDescription() else description
}
