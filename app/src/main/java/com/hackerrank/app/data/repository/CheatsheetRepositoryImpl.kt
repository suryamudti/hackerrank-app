package com.hackerrank.app.data.repository

import com.hackerrank.app.data.local.dao.BookmarkedCheatsheetDao
import com.hackerrank.app.data.local.entity.BookmarkedCheatsheetEntity
import com.hackerrank.app.data.seed.CheatsheetSeedData
import com.hackerrank.app.domain.model.CategoryGuide
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
import com.hackerrank.app.domain.model.ProblemCategory
import com.hackerrank.app.domain.repository.CheatsheetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheatsheetRepositoryImpl
    @Inject
    constructor(
        private val bookmarkedCheatsheetDao: BookmarkedCheatsheetDao,
    ) : CheatsheetRepository {
        private val cheatsheets: List<CheatsheetItem> = CheatsheetSeedData.getCheatsheets()
        private val categoryGuides: Map<ProblemCategory, CategoryGuide> = CheatsheetSeedData.getCategoryGuides()

        override fun getAllCheatsheets(): Flow<List<CheatsheetItem>> = flowOf(cheatsheets)

        override fun getCheatsheetById(id: String): Flow<CheatsheetItem?> =
            flowOf(cheatsheets.find { it.id == id })

        override fun getCheatsheetsByCategory(category: ProblemCategory): Flow<List<CheatsheetItem>> =
            flowOf(cheatsheets.filter { it.category == category })

        override fun getCategoryGuides(): Map<ProblemCategory, CategoryGuide> = categoryGuides

        override fun getComplexityRules(): List<ComplexityRule> =
            CheatsheetSeedData.getComplexityRules()

        override fun getBookmarkedCheatsheetIds(): Flow<Set<String>> =
            bookmarkedCheatsheetDao.getBookmarkedIds().map { it.toSet() }

        override fun isBookmarked(cheatsheetId: String): Flow<Boolean> =
            bookmarkedCheatsheetDao.isBookmarked(cheatsheetId)

        override suspend fun toggleBookmark(cheatsheetId: String) {
            if (bookmarkedCheatsheetDao.isBookmarkedSync(cheatsheetId)) {
                bookmarkedCheatsheetDao.delete(cheatsheetId)
            } else {
                bookmarkedCheatsheetDao.insert(BookmarkedCheatsheetEntity(cheatsheetId = cheatsheetId))
            }
        }
    }
