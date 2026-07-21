package com.hackerrank.app.domain.model

data class QuizSession(
    val questions: List<QuizQuestion>,
    val currentIndex: Int = 0,
    val answers: Map<String, Int> = emptyMap(),
    val startTimeNanos: Long = System.nanoTime(),
    val endTimeNanos: Long? = null,
    val isCompleted: Boolean = false,
    val questionTimesMs: Map<String, Long> = emptyMap(),
) {
    val score: Int get() =
        answers.count { (id, selected) ->
            questions.find { it.id == id }?.correctIndex == selected
        }

    val totalQuestions: Int get() = questions.size

    val correctAnswers: List<QuizQuestion>
        get() =
            questions.filter { q ->
                answers[q.id] == q.correctIndex
            }

    val incorrectAnswers: List<Pair<QuizQuestion, Int>>
        get() =
            questions
                .filter { q -> answers.containsKey(q.id) && answers[q.id] != q.correctIndex }
                .map { q -> q to (answers[q.id] ?: -1) }

    val elapsedTimeMs: Long
        get() {
            val end = endTimeNanos ?: System.nanoTime()
            return (end - startTimeNanos) / 1_000_000
        }
}
