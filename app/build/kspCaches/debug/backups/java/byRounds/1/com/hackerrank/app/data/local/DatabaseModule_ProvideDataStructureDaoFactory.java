package com.hackerrank.app.data.local;

import com.hackerrank.app.data.local.dao.DataStructureDao;
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
public final class DatabaseModule_ProvideDataStructureDaoFactory implements Factory<DataStructureDao> {
  private final Provider<HackerRankDatabase> databaseProvider;

  public DatabaseModule_ProvideDataStructureDaoFactory(
      Provider<HackerRankDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DataStructureDao get() {
    return provideDataStructureDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDataStructureDaoFactory create(
      Provider<HackerRankDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDataStructureDaoFactory(databaseProvider);
  }

  public static DataStructureDao provideDataStructureDao(HackerRankDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDataStructureDao(database));
  }
}
