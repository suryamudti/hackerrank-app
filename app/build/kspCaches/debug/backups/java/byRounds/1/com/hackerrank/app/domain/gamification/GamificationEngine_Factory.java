package com.hackerrank.app.domain.gamification;

import com.hackerrank.app.domain.repository.ProgressRepository;
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
public final class GamificationEngine_Factory implements Factory<GamificationEngine> {
  private final Provider<ProgressRepository> progressRepositoryProvider;

  public GamificationEngine_Factory(Provider<ProgressRepository> progressRepositoryProvider) {
    this.progressRepositoryProvider = progressRepositoryProvider;
  }

  @Override
  public GamificationEngine get() {
    return newInstance(progressRepositoryProvider.get());
  }

  public static GamificationEngine_Factory create(
      Provider<ProgressRepository> progressRepositoryProvider) {
    return new GamificationEngine_Factory(progressRepositoryProvider);
  }

  public static GamificationEngine newInstance(ProgressRepository progressRepository) {
    return new GamificationEngine(progressRepository);
  }
}
