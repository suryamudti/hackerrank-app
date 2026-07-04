package com.hackerrank.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hackerrank.app.data.local.dao.DataStructureDao
import com.hackerrank.app.data.local.dao.ProblemDao
import com.hackerrank.app.data.local.dao.ProfileDao
import com.hackerrank.app.data.local.dao.ProgressDao
import com.hackerrank.app.data.local.dao.QuizQuestionDao
import com.hackerrank.app.data.local.dao.SolvedProblemDao
import com.hackerrank.app.data.local.entity.DataStructureEntity
import com.hackerrank.app.data.local.entity.ProblemEntity
import com.hackerrank.app.data.local.entity.QuizQuestionEntity
import com.hackerrank.app.data.local.entity.SolvedProblemEntity
import com.hackerrank.app.data.local.entity.UserProfileEntity
import com.hackerrank.app.data.local.entity.UserProgressEntity

@Database(
    entities = [
        DataStructureEntity::class,
        QuizQuestionEntity::class,
        UserProgressEntity::class,
        UserProfileEntity::class,
        ProblemEntity::class,
        SolvedProblemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class HackerRankDatabase : RoomDatabase() {
    abstract fun dataStructureDao(): DataStructureDao
    abstract fun quizQuestionDao(): QuizQuestionDao
    abstract fun progressDao(): ProgressDao
    abstract fun profileDao(): ProfileDao
    abstract fun problemDao(): ProblemDao
    abstract fun solvedProblemDao(): SolvedProblemDao
}
