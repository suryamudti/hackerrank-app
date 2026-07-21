package com.hackerrank.app.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.hackerrank.app.data.local.dao.DataStructureDao
import com.hackerrank.app.data.local.dao.ProblemDao
import com.hackerrank.app.data.local.dao.ProfileDao
import com.hackerrank.app.data.local.dao.ProgressDao
import com.hackerrank.app.data.local.dao.QuizQuestionDao
import com.hackerrank.app.data.local.dao.QuizResultDao
import com.hackerrank.app.data.local.dao.SolvedProblemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val MIGRATION_1_2 =
        object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `problems` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `title` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `solutionCode` TEXT NOT NULL,
                        `approachExplanation` TEXT NOT NULL,
                        `difficulty` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `orderIndex` INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `solved_problems` (
                        `problemId` TEXT NOT NULL PRIMARY KEY,
                        `solvedAt` INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
            }
        }

    private val MIGRATION_2_3 =
        object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `problems` ADD COLUMN `inputExample` TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE `problems` ADD COLUMN `outputExample` TEXT NOT NULL DEFAULT ''")
            }
        }

    private val MIGRATION_3_4 =
        object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE INDEX IF NOT EXISTS idx_problems_category ON problems(category)")
            }
        }

    private val MIGRATION_4_5 =
        object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                CREATE TABLE IF NOT EXISTS `quiz_results` (
                    `id` TEXT NOT NULL,
                    `structure_id` TEXT NOT NULL,
                    `score` INTEGER NOT NULL,
                    `total_questions` INTEGER NOT NULL,
                    `xp_earned` INTEGER NOT NULL,
                    `completed_at` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
            """,
                )
            }
        }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): HackerRankDatabase {
        return Room.databaseBuilder(
            context,
            HackerRankDatabase::class.java,
            "hackerrank_app.db",
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
            .build()
    }

    @Provides
    fun provideDataStructureDao(database: HackerRankDatabase): DataStructureDao = database.dataStructureDao()

    @Provides
    fun provideQuizQuestionDao(database: HackerRankDatabase): QuizQuestionDao = database.quizQuestionDao()

    @Provides
    fun provideProgressDao(database: HackerRankDatabase): ProgressDao = database.progressDao()

    @Provides
    fun provideProfileDao(database: HackerRankDatabase): ProfileDao = database.profileDao()

    @Provides
    fun provideProblemDao(database: HackerRankDatabase): ProblemDao = database.problemDao()

    @Provides
    fun provideSolvedProblemDao(database: HackerRankDatabase): SolvedProblemDao = database.solvedProblemDao()

    @Provides
    fun provideQuizResultDao(database: HackerRankDatabase): QuizResultDao = database.quizResultDao()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}
