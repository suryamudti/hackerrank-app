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
public final class RecordQuizCompleteUseCase_Factory implements Factory<RecordQuizCompleteUseCase> {
  private final Provider<GamificationEngine> gamificationEngineProvider;

  public RecordQuizCompleteUseCase_Factory(
      Provider<GamificationEngine> gamificationEngineProvider) {
    this.gamificationEngineProvider = gamificationEngineProvider;
  }

  @Override
  public RecordQuizCompleteUseCase get() {
    return newInstance(gamificationEngineProvider.get());
  }

  public static RecordQuizCompleteUseCase_Factory create(
      Provider<GamificationEngine> gamificationEngineProvider) {
    return new RecordQuizCompleteUseCase_Factory(gamificationEngineProvider);
  }

  public static RecordQuizCompleteUseCase newInstance(GamificationEngine gamificationEngine) {
    return new RecordQuizCompleteUseCase(gamificationEngine);
  }
}
