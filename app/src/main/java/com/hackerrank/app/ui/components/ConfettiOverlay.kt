package com.hackerrank.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

private data class ConfettiParticle(
    val startX: Float,
    val startY: Float,
    val color: Color,
    val size: Float,
    val velocityX: Float,
    val velocityY: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    val particles = remember {
        val colors = listOf(
            Color(0xFFFF6B6B), Color(0xFF4ECDC4), Color(0xFFFFE66D),
            Color(0xFF95E1D3), Color(0xFFF38181), Color(0xFFAA96DA),
            Color(0xFFFCBF49), Color(0xFFEA5455)
        )
        List(40) {
            ConfettiParticle(
                startX = Random.nextFloat(),
                startY = -0.1f - Random.nextFloat() * 0.5f,
                color = colors[Random.nextInt(colors.size)],
                size = 6f + Random.nextFloat() * 8f,
                velocityX = (Random.nextFloat() - 0.5f) * 0.6f,
                velocityY = 0.3f + Random.nextFloat() * 0.5f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 10f
            )
        }
    }

    val progress = remember { Animatable(0f, Float.VectorConverter) }

    LaunchedEffect(visible) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { p ->
            val x = (p.startX + p.velocityX * progress.value) * size.width
            val y = (p.startY + p.velocityY * progress.value) * size.height
            val rotation = p.rotationSpeed * progress.value * 60f

            drawCircle(
                color = p.color.copy(alpha = 1f - progress.value * 0.5f),
                radius = p.size * (1f - progress.value * 0.3f),
                center = Offset(x, y)
            )
        }
    }
}
