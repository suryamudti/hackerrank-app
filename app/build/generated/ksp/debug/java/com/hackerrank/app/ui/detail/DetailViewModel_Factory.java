package com.hackerrank.app.ui.detail;

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
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<ContentRepository> contentRepositoryProvider;

  private final Provider<ProgressRepository> progressRepositoryProvider;

  public DetailViewModel_Factory(Provider<ContentRepository> contentRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider) {
    this.contentRepositoryProvider = contentRepositoryProvider;
    this.progressRepositoryProvider = progressRepositoryProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(contentRepositoryProvider.get(), progressRepositoryProvider.get());
  }

  public static DetailViewModel_Factory create(
      Provider<ContentRepository> contentRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider) {
    return new DetailViewModel_Factory(contentRepositoryProvider, progressRepositoryProvider);
  }

  public static DetailViewModel newInstance(ContentRepository contentRepository,
      ProgressRepository progressRepository) {
    return new DetailViewModel(contentRepository, progressRepository);
  }
}
