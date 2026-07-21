package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.UserProgress
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class StructureDetailData(
    val structure: DataStructure?,
    val progress: UserProgress?,
)

class ObserveStructureDetailUseCase
    @Inject
    constructor(
        private val contentRepository: ContentRepository,
        private val progressRepository: ProgressRepository,
    ) {
        operator fun invoke(slug: String): Flow<StructureDetailData> =
            flow {
                val structure = contentRepository.getStructureBySlug(slug)
                if (structure != null) {
                    emitAll(
                        progressRepository.getProgressByStructureId(structure.id).map { progress ->
                            StructureDetailData(structure = structure, progress = progress)
                        },
                    )
                } else {
                    emit(StructureDetailData(structure = null, progress = null))
                }
            }
    }
