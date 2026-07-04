package com.hackerrank.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlin.math.abs

private val gradientPairs = listOf(
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

private val imageKeywords = mapOf(
    "array" to "array-programming",
    "linked_list" to "linked-list-chain",
    "stack" to "stack-blocks",
    "queue" to "queue-line-people",
    "binary_tree" to "tree-nature",
    "bst" to "binary-search-tree",
    "avl_tree" to "avl-tree-balanced",
    "heap" to "heap-pile-stones",
    "trie" to "prefix-tree-dictionary",
    "graph" to "graph-network-nodes",
    "weighted_graph" to "weighted-graph-map",
    "graph_algorithms" to "graph-algorithm-path",
    "hash_table" to "hash-table-key-value",
    "hash_set" to "hash-set-unique",
    "union_find" to "union-find-connect",
    "segment_tree" to "segment-tree-array"
)

private fun pickGradient(name: String): List<Color> {
    var hash = 7
    for (c in name) {
        hash = hash * 31 + c.code
    }
    return gradientPairs[abs(hash) % gradientPairs.size]
}

private fun searchKeyword(slug: String): String {
    return imageKeywords[slug] ?: slug.replace("_", "-")
}

@Composable
fun StructureCardBackground(
    slug: String,
    name: String,
    modifier: Modifier = Modifier
) {
    val colors = remember(slug) { pickGradient(name) }
    val keyword = remember(slug) { searchKeyword(slug) }
    val url = "https://loremflickr.com/400/300/$keyword"

    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = colors.map { it.copy(alpha = 0.7f) }
                    )
                )
        )
    }
}
