package com.hackerrank.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.math.abs

private val palette = listOf(
    listOf(Color(0xFF667eea), Color(0xFF764ba2)),
    listOf(Color(0xFFf093fb), Color(0xFFf5576c)),
    listOf(Color(0xFF4facfe), Color(0xFF00f2fe)),
    listOf(Color(0xFF43e97b), Color(0xFF38f9d7)),
    listOf(Color(0xFFfa709a), Color(0xFFfee140)),
    listOf(Color(0xFFa18cd1), Color(0xFFfbc2eb)),
    listOf(Color(0xFFfccb90), Color(0xFFd57eeb)),
    listOf(Color(0xFFe0c3fc), Color(0xFF8ec5fc)),
    listOf(Color(0xFFf5576c), Color(0xFFf093fb)),
    listOf(Color(0xFF4facfe), Color(0xFF00f2fe)),
    listOf(Color(0xFF43e97b), Color(0xFF38f9d7)),
    listOf(Color(0xFFfa709a), Color(0xFFfee140)),
    listOf(Color(0xFFa18cd1), Color(0xFFfbc2eb)),
    listOf(Color(0xFF667eea), Color(0xFF764ba2)),
    listOf(Color(0xFFfccb90), Color(0xFFd57eeb)),
    listOf(Color(0xFFe0c3fc), Color(0xFF8ec5fc))
)

private fun hashSeed(name: String): Int {
    var hash = 7
    for (c in name) {
        hash = hash * 31 + c.code
    }
    return abs(hash)
}

@Composable
fun StructureCardBackground(
    name: String,
    modifier: Modifier = Modifier
) {
    val index = remember(name) { hashSeed(name) % palette.size }
    val colors = palette[index]

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(0f, 0f),
                end = Offset(w, h)
            )
        )

        val alpha = 0.08f
        drawCircle(
            color = Color.White.copy(alpha = alpha),
            radius = w * 0.6f,
            center = Offset(w * 0.8f, h * 0.2f)
        )
        drawCircle(
            color = Color.White.copy(alpha = alpha),
            radius = w * 0.4f,
            center = Offset(w * 0.2f, h * 0.8f)
        )
    }
}
