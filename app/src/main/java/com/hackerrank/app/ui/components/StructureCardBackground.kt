package com.hackerrank.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.TableRows
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.abs

private val gradientPairs =
    listOf(
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
        listOf(Color(0xFFe0c3fc), Color(0xFF8ec5fc)),
    )

private val iconMap =
    mapOf(
        "array" to Icons.Default.TableRows,
        "linked-list" to Icons.Default.Link,
        "stack" to Icons.Default.Layers,
        "queue" to Icons.AutoMirrored.Filled.List,
        "binary-tree" to Icons.Default.AccountTree,
        "binary-search-tree" to Icons.Default.AccountTree,
        "avl-tree" to Icons.Default.AccountTree,
        "heap" to Icons.Default.BarChart,
        "trie" to Icons.Default.TextFields,
        "graph" to Icons.Default.Hub,
        "weighted-graph" to Icons.Default.Hub,
        "graph-algorithms" to Icons.Default.Share,
        "hash-table" to Icons.Default.TableChart,
        "hash-set" to Icons.Default.FilterNone,
        "disjoint-set" to Icons.Default.AccountTree,
        "segment-tree" to Icons.AutoMirrored.Filled.ShowChart,
    )

private fun pickGradient(name: String): List<Color> {
    var hash = 7
    for (c in name) {
        hash = hash * 31 + c.code
    }
    val index = Math.floorMod(hash, gradientPairs.size)
    return gradientPairs[index]
}


@Composable
fun StructureCardBackground(
    slug: String,
    name: String,
    modifier: Modifier = Modifier,
) {
    val colors = remember(slug) { pickGradient(name) }
    val icon = remember(slug) { iconMap[slug] ?: Icons.Default.TableRows }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        brush =
                            Brush.linearGradient(
                                colors = colors,
                            ),
                    ),
        )
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = Color.White.copy(alpha = 0.85f),
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .size(72.dp),
        )
    }
}
