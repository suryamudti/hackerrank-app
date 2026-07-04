package com.hackerrank.app.data.repository;

import com.hackerrank.app.data.local.dao.QuizQuestionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class QuizRepositoryImpl_Factory implements Factory<QuizRepositoryImpl> {
  private final Provider<QuizQuestionDao> daoProvider;

  public QuizRepositoryImpl_Factory(Provider<QuizQuestionDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public QuizRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static QuizRepositoryImpl_Factory create(Provider<QuizQuestionDao> daoProvider) {
    return new QuizRepositoryImpl_Factory(daoProvider);
  }

  public static QuizRepositoryImpl newInstance(QuizQuestionDao dao) {
    return new QuizRepositoryImpl(dao);
  }
}
