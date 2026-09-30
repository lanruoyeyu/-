package com.fridgekeeper.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
public final class MapMarkerDao_Impl implements MapMarkerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MapMarkerEntity> __insertionAdapterOfMapMarkerEntity;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public MapMarkerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMapMarkerEntity = new EntityInsertionAdapter<MapMarkerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `map_markers` (`id`,`itemType`,`itemId`,`itemDisplayName`,`posX`,`posY`,`roomLabel`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MapMarkerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getItemType());
        statement.bindLong(3, entity.getItemId());
        statement.bindString(4, entity.getItemDisplayName());
        statement.bindDouble(5, entity.getPosX());
        statement.bindDouble(6, entity.getPosY());
        if (entity.getRoomLabel() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getRoomLabel());
        }
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM map_markers WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MapMarkerEntity marker, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMapMarkerEntity.insertAndReturnId(marker);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MapMarkerEntity>> observeAll() {
    final String _sql = "SELECT * FROM map_markers ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"map_markers"}, new Callable<List<MapMarkerEntity>>() {
      @Override
      @NonNull
      public List<MapMarkerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfItemType = CursorUtil.getColumnIndexOrThrow(_cursor, "itemType");
          final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "itemId");
          final int _cursorIndexOfItemDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemDisplayName");
          final int _cursorIndexOfPosX = CursorUtil.getColumnIndexOrThrow(_cursor, "posX");
          final int _cursorIndexOfPosY = CursorUtil.getColumnIndexOrThrow(_cursor, "posY");
          final int _cursorIndexOfRoomLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "roomLabel");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MapMarkerEntity> _result = new ArrayList<MapMarkerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MapMarkerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpItemType;
            _tmpItemType = _cursor.getString(_cursorIndexOfItemType);
            final long _tmpItemId;
            _tmpItemId = _cursor.getLong(_cursorIndexOfItemId);
            final String _tmpItemDisplayName;
            _tmpItemDisplayName = _cursor.getString(_cursorIndexOfItemDisplayName);
            final float _tmpPosX;
            _tmpPosX = _cursor.getFloat(_cursorIndexOfPosX);
            final float _tmpPosY;
            _tmpPosY = _cursor.getFloat(_cursorIndexOfPosY);
            final String _tmpRoomLabel;
            if (_cursor.isNull(_cursorIndexOfRoomLabel)) {
              _tmpRoomLabel = null;
            } else {
              _tmpRoomLabel = _cursor.getString(_cursorIndexOfRoomLabel);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MapMarkerEntity(_tmpId,_tmpItemType,_tmpItemId,_tmpItemDisplayName,_tmpPosX,_tmpPosY,_tmpRoomLabel,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
