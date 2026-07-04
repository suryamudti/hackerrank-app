package com.hackerrank.app.data.seed

import com.hackerrank.app.data.local.HackerRankDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedInitializer @Inject constructor(
    private val database: HackerRankDatabase
) {
    suspend fun initializeIfNeeded() {
        val structureDao = database.dataStructureDao()
        val quizDao = database.quizQuestionDao()
        val profileDao = database.profileDao()

        if (structureDao.count() > 0) return

        structureDao.insertAll(SeedData.getStructures())
        quizDao.insertAll(SeedData.getQuizQuestionsList())
        profileDao.upsert(SeedData.getDefaultProfile())
    }
}
