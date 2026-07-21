package com.hackerrank.app.integration

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.hackerrank.app.data.local.HackerRankDatabase
import com.hackerrank.app.data.local.entity.DataStructureEntity
import com.hackerrank.app.data.local.entity.QuizQuestionEntity
import com.hackerrank.app.data.repository.ContentRepositoryImpl
import com.hackerrank.app.data.repository.ProgressRepositoryImpl
import com.hackerrank.app.data.repository.QuizRepositoryImpl
import com.hackerrank.app.domain.gamification.GamificationEngine
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.QuizSession
import com.hackerrank.app.domain.usecase.FinishQuizUseCase
import com.hackerrank.app.domain.usecase.ObserveBrowseDataUseCase
import com.hackerrank.app.domain.usecase.ObserveStructureDetailUseCase
import com.hackerrank.app.domain.usecase.RecordQuizCompleteUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class QuizFlowIntegrationTest {
    private lateinit var db: HackerRankDatabase
    private lateinit var contentRepository: ContentRepositoryImpl
    private lateinit var progressRepository: ProgressRepositoryImpl
    private lateinit var quizRepository: QuizRepositoryImpl
    private lateinit var gamificationEngine: GamificationEngine

    private lateinit var observeBrowseDataUseCase: ObserveBrowseDataUseCase
    private lateinit var observeStructureDetailUseCase: ObserveStructureDetailUseCase
    private lateinit var recordQuizCompleteUseCase: RecordQuizCompleteUseCase
    private lateinit var finishQuizUseCase: FinishQuizUseCase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room.inMemoryDatabaseBuilder(context, HackerRankDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        val gson = Gson()
        contentRepository = ContentRepositoryImpl(db.dataStructureDao(), gson)
        progressRepository =
            ProgressRepositoryImpl(
                progressDao = db.progressDao(),
                profileDao = db.profileDao(),
                quizResultDao = db.quizResultDao(),
                gson = gson,
                context = context,
            )
        quizRepository = QuizRepositoryImpl(db.quizQuestionDao(), gson)
        gamificationEngine = GamificationEngine(progressRepository, progressRepository)

        observeBrowseDataUseCase = ObserveBrowseDataUseCase(contentRepository, progressRepository)
        observeStructureDetailUseCase = ObserveStructureDetailUseCase(contentRepository, progressRepository)
        recordQuizCompleteUseCase = RecordQuizCompleteUseCase(gamificationEngine)
        finishQuizUseCase = FinishQuizUseCase(progressRepository, recordQuizCompleteUseCase)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun completeQuizFlow_updatesDatabaseCorrectly() =
        runTest {
            val structureEntity =
                DataStructureEntity(
                    id = "linked_list",
                    name = "Linked List",
                    slug = "linked-list",
                    category = "Linear",
                    explanation = "A linear data structure",
                    complexityJson = "{}",
                    whenToUseJson = "[]",
                    diagramRes = null,
                    codeExample = "class Node",
                    difficulty = "Easy",
                )
            db.dataStructureDao().insertAll(listOf(structureEntity))

            val questionEntities =
                listOf(
                    QuizQuestionEntity(
                        id = "q1",
                        structureId = "linked_list",
                        question = "What is the access time of Linked List?",
                        optionsJson = "[\"O(1)\",\"O(n)\"]",
                        correctIndex = 1,
                        explanation = "Requires traversal",
                    ),
                    QuizQuestionEntity(
                        id = "q2",
                        structureId = "linked_list",
                        question = "What is the insertion time at head?",
                        optionsJson = "[\"O(1)\",\"O(n)\"]",
                        correctIndex = 0,
                        explanation = "Only requires pointer update",
                    ),
                )
            db.quizQuestionDao().insertAll(questionEntities)

            val browseData = observeBrowseDataUseCase().first()
            val structures = browseData.groupedStructures[DataStructureCategory.LINEAR]
            assertNotNull(structures)
            assertEquals(1, structures!!.size)
            assertEquals("Linked List", structures[0].name)
            assertEquals(0f, browseData.progressMap["linked_list"] ?: 0f)

            val detailData = observeStructureDetailUseCase("linked-list").first()
            assertNotNull(detailData.structure)
            assertEquals("linked_list", detailData.structure!!.id)
            assertNull(detailData.progress)

            val questions = quizRepository.getQuestionsByStructureId("linked_list").first()
            assertEquals(2, questions.size)

            val q1 = questions.find { it.id == "q1" }!!
            val q2 = questions.find { it.id == "q2" }!!
            val answers =
                mapOf(
                    q1.id to q1.correctIndex,
                    q2.id to ((q2.correctIndex + 1) % 2),
                )
            val session =
                QuizSession(
                    questions = questions,
                    answers = answers,
                    startTimeNanos = System.nanoTime(),
                    endTimeNanos = System.nanoTime() + 10_000_000_000L,
                    isCompleted = true,
                )

            val finishResult = finishQuizUseCase(session, "linked_list")

            assertEquals(1, finishResult.updatedProgress.quizzesCompleted)
            assertEquals(1, finishResult.updatedProgress.totalCorrect)
            assertEquals(2, finishResult.updatedProgress.totalQuestions)
            assertEquals(50, finishResult.updatedProgress.masteryLevel)
            assertEquals(50, finishResult.updatedProgress.bestScore * 100 / 2)

            val progressInDb = progressRepository.getProgressByStructureId("linked_list").first()
            assertNotNull(progressInDb)
            assertEquals(1, progressInDb!!.quizzesCompleted)
            assertEquals(1, progressInDb.totalCorrect)
            assertEquals(2, progressInDb.totalQuestions)
            assertEquals(50, progressInDb.masteryLevel)

            val profileInDb = progressRepository.getProfile().first()
            assertNotNull(profileInDb)
            assertEquals(90, profileInDb!!.totalXp)
        }
}
