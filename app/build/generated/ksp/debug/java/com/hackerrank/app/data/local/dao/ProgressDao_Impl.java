package com.hackerrank.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hackerrank.app.data.local.entity.UserProgressEntity;
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
public final class ProgressDao_Impl implements ProgressDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserProgressEntity> __insertionAdapterOfUserProgressEntity;

  public ProgressDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserProgressEntity = new EntityInsertionAdapter<UserProgressEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_progress` (`structure_id`,`quizzes_completed`,`total_correct`,`total_questions`,`best_score`,`mastery_level`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProgressEntity entity) {
        statement.bindString(1, entity.getStructureId());
        statement.bindLong(2, entity.getQuizzesCompleted());
        statement.bindLong(3, entity.getTotalCorrect());
        statement.bindLong(4, entity.getTotalQuestions());
        statement.bindLong(5, entity.getBestScore());
        statement.bindLong(6, entity.getMasteryLevel());
      }
    };
  }

  @Override
  public Object upsert(final UserProgressEntity progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserProgressEntity.insert(progress);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserProgressEntity> getProgressByStructureId(final String structureId) {
    final String _sql = "SELECT * FROM user_progress WHERE structure_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, structureId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_progress"}, new Callable<UserProgressEntity>() {
      @Override
      @Nullable
      public UserProgressEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfStructureId = CursorUtil.getColumnIndexOrThrow(_cursor, "structure_id");
          final int _cursorIndexOfQuizzesCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "quizzes_completed");
          final int _cursorIndexOfTotalCorrect = CursorUtil.getColumnIndexOrThrow(_cursor, "total_correct");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "total_questions");
          final int _cursorIndexOfBestScore = CursorUtil.getColumnIndexOrThrow(_cursor, "best_score");
          final int _cursorIndexOfMasteryLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "mastery_level");
          final UserProgressEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpStructureId;
            _tmpStructureId = _cursor.getString(_cursorIndexOfStructureId);
            final int _tmpQuizzesCompleted;
            _tmpQuizzesCompleted = _cursor.getInt(_cursorIndexOfQuizzesCompleted);
            final int _tmpTotalCorrect;
            _tmpTotalCorrect = _cursor.getInt(_cursorIndexOfTotalCorrect);
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            final int _tmpBestScore;
            _tmpBestScore = _cursor.getInt(_cursorIndexOfBestScore);
            final int _tmpMasteryLevel;
            _tmpMasteryLevel = _cursor.getInt(_cursorIndexOfMasteryLevel);
            _result = new UserProgressEntity(_tmpStructureId,_tmpQuizzesCompleted,_tmpTotalCorrect,_tmpTotalQuestions,_tmpBestScore,_tmpMasteryLevel);
          } else {
            _result = null;
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
  public Flow<List<UserProgressEntity>> getAllProgress() {
    final String _sql = "SELECT * FROM user_progress";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_progress"}, new Callable<List<UserProgressEntity>>() {
      @Override
      @NonNull
      public List<UserProgressEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfStructureId = CursorUtil.getColumnIndexOrThrow(_cursor, "structure_id");
          final int _cursorIndexOfQuizzesCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "quizzes_completed");
          final int _cursorIndexOfTotalCorrect = CursorUtil.getColumnIndexOrThrow(_cursor, "total_correct");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "total_questions");
          final int _cursorIndexOfBestScore = CursorUtil.getColumnIndexOrThrow(_cursor, "best_score");
          final int _cursorIndexOfMasteryLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "mastery_level");
          final List<UserProgressEntity> _result = new ArrayList<UserProgressEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserProgressEntity _item;
            final String _tmpStructureId;
            _tmpStructureId = _cursor.getString(_cursorIndexOfStructureId);
            final int _tmpQuizzesCompleted;
            _tmpQuizzesCompleted = _cursor.getInt(_cursorIndexOfQuizzesCompleted);
            final int _tmpTotalCorrect;
            _tmpTotalCorrect = _cursor.getInt(_cursorIndexOfTotalCorrect);
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            final int _tmpBestScore;
            _tmpBestScore = _cursor.getInt(_cursorIndexOfBestScore);
            final int _tmpMasteryLevel;
            _tmpMasteryLevel = _cursor.getInt(_cursorIndexOfMasteryLevel);
            _item = new UserProgressEntity(_tmpStructureId,_tmpQuizzesCompleted,_tmpTotalCorrect,_tmpTotalQuestions,_tmpBestScore,_tmpMasteryLevel);
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
  public Flow<Integer> getMasteredCount() {
    final String _sql = "SELECT COUNT(*) FROM user_progress WHERE mastery_level >= 100";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_progress"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
