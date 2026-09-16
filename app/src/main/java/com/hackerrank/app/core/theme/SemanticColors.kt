package com.hackerrank.app.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.hackerrank.app.domain.model.Difficulty

/**
 * Semantic and domain-specific color tokens for HackerRank.
 * Provides consistent styling for difficulties, quiz states, code blocks, and gamification elements.
 */
@Immutable
data class SemanticColors(
    val easy: Color,
    val easyContainer: Color,
    val onEasy: Color,
    val onEasyContainer: Color,
    val medium: Color,
    val mediumContainer: Color,
    val onMedium: Color,
    val onMediumContainer: Color,
    val hard: Color,
    val hardContainer: Color,
    val onHard: Color,
    val onHardContainer: Color,
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val streakFlame: Color,
    val xpGold: Color,
    val codeBackground: Color,
    val codeOnBackground: Color,
) {
    fun difficultyColor(difficulty: Difficulty): Color =
        when (difficulty) {
            Difficulty.EASY -> easy
            Difficulty.MEDIUM -> medium
            Difficulty.HARD -> hard
        }

    fun difficultyContainerColor(difficulty: Difficulty): Color =
        when (difficulty) {
            Difficulty.EASY -> easyContainer
            Difficulty.MEDIUM -> mediumContainer
            Difficulty.HARD -> hardContainer
        }

    fun difficultyOnContainerColor(difficulty: Difficulty): Color =
        when (difficulty) {
            Difficulty.EASY -> onEasyContainer
            Difficulty.MEDIUM -> onMediumContainer
            Difficulty.HARD -> onHardContainer
        }
}

val LightSemanticColors =
    SemanticColors(
        easy = Color(0xFF4CAF50),
        easyContainer = Color(0xFFE8F5E9),
        onEasy = Color.White,
        onEasyContainer = Color(0xFF1B5E20),
        medium = Color(0xFFFF9800),
        mediumContainer = Color(0xFFFFF3E0),
        onMedium = Color.White,
        onMediumContainer = Color(0xFFE65100),
        hard = Color(0xFFF44336),
        hardContainer = Color(0xFFFFEBEE),
        onHard = Color.White,
        onHardContainer = Color(0xFFB71C1C),
        success = Color(0xFF4CAF50),
        onSuccess = Color.White,
        successContainer = Color(0xFFE8F5E9),
        onSuccessContainer = Color(0xFF1B5E20),
        error = Color(0xFFE53935),
        onError = Color.White,
        errorContainer = Color(0xFFFFEBEE),
        onErrorContainer = Color(0xFFB71C1C),
        streakFlame = Color(0xFFFF5722),
        xpGold = Color(0xFFFFC107),
        codeBackground = Color(0xFF282A36),
        codeOnBackground = Color(0xFFF8F8F2),
    )

val DarkSemanticColors =
    SemanticColors(
        easy = Color(0xFF66BB6A),
        easyContainer = Color(0xFF1B3820),
        onEasy = Color.Black,
        onEasyContainer = Color(0xFFA5D6A7),
        medium = Color(0xFFFFA726),
        mediumContainer = Color(0xFF3E2706),
        onMedium = Color.Black,
        onMediumContainer = Color(0xFFFFCC80),
        hard = Color(0xFFEF5350),
        hardContainer = Color(0xFF3E1412),
        onHard = Color.Black,
        onHardContainer = Color(0xFFEF9A9A),
        success = Color(0xFF66BB6A),
        onSuccess = Color.Black,
        successContainer = Color(0xFF1B3820),
        onSuccessContainer = Color(0xFFA5D6A7),
        error = Color(0xFFEF5350),
        onError = Color.Black,
        errorContainer = Color(0xFF3E1412),
        onErrorContainer = Color(0xFFEF9A9A),
        streakFlame = Color(0xFFFF7043),
        xpGold = Color(0xFFFFD54F),
        codeBackground = Color(0xFF1E1E2E),
        codeOnBackground = Color(0xFFF8F8F2),
    )

val LocalSemanticColors = staticCompositionLocalOf { LightSemanticColors }
