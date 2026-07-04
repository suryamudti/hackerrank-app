package com.hackerrank.app.data.repository;

import com.hackerrank.app.data.local.dao.DataStructureDao;
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
public final class ContentRepositoryImpl_Factory implements Factory<ContentRepositoryImpl> {
  private final Provider<DataStructureDao> daoProvider;

  public ContentRepositoryImpl_Factory(Provider<DataStructureDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ContentRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static ContentRepositoryImpl_Factory create(Provider<DataStructureDao> daoProvider) {
    return new ContentRepositoryImpl_Factory(daoProvider);
  }

  public static ContentRepositoryImpl newInstance(DataStructureDao dao) {
    return new ContentRepositoryImpl(dao);
  }
}
