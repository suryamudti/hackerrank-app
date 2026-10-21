package com.hackerrank.app.ui.cheatsheet

import com.hackerrank.app.domain.model.CheatsheetCategory
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule

sealed interface CheatsheetUiState {
    data object Loading : CheatsheetUiState

    data class Loaded(
        val allCheatsheets: List<CheatsheetItem>,
        val filteredCheatsheets: List<CheatsheetItem>,
        val bookmarkedIds: Set<String>,
        val complexityRules: List<ComplexityRule>,
        val searchQuery: String = "",
        val selectedCategory: CheatsheetCategory? = null,
        val showOnlyBookmarked: Boolean = false,
        val expandedCardIds: Set<String> = emptySet(),
        val showComplexityGuide: Boolean = true,
    ) : CheatsheetUiState
}
