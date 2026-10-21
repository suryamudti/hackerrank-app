package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
import com.hackerrank.app.domain.repository.CheatsheetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class CheatsheetsData(
    val cheatsheets: List<CheatsheetItem>,
    val bookmarkedIds: Set<String>,
    val complexityRules: List<ComplexityRule>,
)

class ObserveCheatsheetsUseCase
    @Inject
    constructor(
        private val cheatsheetRepository: CheatsheetRepository,
    ) {
        operator fun invoke(): Flow<CheatsheetsData> =
            combine(
                cheatsheetRepository.getAllCheatsheets(),
                cheatsheetRepository.getBookmarkedCheatsheetIds(),
            ) { cheatsheets, bookmarkedIds ->
                CheatsheetsData(
                    cheatsheets = cheatsheets,
                    bookmarkedIds = bookmarkedIds,
                    complexityRules = cheatsheetRepository.getComplexityRules(),
                )
            }
    }
