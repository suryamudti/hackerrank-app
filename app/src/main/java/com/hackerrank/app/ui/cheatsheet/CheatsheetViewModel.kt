package com.hackerrank.app.ui.cheatsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerrank.app.domain.model.CheatsheetCategory
import com.hackerrank.app.domain.usecase.CheatsheetsData
import com.hackerrank.app.domain.usecase.ObserveCheatsheetsUseCase
import com.hackerrank.app.domain.usecase.ToggleCheatsheetBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheatsheetViewModel
    @Inject
    constructor(
        observeCheatsheetsUseCase: ObserveCheatsheetsUseCase,
        private val toggleBookmarkUseCase: ToggleCheatsheetBookmarkUseCase,
    ) : ViewModel() {
        private val searchQuery = MutableStateFlow("")
        private val selectedCategory = MutableStateFlow<CheatsheetCategory?>(null)
        private val showOnlyBookmarked = MutableStateFlow(false)
        private val expandedCardIds = MutableStateFlow<Set<String>>(emptySet())
        private val showComplexityGuide = MutableStateFlow(true)

        @Suppress("UNCHECKED_CAST")
        val uiState: StateFlow<CheatsheetUiState> =
            combine(
                observeCheatsheetsUseCase(),
                searchQuery,
                selectedCategory,
                showOnlyBookmarked,
                expandedCardIds,
                showComplexityGuide,
            ) { params ->
                val data = params[0] as CheatsheetsData
                val query = params[1] as String
                val category = params[2] as CheatsheetCategory?
                val onlyBookmarked = params[3] as Boolean
                val expanded = params[4] as Set<String>
                val guideVisible = params[5] as Boolean

                val filtered =
                    data.cheatsheets.filter { item ->
                        val matchesQuery =
                            query.isBlank() ||
                                item.title.contains(query, ignoreCase = true) ||
                                item.summary.contains(query, ignoreCase = true) ||
                                item.keyTricks.any { it.contains(query, ignoreCase = true) } ||
                                item.whenToUse.any { it.contains(query, ignoreCase = true) }

                        val matchesCategory = category == null || item.category == category
                        val matchesBookmark = !onlyBookmarked || data.bookmarkedIds.contains(item.id)

                        matchesQuery && matchesCategory && matchesBookmark
                    }

                CheatsheetUiState.Loaded(
                    allCheatsheets = data.cheatsheets,
                    filteredCheatsheets = filtered,
                    bookmarkedIds = data.bookmarkedIds,
                    complexityRules = data.complexityRules,
                    searchQuery = query,
                    selectedCategory = category,
                    showOnlyBookmarked = onlyBookmarked,
                    expandedCardIds = expanded,
                    showComplexityGuide = guideVisible,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CheatsheetUiState.Loading,
            )

        fun onSearchQueryChanged(query: String) {
            searchQuery.value = query
        }

        fun onCategorySelected(category: CheatsheetCategory?) {
            selectedCategory.value = category
        }

        fun onToggleBookmarkedOnly() {
            showOnlyBookmarked.value = !showOnlyBookmarked.value
        }

        fun toggleCardExpanded(id: String) {
            val current = expandedCardIds.value
            expandedCardIds.value =
                if (current.contains(id)) {
                    current - id
                } else {
                    current + id
                }
        }

        fun toggleComplexityGuide() {
            showComplexityGuide.value = !showComplexityGuide.value
        }

        fun toggleBookmark(id: String) {
            viewModelScope.launch {
                toggleBookmarkUseCase(id)
            }
        }
    }
