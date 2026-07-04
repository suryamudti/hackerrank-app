package com.hackerrank.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hackerrank.app.data.local.dao.DataStructureDao
import com.hackerrank.app.data.local.dao.ProfileDao
import com.hackerrank.app.data.local.dao.ProgressDao
import com.hackerrank.app.data.local.dao.QuizQuestionDao
import com.hackerrank.app.data.local.entity.DataStructureEntity
import com.hackerrank.app.data.local.entity.QuizQuestionEntity
import com.hackerrank.app.data.local.entity.UserProfileEntity
import com.hackerrank.app.data.local.entity.UserProgressEntity

@Database(
    entities = [
        DataStructureEntity::class,
        QuizQuestionEntity::class,
        UserProgressEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HackerRankDatabase : RoomDatabase() {
    abstract fun dataStructureDao(): DataStructureDao
    abstract fun quizQuestionDao(): QuizQuestionDao
    abstract fun progressDao(): ProgressDao
    abstract fun profileDao(): ProfileDao
}
