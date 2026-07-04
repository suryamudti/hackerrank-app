package com.hackerrank.app.data.local;

import com.hackerrank.app.data.local.dao.ProgressDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideProgressDaoFactory implements Factory<ProgressDao> {
  private final Provider<HackerRankDatabase> databaseProvider;

  public DatabaseModule_ProvideProgressDaoFactory(Provider<HackerRankDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ProgressDao get() {
    return provideProgressDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideProgressDaoFactory create(
      Provider<HackerRankDatabase> databaseProvider) {
    return new DatabaseModule_ProvideProgressDaoFactory(databaseProvider);
  }

  public static ProgressDao provideProgressDao(HackerRankDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProgressDao(database));
  }
}
