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
        val problemDao = database.problemDao()

        // Seed structures, quizzes, and profile (only if structures haven't been seeded yet)
        if (structureDao.count() == 0) {
            structureDao.insertAll(SeedData.getStructures())
            quizDao.insertAll(SeedData.getQuizQuestionsList())
            profileDao.upsert(SeedData.getDefaultProfile())
        }

        // Seed problems independently (on every fresh install or after DB wipe)
        if (problemDao.count() == 0) {
            problemDao.insertAll(ProblemSeedData.getProblems())
        }
    }
}
