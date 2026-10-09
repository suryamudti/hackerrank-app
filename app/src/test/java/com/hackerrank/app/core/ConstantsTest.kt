package com.hackerrank.app.core

import org.junit.Assert.assertEquals
import org.junit.Test

class ConstantsTest {
    @Test
    fun getLevel_calculatesCorrectLevelForTotalXp() {
        assertEquals(0, Constants.getLevel(0))
        assertEquals(0, Constants.getLevel(99))
        assertEquals(1, Constants.getLevel(100))
        assertEquals(1, Constants.getLevel(399))
        assertEquals(2, Constants.getLevel(400))
        assertEquals(3, Constants.getLevel(900))
        assertEquals(10, Constants.getLevel(10000))
    }

    @Test
    fun getXpProgress_returnsCurrentLevelProgressAndNextLevelTarget() {
        // At 0 XP: level 0 (0 to 100) -> progress: 0, target: 100
        val (current0, target0) = Constants.getXpProgress(0)
        assertEquals(0, current0)
        assertEquals(100, target0)

        // At 150 XP: level 1 (100 to 400) -> progress: 50, target: 300
        val (current150, target150) = Constants.getXpProgress(150)
        assertEquals(50, current150)
        assertEquals(300, target150)

        // At 400 XP: level 2 (400 to 900) -> progress: 0, target: 500
        val (current400, target400) = Constants.getXpProgress(400)
        assertEquals(0, current400)
        assertEquals(500, target400)
    }

    @Test
    fun constants_haveExpectedValues() {
        assertEquals(50, Constants.BASE_QUIZ_XP)
        assertEquals(50, Constants.PERFECT_SCORE_BONUS)
        assertEquals(10, Constants.DAILY_LOGIN_XP)
        assertEquals(20, Constants.DAILY_QUIZ_XP)
        assertEquals(30, Constants.DAILY_CHALLENGE_BONUS_XP)
        assertEquals(8, Constants.QUESTIONS_PER_QUIZ)
    }
}
