package com.hackerrank.app.domain.usecase;

import com.hackerrank.app.domain.gamification.GamificationEngine;
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
public final class RecordLoginUseCase_Factory implements Factory<RecordLoginUseCase> {
  private final Provider<GamificationEngine> gamificationEngineProvider;

  public RecordLoginUseCase_Factory(Provider<GamificationEngine> gamificationEngineProvider) {
    this.gamificationEngineProvider = gamificationEngineProvider;
  }

  @Override
  public RecordLoginUseCase get() {
    return newInstance(gamificationEngineProvider.get());
  }

  public static RecordLoginUseCase_Factory create(
      Provider<GamificationEngine> gamificationEngineProvider) {
    return new RecordLoginUseCase_Factory(gamificationEngineProvider);
  }

  public static RecordLoginUseCase newInstance(GamificationEngine gamificationEngine) {
    return new RecordLoginUseCase(gamificationEngine);
  }
}
