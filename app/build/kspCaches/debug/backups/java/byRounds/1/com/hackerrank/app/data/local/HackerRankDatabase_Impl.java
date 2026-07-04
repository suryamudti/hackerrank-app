package com.hackerrank.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.hackerrank.app.data.local.dao.DataStructureDao;
import com.hackerrank.app.data.local.dao.DataStructureDao_Impl;
import com.hackerrank.app.data.local.dao.ProfileDao;
import com.hackerrank.app.data.local.dao.ProfileDao_Impl;
import com.hackerrank.app.data.local.dao.ProgressDao;
import com.hackerrank.app.data.local.dao.ProgressDao_Impl;
import com.hackerrank.app.data.local.dao.QuizQuestionDao;
import com.hackerrank.app.data.local.dao.QuizQuestionDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HackerRankDatabase_Impl extends HackerRankDatabase {
  private volatile DataStructureDao _dataStructureDao;

  private volatile QuizQuestionDao _quizQuestionDao;

  private volatile ProgressDao _progressDao;

  private volatile ProfileDao _profileDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `data_structures` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `slug` TEXT NOT NULL, `category` TEXT NOT NULL, `explanation` TEXT NOT NULL, `complexity_json` TEXT NOT NULL, `when_to_use_json` TEXT NOT NULL, `diagram_res` TEXT, `code_example` TEXT NOT NULL, `difficulty` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quiz_questions` (`id` TEXT NOT NULL, `structure_id` TEXT NOT NULL, `question` TEXT NOT NULL, `options_json` TEXT NOT NULL, `correct_index` INTEGER NOT NULL, `explanation` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`structure_id`) REFERENCES `data_structures`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_quiz_questions_structure_id` ON `quiz_questions` (`structure_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_progress` (`structure_id` TEXT NOT NULL, `quizzes_completed` INTEGER NOT NULL, `total_correct` INTEGER NOT NULL, `total_questions` INTEGER NOT NULL, `best_score` INTEGER NOT NULL, `mastery_level` INTEGER NOT NULL, PRIMARY KEY(`structure_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` INTEGER NOT NULL, `total_xp` INTEGER NOT NULL, `current_streak` INTEGER NOT NULL, `longest_streak` INTEGER NOT NULL, `last_active_date` TEXT, `earned_badge_ids_json` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a7eff95a8163ff7ff7af2b784bc03e11')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `data_structures`");
        db.execSQL("DROP TABLE IF EXISTS `quiz_questions`");
        db.execSQL("DROP TABLE IF EXISTS `user_progress`");
        db.execSQL("DROP TABLE IF EXISTS `user_profile`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDataStructures = new HashMap<String, TableInfo.Column>(10);
        _columnsDataStructures.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataStructures.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataStructures.put("slug", new TableInfo.Column("slug", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataStructures.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataStructures.put("explanation", new TableInfo.Column("explanation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataStructures.put("complexity_json", new TableInfo.Column("complexity_json", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataStructures.put("when_to_use_json", new TableInfo.Column("when_to_use_json", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataStructures.put("diagram_res", new TableInfo.Column("diagram_res", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataStructures.put("code_example", new TableInfo.Column("code_example", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataStructures.put("difficulty", new TableInfo.Column("difficulty", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDataStructures = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDataStructures = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDataStructures = new TableInfo("data_structures", _columnsDataStructures, _foreignKeysDataStructures, _indicesDataStructures);
        final TableInfo _existingDataStructures = TableInfo.read(db, "data_structures");
        if (!_infoDataStructures.equals(_existingDataStructures)) {
          return new RoomOpenHelper.ValidationResult(false, "data_structures(com.hackerrank.app.data.local.entity.DataStructureEntity).\n"
                  + " Expected:\n" + _infoDataStructures + "\n"
                  + " Found:\n" + _existingDataStructures);
        }
        final HashMap<String, TableInfo.Column> _columnsQuizQuestions = new HashMap<String, TableInfo.Column>(6);
        _columnsQuizQuestions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("structure_id", new TableInfo.Column("structure_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("question", new TableInfo.Column("question", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("options_json", new TableInfo.Column("options_json", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("correct_index", new TableInfo.Column("correct_index", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("explanation", new TableInfo.Column("explanation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuizQuestions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysQuizQuestions.add(new TableInfo.ForeignKey("data_structures", "CASCADE", "NO ACTION", Arrays.asList("structure_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesQuizQuestions = new HashSet<TableInfo.Index>(1);
        _indicesQuizQuestions.add(new TableInfo.Index("index_quiz_questions_structure_id", false, Arrays.asList("structure_id"), Arrays.asList("ASC")));
        final TableInfo _infoQuizQuestions = new TableInfo("quiz_questions", _columnsQuizQuestions, _foreignKeysQuizQuestions, _indicesQuizQuestions);
        final TableInfo _existingQuizQuestions = TableInfo.read(db, "quiz_questions");
        if (!_infoQuizQuestions.equals(_existingQuizQuestions)) {
          return new RoomOpenHelper.ValidationResult(false, "quiz_questions(com.hackerrank.app.data.local.entity.QuizQuestionEntity).\n"
                  + " Expected:\n" + _infoQuizQuestions + "\n"
                  + " Found:\n" + _existingQuizQuestions);
        }
        final HashMap<String, TableInfo.Column> _columnsUserProgress = new HashMap<String, TableInfo.Column>(6);
        _columnsUserProgress.put("structure_id", new TableInfo.Column("structure_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("quizzes_completed", new TableInfo.Column("quizzes_completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("total_correct", new TableInfo.Column("total_correct", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("total_questions", new TableInfo.Column("total_questions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("best_score", new TableInfo.Column("best_score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("mastery_level", new TableInfo.Column("mastery_level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProgress = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProgress = new TableInfo("user_progress", _columnsUserProgress, _foreignKeysUserProgress, _indicesUserProgress);
        final TableInfo _existingUserProgress = TableInfo.read(db, "user_progress");
        if (!_infoUserProgress.equals(_existingUserProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "user_progress(com.hackerrank.app.data.local.entity.UserProgressEntity).\n"
                  + " Expected:\n" + _infoUserProgress + "\n"
                  + " Found:\n" + _existingUserProgress);
        }
        final HashMap<String, TableInfo.Column> _columnsUserProfile = new HashMap<String, TableInfo.Column>(6);
        _columnsUserProfile.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("total_xp", new TableInfo.Column("total_xp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("current_streak", new TableInfo.Column("current_streak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("longest_streak", new TableInfo.Column("longest_streak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("last_active_date", new TableInfo.Column("last_active_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("earned_badge_ids_json", new TableInfo.Column("earned_badge_ids_json", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProfile = new TableInfo("user_profile", _columnsUserProfile, _foreignKeysUserProfile, _indicesUserProfile);
        final TableInfo _existingUserProfile = TableInfo.read(db, "user_profile");
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "user_profile(com.hackerrank.app.data.local.entity.UserProfileEntity).\n"
                  + " Expected:\n" + _infoUserProfile + "\n"
                  + " Found:\n" + _existingUserProfile);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a7eff95a8163ff7ff7af2b784bc03e11", "0b780c76624460c00a38e37142228830");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "data_structures","quiz_questions","user_progress","user_profile");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `data_structures`");
      _db.execSQL("DELETE FROM `quiz_questions`");
      _db.execSQL("DELETE FROM `user_progress`");
      _db.execSQL("DELETE FROM `user_profile`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DataStructureDao.class, DataStructureDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QuizQuestionDao.class, QuizQuestionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProgressDao.class, ProgressDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProfileDao.class, ProfileDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DataStructureDao dataStructureDao() {
    if (_dataStructureDao != null) {
      return _dataStructureDao;
    } else {
      synchronized(this) {
        if(_dataStructureDao == null) {
          _dataStructureDao = new DataStructureDao_Impl(this);
        }
        return _dataStructureDao;
      }
    }
  }

  @Override
  public QuizQuestionDao quizQuestionDao() {
    if (_quizQuestionDao != null) {
      return _quizQuestionDao;
    } else {
      synchronized(this) {
        if(_quizQuestionDao == null) {
          _quizQuestionDao = new QuizQuestionDao_Impl(this);
        }
        return _quizQuestionDao;
      }
    }
  }

  @Override
  public ProgressDao progressDao() {
    if (_progressDao != null) {
      return _progressDao;
    } else {
      synchronized(this) {
        if(_progressDao == null) {
          _progressDao = new ProgressDao_Impl(this);
        }
        return _progressDao;
      }
    }
  }

  @Override
  public ProfileDao profileDao() {
    if (_profileDao != null) {
      return _profileDao;
    } else {
      synchronized(this) {
        if(_profileDao == null) {
          _profileDao = new ProfileDao_Impl(this);
        }
        return _profileDao;
      }
    }
  }
}
