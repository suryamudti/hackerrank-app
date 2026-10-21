package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.repository.CheatsheetRepository
import javax.inject.Inject

class ToggleCheatsheetBookmarkUseCase
    @Inject
    constructor(
        private val repository: CheatsheetRepository,
    ) {
        suspend operator fun invoke(cheatsheetId: String) {
            repository.toggleBookmark(cheatsheetId)
        }
    }
