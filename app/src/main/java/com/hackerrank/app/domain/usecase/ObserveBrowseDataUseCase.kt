package com.hackerrank.app.domain.usecase

import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class BrowseData(
    val groupedStructures: Map<DataStructureCategory, List<DataStructure>>,
    val progressMap: Map<String, Float>,
)

class ObserveBrowseDataUseCase
    @Inject
    constructor(
        private val contentRepository: ContentRepository,
        private val progressRepository: ProgressRepository,
    ) {
        operator fun invoke(): Flow<BrowseData> {
            return combine(
                contentRepository.getAllStructures(),
                progressRepository.getAllProgress().map { list ->
                    list.associate { it.structureId to it.masteryPercentage / 100f }
                },
            ) { structures, progressMap ->
                val grouped = structures.groupBy { it.category }
                BrowseData(
                    groupedStructures =
                        DataStructureCategory.entries
                            .mapNotNull { cat -> grouped[cat]?.let { cat to it } }
                            .toMap(),
                    progressMap = progressMap,
                )
            }
        }
    }
