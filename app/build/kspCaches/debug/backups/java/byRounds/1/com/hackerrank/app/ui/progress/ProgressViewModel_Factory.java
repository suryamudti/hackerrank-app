package com.hackerrank.app.ui.progress;

import com.hackerrank.app.domain.repository.ContentRepository;
import com.hackerrank.app.domain.repository.ProgressRepository;
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
public final class ProgressViewModel_Factory implements Factory<ProgressViewModel> {
  private final Provider<ProgressRepository> progressRepositoryProvider;

  private final Provider<ContentRepository> contentRepositoryProvider;

  public ProgressViewModel_Factory(Provider<ProgressRepository> progressRepositoryProvider,
      Provider<ContentRepository> contentRepositoryProvider) {
    this.progressRepositoryProvider = progressRepositoryProvider;
    this.contentRepositoryProvider = contentRepositoryProvider;
  }

  @Override
  public ProgressViewModel get() {
    return newInstance(progressRepositoryProvider.get(), contentRepositoryProvider.get());
  }

  public static ProgressViewModel_Factory create(
      Provider<ProgressRepository> progressRepositoryProvider,
      Provider<ContentRepository> contentRepositoryProvider) {
    return new ProgressViewModel_Factory(progressRepositoryProvider, contentRepositoryProvider);
  }

  public static ProgressViewModel newInstance(ProgressRepository progressRepository,
      ContentRepository contentRepository) {
    return new ProgressViewModel(progressRepository, contentRepository);
  }
}
