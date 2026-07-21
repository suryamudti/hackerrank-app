package com.hackerrank.app.data.seed

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hackerrank.app.data.local.HackerRankDatabase
import com.hackerrank.app.data.local.entity.DataStructureEntity
import com.hackerrank.app.data.local.entity.ProblemEntity
import com.hackerrank.app.data.local.entity.QuizQuestionEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedInitializer
    @Inject
    constructor(
        private val database: HackerRankDatabase,
        private val gson: Gson,
        @ApplicationContext private val context: Context,
    ) {
        suspend fun initializeIfNeeded() {
            val structureDao = database.dataStructureDao()
            val quizDao = database.quizQuestionDao()
            val profileDao = database.profileDao()
            val problemDao = database.problemDao()

            if (structureDao.count() == 0) {
                val structures = loadStructures()
                structureDao.insertAll(structures)
                val quizzes = loadQuizzes()
                quizDao.insertAll(quizzes)
                profileDao.upsert(SeedData.getDefaultProfile())
            }

            if (problemDao.count() == 0) {
                val problems = loadProblems()
                problemDao.insertAll(problems)
            }
        }

        private fun loadStructures(): List<DataStructureEntity> {
            return try {
                val json = context.assets.open("seed/structures.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<DataStructureEntity>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                Log.w("SeedInitializer", "Failed to load structures from assets, using fallback", e)
                SeedData.getStructures()
            }
        }

        private fun loadQuizzes(): List<QuizQuestionEntity> {
            return try {
                val json = context.assets.open("seed/quizzes.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<QuizQuestionEntity>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                Log.w("SeedInitializer", "Failed to load quizzes from assets, using fallback", e)
                SeedData.getQuizQuestionsList()
            }
        }

        private fun loadProblems(): List<ProblemEntity> {
            return try {
                val json = context.assets.open("seed/problems.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<ProblemEntity>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                Log.w("SeedInitializer", "Failed to load problems from assets, using fallback", e)
                ProblemSeedData.getProblems()
            }
        }
    }
