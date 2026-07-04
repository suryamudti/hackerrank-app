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
import com.hackerrank.app.data.local.entity.QuizQuestionEntity;
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
public final class QuizQuestionDao_Impl implements QuizQuestionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<QuizQuestionEntity> __insertionAdapterOfQuizQuestionEntity;

  public QuizQuestionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuizQuestionEntity = new EntityInsertionAdapter<QuizQuestionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `quiz_questions` (`id`,`structure_id`,`question`,`options_json`,`correct_index`,`explanation`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuizQuestionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getStructureId());
        statement.bindString(3, entity.getQuestion());
        statement.bindString(4, entity.getOptionsJson());
        statement.bindLong(5, entity.getCorrectIndex());
        statement.bindString(6, entity.getExplanation());
      }
    };
  }

  @Override
  public Object insertAll(final List<QuizQuestionEntity> questions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuizQuestionEntity.insert(questions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<QuizQuestionEntity>> getQuestionsByStructureId(final String structureId) {
    final String _sql = "SELECT * FROM quiz_questions WHERE structure_id = ? ORDER BY RANDOM()";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, structureId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quiz_questions"}, new Callable<List<QuizQuestionEntity>>() {
      @Override
      @NonNull
      public List<QuizQuestionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStructureId = CursorUtil.getColumnIndexOrThrow(_cursor, "structure_id");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfOptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "options_json");
          final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correct_index");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final List<QuizQuestionEntity> _result = new ArrayList<QuizQuestionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuizQuestionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStructureId;
            _tmpStructureId = _cursor.getString(_cursorIndexOfStructureId);
            final String _tmpQuestion;
            _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            final String _tmpOptionsJson;
            _tmpOptionsJson = _cursor.getString(_cursorIndexOfOptionsJson);
            final int _tmpCorrectIndex;
            _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            _item = new QuizQuestionEntity(_tmpId,_tmpStructureId,_tmpQuestion,_tmpOptionsJson,_tmpCorrectIndex,_tmpExplanation);
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
  public Object getQuestionById(final String id,
      final Continuation<? super QuizQuestionEntity> $completion) {
    final String _sql = "SELECT * FROM quiz_questions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<QuizQuestionEntity>() {
      @Override
      @Nullable
      public QuizQuestionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStructureId = CursorUtil.getColumnIndexOrThrow(_cursor, "structure_id");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfOptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "options_json");
          final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correct_index");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final QuizQuestionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStructureId;
            _tmpStructureId = _cursor.getString(_cursorIndexOfStructureId);
            final String _tmpQuestion;
            _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            final String _tmpOptionsJson;
            _tmpOptionsJson = _cursor.getString(_cursorIndexOfOptionsJson);
            final int _tmpCorrectIndex;
            _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            _result = new QuizQuestionEntity(_tmpId,_tmpStructureId,_tmpQuestion,_tmpOptionsJson,_tmpCorrectIndex,_tmpExplanation);
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
  public Object countByStructureId(final String structureId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM quiz_questions WHERE structure_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, structureId);
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
