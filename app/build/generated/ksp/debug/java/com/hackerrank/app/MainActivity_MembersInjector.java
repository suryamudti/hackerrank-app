package com.hackerrank.app;

import com.hackerrank.app.data.seed.SeedInitializer;
import com.hackerrank.app.domain.usecase.RecordLoginUseCase;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<SeedInitializer> seedInitializerProvider;

  private final Provider<RecordLoginUseCase> recordLoginUseCaseProvider;

  public MainActivity_MembersInjector(Provider<SeedInitializer> seedInitializerProvider,
      Provider<RecordLoginUseCase> recordLoginUseCaseProvider) {
    this.seedInitializerProvider = seedInitializerProvider;
    this.recordLoginUseCaseProvider = recordLoginUseCaseProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<SeedInitializer> seedInitializerProvider,
      Provider<RecordLoginUseCase> recordLoginUseCaseProvider) {
    return new MainActivity_MembersInjector(seedInitializerProvider, recordLoginUseCaseProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectSeedInitializer(instance, seedInitializerProvider.get());
    injectRecordLoginUseCase(instance, recordLoginUseCaseProvider.get());
  }

  @InjectedFieldSignature("com.hackerrank.app.MainActivity.seedInitializer")
  public static void injectSeedInitializer(MainActivity instance, SeedInitializer seedInitializer) {
    instance.seedInitializer = seedInitializer;
  }

  @InjectedFieldSignature("com.hackerrank.app.MainActivity.recordLoginUseCase")
  public static void injectRecordLoginUseCase(MainActivity instance,
      RecordLoginUseCase recordLoginUseCase) {
    instance.recordLoginUseCase = recordLoginUseCase;
  }
}
