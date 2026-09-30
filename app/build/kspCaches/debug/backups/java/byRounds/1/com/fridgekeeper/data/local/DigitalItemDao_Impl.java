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
import java.lang.Double;
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
public final class DigitalItemDao_Impl implements DigitalItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DigitalItemEntity> __insertionAdapterOfDigitalItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkSold;

  public DigitalItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDigitalItemEntity = new EntityInsertionAdapter<DigitalItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `digital_items` (`id`,`name`,`purchasePrice`,`purchaseDate`,`sellPrice`,`sellDate`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DigitalItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getPurchasePrice());
        statement.bindLong(4, entity.getPurchaseDate());
        if (entity.getSellPrice() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getSellPrice());
        }
        if (entity.getSellDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getSellDate());
        }
        statement.bindLong(7, entity.getCreatedAt());
        statement.bindLong(8, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfMarkSold = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE digital_items SET sellPrice = ?, sellDate = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final DigitalItemEntity item, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDigitalItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markSold(final long id, final double sellPrice, final long sellDate,
      final long timestamp, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSold.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, sellPrice);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, sellDate);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 4;
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
          __preparedStmtOfMarkSold.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DigitalItemEntity>> observeAllItems() {
    final String _sql = "SELECT * FROM digital_items ORDER BY CASE WHEN sellDate IS NULL THEN 0 ELSE 1 END ASC, purchaseDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"digital_items"}, new Callable<List<DigitalItemEntity>>() {
      @Override
      @NonNull
      public List<DigitalItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPurchasePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "purchasePrice");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfSellPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "sellPrice");
          final int _cursorIndexOfSellDate = CursorUtil.getColumnIndexOrThrow(_cursor, "sellDate");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<DigitalItemEntity> _result = new ArrayList<DigitalItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DigitalItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpPurchasePrice;
            _tmpPurchasePrice = _cursor.getDouble(_cursorIndexOfPurchasePrice);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final Double _tmpSellPrice;
            if (_cursor.isNull(_cursorIndexOfSellPrice)) {
              _tmpSellPrice = null;
            } else {
              _tmpSellPrice = _cursor.getDouble(_cursorIndexOfSellPrice);
            }
            final Long _tmpSellDate;
            if (_cursor.isNull(_cursorIndexOfSellDate)) {
              _tmpSellDate = null;
            } else {
              _tmpSellDate = _cursor.getLong(_cursorIndexOfSellDate);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new DigitalItemEntity(_tmpId,_tmpName,_tmpPurchasePrice,_tmpPurchaseDate,_tmpSellPrice,_tmpSellDate,_tmpCreatedAt,_tmpUpdatedAt);
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
