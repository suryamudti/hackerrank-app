package com.hackerrank.app.domain.repository

import com.hackerrank.app.domain.model.CategoryGuide
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
import com.hackerrank.app.domain.model.ProblemCategory
import kotlinx.coroutines.flow.Flow

interface CheatsheetRepository {
    fun getAllCheatsheets(): Flow<List<CheatsheetItem>>

    fun getCheatsheetById(id: String): Flow<CheatsheetItem?>

    fun getCheatsheetsByCategory(category: ProblemCategory): Flow<List<CheatsheetItem>>

    fun getCategoryGuides(): Map<ProblemCategory, CategoryGuide>

    fun getComplexityRules(): List<ComplexityRule>

    fun getBookmarkedCheatsheetIds(): Flow<Set<String>>

    fun isBookmarked(cheatsheetId: String): Flow<Boolean>

    suspend fun toggleBookmark(cheatsheetId: String)
}
