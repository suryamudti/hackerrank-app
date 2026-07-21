package com.hackerrank.app.domain.model

data class RecentActivity(
    val id: String,
    val structureId: String,
    val structureName: String,
    val score: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val completedAt: Long,
)
