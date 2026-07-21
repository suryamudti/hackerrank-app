package com.hackerrank.app.domain.model

data class QuizQuestion(
    val id: String,
    val structureId: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
)
