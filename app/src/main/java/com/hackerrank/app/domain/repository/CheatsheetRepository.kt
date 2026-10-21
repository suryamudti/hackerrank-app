package com.hackerrank.app.domain.repository

import com.hackerrank.app.domain.model.CheatsheetCategory
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
import kotlinx.coroutines.flow.Flow

interface CheatsheetRepository {
    fun getAllCheatsheets(): Flow<List<CheatsheetItem>>

    fun getCheatsheetById(id: String): Flow<CheatsheetItem?>

    fun getCheatsheetsByCategory(category: CheatsheetCategory): Flow<List<CheatsheetItem>>

    fun getComplexityRules(): List<ComplexityRule>

    fun getBookmarkedCheatsheetIds(): Flow<Set<String>>

    fun isBookmarked(cheatsheetId: String): Flow<Boolean>

    suspend fun toggleBookmark(cheatsheetId: String)
}
