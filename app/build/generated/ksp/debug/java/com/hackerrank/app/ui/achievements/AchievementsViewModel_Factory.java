package com.hackerrank.app.ui.achievements;

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
public final class AchievementsViewModel_Factory implements Factory<AchievementsViewModel> {
  private final Provider<ProgressRepository> progressRepositoryProvider;

  public AchievementsViewModel_Factory(Provider<ProgressRepository> progressRepositoryProvider) {
    this.progressRepositoryProvider = progressRepositoryProvider;
  }

  @Override
  public AchievementsViewModel get() {
    return newInstance(progressRepositoryProvider.get());
  }

  public static AchievementsViewModel_Factory create(
      Provider<ProgressRepository> progressRepositoryProvider) {
    return new AchievementsViewModel_Factory(progressRepositoryProvider);
  }

  public static AchievementsViewModel newInstance(ProgressRepository progressRepository) {
    return new AchievementsViewModel(progressRepository);
  }
}
