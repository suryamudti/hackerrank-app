package com.hackerrank.app.ui.quiz;

import com.hackerrank.app.domain.repository.ContentRepository;
import com.hackerrank.app.domain.repository.ProgressRepository;
import com.hackerrank.app.domain.repository.QuizRepository;
import com.hackerrank.app.domain.usecase.RecordQuizCompleteUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class QuizViewModel_Factory implements Factory<QuizViewModel> {
  private final Provider<ContentRepository> contentRepositoryProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<ProgressRepository> progressRepositoryProvider;

  private final Provider<RecordQuizCompleteUseCase> recordQuizCompleteUseCaseProvider;

  public QuizViewModel_Factory(Provider<ContentRepository> contentRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider,
      Provider<RecordQuizCompleteUseCase> recordQuizCompleteUseCaseProvider) {
    this.contentRepositoryProvider = contentRepositoryProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.progressRepositoryProvider = progressRepositoryProvider;
    this.recordQuizCompleteUseCaseProvider = recordQuizCompleteUseCaseProvider;
  }

  @Override
  public QuizViewModel get() {
    return newInstance(contentRepositoryProvider.get(), quizRepositoryProvider.get(), progressRepositoryProvider.get(), recordQuizCompleteUseCaseProvider.get());
  }

  public static QuizViewModel_Factory create(Provider<ContentRepository> contentRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider,
      Provider<RecordQuizCompleteUseCase> recordQuizCompleteUseCaseProvider) {
    return new QuizViewModel_Factory(contentRepositoryProvider, quizRepositoryProvider, progressRepositoryProvider, recordQuizCompleteUseCaseProvider);
  }

  public static QuizViewModel newInstance(ContentRepository contentRepository,
      QuizRepository quizRepository, ProgressRepository progressRepository,
      RecordQuizCompleteUseCase recordQuizCompleteUseCase) {
    return new QuizViewModel(contentRepository, quizRepository, progressRepository, recordQuizCompleteUseCase);
  }
}
