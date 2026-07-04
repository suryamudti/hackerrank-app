package com.hackerrank.app.data.repository;

import com.hackerrank.app.data.local.dao.ProfileDao;
import com.hackerrank.app.data.local.dao.ProgressDao;
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
public final class ProgressRepositoryImpl_Factory implements Factory<ProgressRepositoryImpl> {
  private final Provider<ProgressDao> progressDaoProvider;

  private final Provider<ProfileDao> profileDaoProvider;

  public ProgressRepositoryImpl_Factory(Provider<ProgressDao> progressDaoProvider,
      Provider<ProfileDao> profileDaoProvider) {
    this.progressDaoProvider = progressDaoProvider;
    this.profileDaoProvider = profileDaoProvider;
  }

  @Override
  public ProgressRepositoryImpl get() {
    return newInstance(progressDaoProvider.get(), profileDaoProvider.get());
  }

  public static ProgressRepositoryImpl_Factory create(Provider<ProgressDao> progressDaoProvider,
      Provider<ProfileDao> profileDaoProvider) {
    return new ProgressRepositoryImpl_Factory(progressDaoProvider, profileDaoProvider);
  }

  public static ProgressRepositoryImpl newInstance(ProgressDao progressDao, ProfileDao profileDao) {
    return new ProgressRepositoryImpl(progressDao, profileDao);
  }
}
