package com.hackerrank.app.ui.browse;

import com.hackerrank.app.domain.repository.ContentRepository;
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
public final class BrowseViewModel_Factory implements Factory<BrowseViewModel> {
  private final Provider<ContentRepository> contentRepositoryProvider;

  public BrowseViewModel_Factory(Provider<ContentRepository> contentRepositoryProvider) {
    this.contentRepositoryProvider = contentRepositoryProvider;
  }

  @Override
  public BrowseViewModel get() {
    return newInstance(contentRepositoryProvider.get());
  }

  public static BrowseViewModel_Factory create(
      Provider<ContentRepository> contentRepositoryProvider) {
    return new BrowseViewModel_Factory(contentRepositoryProvider);
  }

  public static BrowseViewModel newInstance(ContentRepository contentRepository) {
    return new BrowseViewModel(contentRepository);
  }
}
