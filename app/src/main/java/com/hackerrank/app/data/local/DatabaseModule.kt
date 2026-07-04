package com.hackerrank.app.data.local

import android.content.Context
import androidx.room.Room
import com.hackerrank.app.data.local.dao.DataStructureDao
import com.hackerrank.app.data.local.dao.ProfileDao
import com.hackerrank.app.data.local.dao.ProgressDao
import com.hackerrank.app.data.local.dao.QuizQuestionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HackerRankDatabase {
        return Room.databaseBuilder(
            context,
            HackerRankDatabase::class.java,
            "hackerrank_app.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDataStructureDao(database: HackerRankDatabase): DataStructureDao =
        database.dataStructureDao()

    @Provides
    fun provideQuizQuestionDao(database: HackerRankDatabase): QuizQuestionDao =
        database.quizQuestionDao()

    @Provides
    fun provideProgressDao(database: HackerRankDatabase): ProgressDao =
        database.progressDao()

    @Provides
    fun provideProfileDao(database: HackerRankDatabase): ProfileDao =
        database.profileDao()
}
