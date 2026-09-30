package com.fridgekeeper.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PetItemDao_Impl implements PetItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PetItemEntity> __insertionAdapterOfPetItemEntity;

  public PetItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPetItemEntity = new EntityInsertionAdapter<PetItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `pet_items` (`id`,`name`,`categoryId`,`purchasePrice`,`purchaseDate`,`quantityType`,`weightUnit`,`quantity`,`tags`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PetItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getCategoryId());
        statement.bindDouble(4, entity.getPurchasePrice());
        statement.bindLong(5, entity.getPurchaseDate());
        statement.bindString(6, entity.getQuantityType());
        if (entity.getWeightUnit() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getWeightUnit());
        }
        statement.bindDouble(8, entity.getQuantity());
        statement.bindString(9, entity.getTags());
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getUpdatedAt());
      }
    };
  }

  @Override
  public Object insert(final PetItemEntity item, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPetItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PetItemEntity>> observeAll() {
    final String _sql = "SELECT * FROM pet_items ORDER BY purchaseDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pet_items"}, new Callable<List<PetItemEntity>>() {
      @Override
      @NonNull
      public List<PetItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfPurchasePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "purchasePrice");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfQuantityType = CursorUtil.getColumnIndexOrThrow(_cursor, "quantityType");
          final int _cursorIndexOfWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "weightUnit");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<PetItemEntity> _result = new ArrayList<PetItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PetItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpCategoryId;
            _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            final double _tmpPurchasePrice;
            _tmpPurchasePrice = _cursor.getDouble(_cursorIndexOfPurchasePrice);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final String _tmpQuantityType;
            _tmpQuantityType = _cursor.getString(_cursorIndexOfQuantityType);
            final String _tmpWeightUnit;
            if (_cursor.isNull(_cursorIndexOfWeightUnit)) {
              _tmpWeightUnit = null;
            } else {
              _tmpWeightUnit = _cursor.getString(_cursorIndexOfWeightUnit);
            }
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new PetItemEntity(_tmpId,_tmpName,_tmpCategoryId,_tmpPurchasePrice,_tmpPurchaseDate,_tmpQuantityType,_tmpWeightUnit,_tmpQuantity,_tmpTags,_tmpCreatedAt,_tmpUpdatedAt);
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
