package com.hackerrank.app.ui.cheatsheet

import com.hackerrank.app.domain.model.CategoryGuide
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
import com.hackerrank.app.domain.model.ProblemCategory

sealed interface CheatsheetUiState {
    data object Loading : CheatsheetUiState

    data class Loaded(
        val allCheatsheets: List<CheatsheetItem>,
        val filteredCheatsheets: List<CheatsheetItem>,
        val bookmarkedIds: Set<String>,
        val complexityRules: List<ComplexityRule>,
        val categoryGuides: Map<ProblemCategory, CategoryGuide> = emptyMap(),
        val searchQuery: String = "",
        val selectedCategory: ProblemCategory? = null,
        val showOnlyBookmarked: Boolean = false,
        val expandedCardIds: Set<String> = emptySet(),
        val showComplexityGuide: Boolean = true,
        val showCategoryGuide: Boolean = true,
    ) : CheatsheetUiState {
        val activeCategoryGuide: CategoryGuide?
            get() = selectedCategory?.let { categoryGuides[it] }
    }
}
