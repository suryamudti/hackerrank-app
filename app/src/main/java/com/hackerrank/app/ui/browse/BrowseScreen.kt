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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hackerrank.app.R
import com.hackerrank.app.core.LocaleManager
import com.hackerrank.app.core.ThemeManager
import com.hackerrank.app.core.ThemeMode
import com.hackerrank.app.core.localizedName
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.ui.components.EmptyState
import com.hackerrank.app.ui.components.MasteryRing
import com.hackerrank.app.ui.components.StructureCardBackground

@Composable
fun BrowseScreen(
    themeManager: ThemeManager? = null,
    localeManager: LocaleManager? = null,
    onStructureClick: (String) -> Unit,
    onError: (String) -> Unit = {},
    viewModel: BrowseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val themeMode by themeManager?.themeMode?.collectAsState() ?: remember { mutableStateOf(ThemeMode.SYSTEM) }
    var showLanguageDialog by remember { mutableStateOf(false) }

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
            title = stringResource(R.string.browse_no_structures_title),
            message = stringResource(R.string.browse_no_structures_message)
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                localeManager?.let {
                    IconButton(onClick = { showLanguageDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = stringResource(R.string.browse_language),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                themeManager?.let {
                    IconButton(onClick = { it.toggle() }) {
                        Icon(
                            imageVector = when (themeMode) {
                                ThemeMode.DARK -> Icons.Default.LightMode
                                else -> Icons.Default.DarkMode
                            },
                            contentDescription = stringResource(R.string.browse_toggle_theme),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

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
                        onStructureClick = onStructureClick,
                        progressMap = uiState.progressMap
                    )
                }
            }
        }
    }

    if (showLanguageDialog && localeManager != null) {
        val currentLocale = localeManager.getCurrentCode()
        val options = listOf("en" to stringResource(R.string.lang_en), "in" to stringResource(R.string.lang_in))
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.browse_language_dialog_title)) },
            text = {
                Column {
                    options.forEach { (code, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    localeManager.setLocale(code)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentLocale == code,
                                onClick = {
                                    localeManager.setLocale(code)
                                    showLanguageDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(text = label, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
private fun CategorySection(
    category: DataStructureCategory,
    structures: List<DataStructure>,
    onStructureClick: (String) -> Unit,
    progressMap: Map<String, Float>
) {
    Column {
        Text(
            text = category.localizedName(),
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
                    onClick = { onStructureClick(structure.slug) },
                    progress = progressMap[structure.id] ?: 0f
                )
            }
        }
    }
}

@Composable
private fun StructureCard(
    structure: DataStructure,
    onClick: () -> Unit,
    progress: Float = 0f
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(140.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            StructureCardBackground(slug = structure.slug, name = structure.name)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = structure.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = structure.difficulty.localizedName(),
                    style = MaterialTheme.typography.labelSmall,
                    color = when (structure.difficulty) {
                        Difficulty.EASY -> Color(0xFF4CAF50)
                        Difficulty.MEDIUM -> Color(0xFFFF9800)
                        Difficulty.HARD -> Color(0xFFF44336)
                    }
                )
            }
            MasteryRing(
                progress = progress,
                size = 20.dp,
                strokeWidth = 3.dp,
                trackColor = Color.White.copy(alpha = 0.3f),
                progressColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
            )
        }
    }
}
