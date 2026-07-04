package com.hackerrank.app.data.seed;

import com.hackerrank.app.data.local.HackerRankDatabase;
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
public final class SeedInitializer_Factory implements Factory<SeedInitializer> {
  private final Provider<HackerRankDatabase> databaseProvider;

  public SeedInitializer_Factory(Provider<HackerRankDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SeedInitializer get() {
    return newInstance(databaseProvider.get());
  }

  public static SeedInitializer_Factory create(Provider<HackerRankDatabase> databaseProvider) {
    return new SeedInitializer_Factory(databaseProvider);
  }

  public static SeedInitializer newInstance(HackerRankDatabase database) {
    return new SeedInitializer(database);
  }
}
