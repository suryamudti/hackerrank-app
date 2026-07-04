package com.hackerrank.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.hackerrank.app.data.local.DatabaseModule_ProvideDataStructureDaoFactory;
import com.hackerrank.app.data.local.DatabaseModule_ProvideDatabaseFactory;
import com.hackerrank.app.data.local.DatabaseModule_ProvideProfileDaoFactory;
import com.hackerrank.app.data.local.DatabaseModule_ProvideProgressDaoFactory;
import com.hackerrank.app.data.local.DatabaseModule_ProvideQuizQuestionDaoFactory;
import com.hackerrank.app.data.local.HackerRankDatabase;
import com.hackerrank.app.data.local.dao.DataStructureDao;
import com.hackerrank.app.data.local.dao.ProfileDao;
import com.hackerrank.app.data.local.dao.ProgressDao;
import com.hackerrank.app.data.local.dao.QuizQuestionDao;
import com.hackerrank.app.data.repository.ContentRepositoryImpl;
import com.hackerrank.app.data.repository.ProgressRepositoryImpl;
import com.hackerrank.app.data.repository.QuizRepositoryImpl;
import com.hackerrank.app.data.seed.SeedInitializer;
import com.hackerrank.app.domain.gamification.GamificationEngine;
import com.hackerrank.app.domain.usecase.RecordLoginUseCase;
import com.hackerrank.app.domain.usecase.RecordQuizCompleteUseCase;
import com.hackerrank.app.ui.achievements.AchievementsViewModel;
import com.hackerrank.app.ui.achievements.AchievementsViewModel_HiltModules;
import com.hackerrank.app.ui.browse.BrowseViewModel;
import com.hackerrank.app.ui.browse.BrowseViewModel_HiltModules;
import com.hackerrank.app.ui.detail.DetailViewModel;
import com.hackerrank.app.ui.detail.DetailViewModel_HiltModules;
import com.hackerrank.app.ui.progress.ProgressViewModel;
import com.hackerrank.app.ui.progress.ProgressViewModel_HiltModules;
import com.hackerrank.app.ui.quiz.QuizViewModel;
import com.hackerrank.app.ui.quiz.QuizViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerHackerRankApp_HiltComponents_SingletonC {
  private DaggerHackerRankApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public HackerRankApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements HackerRankApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public HackerRankApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements HackerRankApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public HackerRankApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements HackerRankApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public HackerRankApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements HackerRankApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public HackerRankApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements HackerRankApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public HackerRankApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements HackerRankApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public HackerRankApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements HackerRankApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public HackerRankApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends HackerRankApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends HackerRankApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends HackerRankApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends HackerRankApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    private RecordLoginUseCase recordLoginUseCase() {
      return new RecordLoginUseCase(singletonCImpl.gamificationEngineProvider.get());
    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(5).put(LazyClassKeyProvider.com_hackerrank_app_ui_achievements_AchievementsViewModel, AchievementsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_hackerrank_app_ui_browse_BrowseViewModel, BrowseViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_hackerrank_app_ui_detail_DetailViewModel, DetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_hackerrank_app_ui_progress_ProgressViewModel, ProgressViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_hackerrank_app_ui_quiz_QuizViewModel, QuizViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectSeedInitializer(instance, singletonCImpl.seedInitializerProvider.get());
      MainActivity_MembersInjector.injectRecordLoginUseCase(instance, recordLoginUseCase());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_hackerrank_app_ui_detail_DetailViewModel = "com.hackerrank.app.ui.detail.DetailViewModel";

      static String com_hackerrank_app_ui_progress_ProgressViewModel = "com.hackerrank.app.ui.progress.ProgressViewModel";

      static String com_hackerrank_app_ui_browse_BrowseViewModel = "com.hackerrank.app.ui.browse.BrowseViewModel";

      static String com_hackerrank_app_ui_achievements_AchievementsViewModel = "com.hackerrank.app.ui.achievements.AchievementsViewModel";

      static String com_hackerrank_app_ui_quiz_QuizViewModel = "com.hackerrank.app.ui.quiz.QuizViewModel";

      @KeepFieldType
      DetailViewModel com_hackerrank_app_ui_detail_DetailViewModel2;

      @KeepFieldType
      ProgressViewModel com_hackerrank_app_ui_progress_ProgressViewModel2;

      @KeepFieldType
      BrowseViewModel com_hackerrank_app_ui_browse_BrowseViewModel2;

      @KeepFieldType
      AchievementsViewModel com_hackerrank_app_ui_achievements_AchievementsViewModel2;

      @KeepFieldType
      QuizViewModel com_hackerrank_app_ui_quiz_QuizViewModel2;
    }
  }

  private static final class ViewModelCImpl extends HackerRankApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AchievementsViewModel> achievementsViewModelProvider;

    private Provider<BrowseViewModel> browseViewModelProvider;

    private Provider<DetailViewModel> detailViewModelProvider;

    private Provider<ProgressViewModel> progressViewModelProvider;

    private Provider<QuizViewModel> quizViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private RecordQuizCompleteUseCase recordQuizCompleteUseCase() {
      return new RecordQuizCompleteUseCase(singletonCImpl.gamificationEngineProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.achievementsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.browseViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.detailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.progressViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.quizViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(5).put(LazyClassKeyProvider.com_hackerrank_app_ui_achievements_AchievementsViewModel, ((Provider) achievementsViewModelProvider)).put(LazyClassKeyProvider.com_hackerrank_app_ui_browse_BrowseViewModel, ((Provider) browseViewModelProvider)).put(LazyClassKeyProvider.com_hackerrank_app_ui_detail_DetailViewModel, ((Provider) detailViewModelProvider)).put(LazyClassKeyProvider.com_hackerrank_app_ui_progress_ProgressViewModel, ((Provider) progressViewModelProvider)).put(LazyClassKeyProvider.com_hackerrank_app_ui_quiz_QuizViewModel, ((Provider) quizViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_hackerrank_app_ui_achievements_AchievementsViewModel = "com.hackerrank.app.ui.achievements.AchievementsViewModel";

      static String com_hackerrank_app_ui_detail_DetailViewModel = "com.hackerrank.app.ui.detail.DetailViewModel";

      static String com_hackerrank_app_ui_progress_ProgressViewModel = "com.hackerrank.app.ui.progress.ProgressViewModel";

      static String com_hackerrank_app_ui_quiz_QuizViewModel = "com.hackerrank.app.ui.quiz.QuizViewModel";

      static String com_hackerrank_app_ui_browse_BrowseViewModel = "com.hackerrank.app.ui.browse.BrowseViewModel";

      @KeepFieldType
      AchievementsViewModel com_hackerrank_app_ui_achievements_AchievementsViewModel2;

      @KeepFieldType
      DetailViewModel com_hackerrank_app_ui_detail_DetailViewModel2;

      @KeepFieldType
      ProgressViewModel com_hackerrank_app_ui_progress_ProgressViewModel2;

      @KeepFieldType
      QuizViewModel com_hackerrank_app_ui_quiz_QuizViewModel2;

      @KeepFieldType
      BrowseViewModel com_hackerrank_app_ui_browse_BrowseViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.hackerrank.app.ui.achievements.AchievementsViewModel 
          return (T) new AchievementsViewModel(singletonCImpl.progressRepositoryImplProvider.get());

          case 1: // com.hackerrank.app.ui.browse.BrowseViewModel 
          return (T) new BrowseViewModel(singletonCImpl.contentRepositoryImplProvider.get());

          case 2: // com.hackerrank.app.ui.detail.DetailViewModel 
          return (T) new DetailViewModel(singletonCImpl.contentRepositoryImplProvider.get(), singletonCImpl.progressRepositoryImplProvider.get());

          case 3: // com.hackerrank.app.ui.progress.ProgressViewModel 
          return (T) new ProgressViewModel(singletonCImpl.progressRepositoryImplProvider.get(), singletonCImpl.contentRepositoryImplProvider.get());

          case 4: // com.hackerrank.app.ui.quiz.QuizViewModel 
          return (T) new QuizViewModel(singletonCImpl.contentRepositoryImplProvider.get(), singletonCImpl.quizRepositoryImplProvider.get(), singletonCImpl.progressRepositoryImplProvider.get(), viewModelCImpl.recordQuizCompleteUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends HackerRankApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends HackerRankApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends HackerRankApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<HackerRankDatabase> provideDatabaseProvider;

    private Provider<SeedInitializer> seedInitializerProvider;

    private Provider<ProgressRepositoryImpl> progressRepositoryImplProvider;

    private Provider<GamificationEngine> gamificationEngineProvider;

    private Provider<ContentRepositoryImpl> contentRepositoryImplProvider;

    private Provider<QuizRepositoryImpl> quizRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private ProgressDao progressDao() {
      return DatabaseModule_ProvideProgressDaoFactory.provideProgressDao(provideDatabaseProvider.get());
    }

    private ProfileDao profileDao() {
      return DatabaseModule_ProvideProfileDaoFactory.provideProfileDao(provideDatabaseProvider.get());
    }

    private DataStructureDao dataStructureDao() {
      return DatabaseModule_ProvideDataStructureDaoFactory.provideDataStructureDao(provideDatabaseProvider.get());
    }

    private QuizQuestionDao quizQuestionDao() {
      return DatabaseModule_ProvideQuizQuestionDaoFactory.provideQuizQuestionDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<HackerRankDatabase>(singletonCImpl, 1));
      this.seedInitializerProvider = DoubleCheck.provider(new SwitchingProvider<SeedInitializer>(singletonCImpl, 0));
      this.progressRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ProgressRepositoryImpl>(singletonCImpl, 3));
      this.gamificationEngineProvider = DoubleCheck.provider(new SwitchingProvider<GamificationEngine>(singletonCImpl, 2));
      this.contentRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ContentRepositoryImpl>(singletonCImpl, 4));
      this.quizRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<QuizRepositoryImpl>(singletonCImpl, 5));
    }

    @Override
    public void injectHackerRankApp(HackerRankApp hackerRankApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.hackerrank.app.data.seed.SeedInitializer 
          return (T) new SeedInitializer(singletonCImpl.provideDatabaseProvider.get());

          case 1: // com.hackerrank.app.data.local.HackerRankDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.hackerrank.app.domain.gamification.GamificationEngine 
          return (T) new GamificationEngine(singletonCImpl.progressRepositoryImplProvider.get());

          case 3: // com.hackerrank.app.data.repository.ProgressRepositoryImpl 
          return (T) new ProgressRepositoryImpl(singletonCImpl.progressDao(), singletonCImpl.profileDao());

          case 4: // com.hackerrank.app.data.repository.ContentRepositoryImpl 
          return (T) new ContentRepositoryImpl(singletonCImpl.dataStructureDao());

          case 5: // com.hackerrank.app.data.repository.QuizRepositoryImpl 
          return (T) new QuizRepositoryImpl(singletonCImpl.quizQuestionDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
