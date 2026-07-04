package com.hackerrank.app.ui.browse

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.ui.components.EmptyState
import com.hackerrank.app.ui.components.MasteryRing
import com.hackerrank.app.ui.components.StructureCardBackground

@Composable
fun BrowseScreen(
    onStructureClick: (String) -> Unit,
    onError: (String) -> Unit = {},
    viewModel: BrowseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.groupedStructures.values.all { it.isEmpty() }) {
        EmptyState(
            icon = Icons.Default.Computer,
            title = "No Structures Yet",
            message = "Data structures will appear here once they are loaded."
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        uiState.groupedStructures.forEach { (category, structures) ->
            item(key = "header_${category.name}") {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(400)) +
                            slideInHorizontally(animationSpec = tween(400))
                ) {
                    CategorySection(
                        category = category,
                        structures = structures,
                        onStructureClick = onStructureClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySection(
    category: DataStructureCategory,
    structures: List<DataStructure>,
    onStructureClick: (String) -> Unit
) {
    Column {
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(structures, key = { it.id }) { structure ->
                StructureCard(
                    structure = structure,
                    onClick = { onStructureClick(structure.slug) }
                )
            }
        }
    }
}

@Composable
private fun StructureCard(
    structure: DataStructure,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            StructureCardBackground(slug = structure.slug, name = structure.name)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MasteryRing(
                    progress = 0.0f,
                    size = 48.dp,
                    strokeWidth = 4.dp,
                    trackColor = Color.White.copy(alpha = 0.3f),
                    progressColor = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = structure.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = structure.difficulty.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}
