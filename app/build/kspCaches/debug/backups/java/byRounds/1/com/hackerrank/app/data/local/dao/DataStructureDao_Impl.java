package com.hackerrank.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hackerrank.app.data.local.entity.DataStructureEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DataStructureDao_Impl implements DataStructureDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DataStructureEntity> __insertionAdapterOfDataStructureEntity;

  public DataStructureDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDataStructureEntity = new EntityInsertionAdapter<DataStructureEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `data_structures` (`id`,`name`,`slug`,`category`,`explanation`,`complexity_json`,`when_to_use_json`,`diagram_res`,`code_example`,`difficulty`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DataStructureEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSlug());
        statement.bindString(4, entity.getCategory());
        statement.bindString(5, entity.getExplanation());
        statement.bindString(6, entity.getComplexityJson());
        statement.bindString(7, entity.getWhenToUseJson());
        if (entity.getDiagramRes() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDiagramRes());
        }
        statement.bindString(9, entity.getCodeExample());
        statement.bindString(10, entity.getDifficulty());
      }
    };
  }

  @Override
  public Object insertAll(final List<DataStructureEntity> structures,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDataStructureEntity.insert(structures);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DataStructureEntity>> getAllStructures() {
    final String _sql = "SELECT * FROM data_structures ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"data_structures"}, new Callable<List<DataStructureEntity>>() {
      @Override
      @NonNull
      public List<DataStructureEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSlug = CursorUtil.getColumnIndexOrThrow(_cursor, "slug");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfComplexityJson = CursorUtil.getColumnIndexOrThrow(_cursor, "complexity_json");
          final int _cursorIndexOfWhenToUseJson = CursorUtil.getColumnIndexOrThrow(_cursor, "when_to_use_json");
          final int _cursorIndexOfDiagramRes = CursorUtil.getColumnIndexOrThrow(_cursor, "diagram_res");
          final int _cursorIndexOfCodeExample = CursorUtil.getColumnIndexOrThrow(_cursor, "code_example");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final List<DataStructureEntity> _result = new ArrayList<DataStructureEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DataStructureEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSlug;
            _tmpSlug = _cursor.getString(_cursorIndexOfSlug);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpComplexityJson;
            _tmpComplexityJson = _cursor.getString(_cursorIndexOfComplexityJson);
            final String _tmpWhenToUseJson;
            _tmpWhenToUseJson = _cursor.getString(_cursorIndexOfWhenToUseJson);
            final String _tmpDiagramRes;
            if (_cursor.isNull(_cursorIndexOfDiagramRes)) {
              _tmpDiagramRes = null;
            } else {
              _tmpDiagramRes = _cursor.getString(_cursorIndexOfDiagramRes);
            }
            final String _tmpCodeExample;
            _tmpCodeExample = _cursor.getString(_cursorIndexOfCodeExample);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            _item = new DataStructureEntity(_tmpId,_tmpName,_tmpSlug,_tmpCategory,_tmpExplanation,_tmpComplexityJson,_tmpWhenToUseJson,_tmpDiagramRes,_tmpCodeExample,_tmpDifficulty);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<DataStructureEntity>> getStructuresByCategory(final String category) {
    final String _sql = "SELECT * FROM data_structures WHERE category = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"data_structures"}, new Callable<List<DataStructureEntity>>() {
      @Override
      @NonNull
      public List<DataStructureEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSlug = CursorUtil.getColumnIndexOrThrow(_cursor, "slug");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfComplexityJson = CursorUtil.getColumnIndexOrThrow(_cursor, "complexity_json");
          final int _cursorIndexOfWhenToUseJson = CursorUtil.getColumnIndexOrThrow(_cursor, "when_to_use_json");
          final int _cursorIndexOfDiagramRes = CursorUtil.getColumnIndexOrThrow(_cursor, "diagram_res");
          final int _cursorIndexOfCodeExample = CursorUtil.getColumnIndexOrThrow(_cursor, "code_example");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final List<DataStructureEntity> _result = new ArrayList<DataStructureEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DataStructureEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSlug;
            _tmpSlug = _cursor.getString(_cursorIndexOfSlug);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpComplexityJson;
            _tmpComplexityJson = _cursor.getString(_cursorIndexOfComplexityJson);
            final String _tmpWhenToUseJson;
            _tmpWhenToUseJson = _cursor.getString(_cursorIndexOfWhenToUseJson);
            final String _tmpDiagramRes;
            if (_cursor.isNull(_cursorIndexOfDiagramRes)) {
              _tmpDiagramRes = null;
            } else {
              _tmpDiagramRes = _cursor.getString(_cursorIndexOfDiagramRes);
            }
            final String _tmpCodeExample;
            _tmpCodeExample = _cursor.getString(_cursorIndexOfCodeExample);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            _item = new DataStructureEntity(_tmpId,_tmpName,_tmpSlug,_tmpCategory,_tmpExplanation,_tmpComplexityJson,_tmpWhenToUseJson,_tmpDiagramRes,_tmpCodeExample,_tmpDifficulty);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getStructureBySlug(final String slug,
      final Continuation<? super DataStructureEntity> $completion) {
    final String _sql = "SELECT * FROM data_structures WHERE slug = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, slug);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DataStructureEntity>() {
      @Override
      @Nullable
      public DataStructureEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSlug = CursorUtil.getColumnIndexOrThrow(_cursor, "slug");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfComplexityJson = CursorUtil.getColumnIndexOrThrow(_cursor, "complexity_json");
          final int _cursorIndexOfWhenToUseJson = CursorUtil.getColumnIndexOrThrow(_cursor, "when_to_use_json");
          final int _cursorIndexOfDiagramRes = CursorUtil.getColumnIndexOrThrow(_cursor, "diagram_res");
          final int _cursorIndexOfCodeExample = CursorUtil.getColumnIndexOrThrow(_cursor, "code_example");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final DataStructureEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSlug;
            _tmpSlug = _cursor.getString(_cursorIndexOfSlug);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpComplexityJson;
            _tmpComplexityJson = _cursor.getString(_cursorIndexOfComplexityJson);
            final String _tmpWhenToUseJson;
            _tmpWhenToUseJson = _cursor.getString(_cursorIndexOfWhenToUseJson);
            final String _tmpDiagramRes;
            if (_cursor.isNull(_cursorIndexOfDiagramRes)) {
              _tmpDiagramRes = null;
            } else {
              _tmpDiagramRes = _cursor.getString(_cursorIndexOfDiagramRes);
            }
            final String _tmpCodeExample;
            _tmpCodeExample = _cursor.getString(_cursorIndexOfCodeExample);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            _result = new DataStructureEntity(_tmpId,_tmpName,_tmpSlug,_tmpCategory,_tmpExplanation,_tmpComplexityJson,_tmpWhenToUseJson,_tmpDiagramRes,_tmpCodeExample,_tmpDifficulty);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getStructureById(final String id,
      final Continuation<? super DataStructureEntity> $completion) {
    final String _sql = "SELECT * FROM data_structures WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DataStructureEntity>() {
      @Override
      @Nullable
      public DataStructureEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSlug = CursorUtil.getColumnIndexOrThrow(_cursor, "slug");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfComplexityJson = CursorUtil.getColumnIndexOrThrow(_cursor, "complexity_json");
          final int _cursorIndexOfWhenToUseJson = CursorUtil.getColumnIndexOrThrow(_cursor, "when_to_use_json");
          final int _cursorIndexOfDiagramRes = CursorUtil.getColumnIndexOrThrow(_cursor, "diagram_res");
          final int _cursorIndexOfCodeExample = CursorUtil.getColumnIndexOrThrow(_cursor, "code_example");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final DataStructureEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSlug;
            _tmpSlug = _cursor.getString(_cursorIndexOfSlug);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpComplexityJson;
            _tmpComplexityJson = _cursor.getString(_cursorIndexOfComplexityJson);
            final String _tmpWhenToUseJson;
            _tmpWhenToUseJson = _cursor.getString(_cursorIndexOfWhenToUseJson);
            final String _tmpDiagramRes;
            if (_cursor.isNull(_cursorIndexOfDiagramRes)) {
              _tmpDiagramRes = null;
            } else {
              _tmpDiagramRes = _cursor.getString(_cursorIndexOfDiagramRes);
            }
            final String _tmpCodeExample;
            _tmpCodeExample = _cursor.getString(_cursorIndexOfCodeExample);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            _result = new DataStructureEntity(_tmpId,_tmpName,_tmpSlug,_tmpCategory,_tmpExplanation,_tmpComplexityJson,_tmpWhenToUseJson,_tmpDiagramRes,_tmpCodeExample,_tmpDifficulty);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<String>> getAllCategories() {
    final String _sql = "SELECT DISTINCT category FROM data_structures ORDER BY CASE WHEN category = 'Linear' THEN 1 WHEN category = 'Trees' THEN 2 WHEN category = 'Graphs' THEN 3 WHEN category = 'Hash-Based' THEN 4 WHEN category = 'Other' THEN 5 END";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"data_structures"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM data_structures";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
