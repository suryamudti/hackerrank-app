package com.hackerrank.app.data.local;

import com.hackerrank.app.data.local.dao.QuizQuestionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DatabaseModule_ProvideQuizQuestionDaoFactory implements Factory<QuizQuestionDao> {
  private final Provider<HackerRankDatabase> databaseProvider;

  public DatabaseModule_ProvideQuizQuestionDaoFactory(
      Provider<HackerRankDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public QuizQuestionDao get() {
    return provideQuizQuestionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideQuizQuestionDaoFactory create(
      Provider<HackerRankDatabase> databaseProvider) {
    return new DatabaseModule_ProvideQuizQuestionDaoFactory(databaseProvider);
  }

  public static QuizQuestionDao provideQuizQuestionDao(HackerRankDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideQuizQuestionDao(database));
  }
}
