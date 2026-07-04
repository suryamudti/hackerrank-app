package com.hackerrank.app.domain.model

enum class BadgeDefinition(
    val id: String,
    val title: String,
    val description: String
) {
    FIRST_STEPS("first_steps", "First Steps", "Complete your first quiz"),
    QUICK_LEARNER("quick_learner", "Quick Learner", "Get a perfect score on any quiz"),
    SPEED_DEMON("speed_demon", "Speed Demon", "Complete a quiz in under 60 seconds"),
    STREAK_NOVICE("streak_novice", "Streak Novice", "Reach a 3-day streak"),
    STREAK_MASTER("streak_master", "Streak Master", "Reach a 7-day streak"),
    STREAK_LEGEND("streak_legend", "Streak Legend", "Reach a 30-day streak"),
    ARRAY_ACE("array_ace", "Array Ace", "Master all array quizzes"),
    TREE_WHISPERER("tree_whisperer", "Tree Whisperer", "Master all tree quizzes"),
    GRAPH_GURU("graph_guru", "Graph Guru", "Master all graph quizzes"),
    COMPLETIONIST("completionist", "Completionist", "Master every data structure"),
    LEVEL_10("level_10", "Level 10", "Reach level 10"),
    LEVEL_25("level_25", "Level 25", "Reach level 25"),
    LEVEL_50("level_50", "Level 50", "Reach level 50");

    companion object {
        fun fromId(id: String): BadgeDefinition? = entries.find { it.id == id }
    }
}
