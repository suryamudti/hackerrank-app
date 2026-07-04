package com.hackerrank.app.di

import com.hackerrank.app.data.repository.ContentRepositoryImpl
import com.hackerrank.app.data.repository.ProblemRepositoryImpl
import com.hackerrank.app.data.repository.ProgressRepositoryImpl
import com.hackerrank.app.data.repository.QuizRepositoryImpl
import com.hackerrank.app.domain.repository.ContentRepository
import com.hackerrank.app.domain.repository.ProblemRepository
import com.hackerrank.app.domain.repository.ProgressRepository
import com.hackerrank.app.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindContentRepository(impl: ContentRepositoryImpl): ContentRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindQuizRepository(impl: QuizRepositoryImpl): QuizRepository

    @Binds
    @Singleton
    abstract fun bindProblemRepository(impl: ProblemRepositoryImpl): ProblemRepository
}
