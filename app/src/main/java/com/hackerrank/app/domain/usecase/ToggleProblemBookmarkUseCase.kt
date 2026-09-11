package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.repository.ProblemRepository
import javax.inject.Inject

class ToggleProblemBookmarkUseCase
    @Inject
    constructor(
        private val problemRepository: ProblemRepository,
    ) {
        suspend operator fun invoke(problemId: String) {
            problemRepository.toggleBookmark(problemId)
        }
    }
