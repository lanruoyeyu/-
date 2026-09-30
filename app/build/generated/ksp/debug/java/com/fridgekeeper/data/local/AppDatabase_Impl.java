package com.fridgekeeper.data.local;

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
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ItemDao _itemDao;

  private volatile DigitalItemDao _digitalItemDao;

  private volatile PetCategoryDao _petCategoryDao;

  private volatile PetItemDao _petItemDao;

  private volatile MapMarkerDao _mapMarkerDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `category` TEXT NOT NULL, `quantity` INTEGER NOT NULL, `storageDate` INTEGER NOT NULL, `expirationDate` INTEGER NOT NULL, `isCleared` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `digital_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `purchasePrice` REAL NOT NULL, `purchaseDate` INTEGER NOT NULL, `sellPrice` REAL, `sellDate` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pet_categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `isDefault` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pet_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `categoryId` INTEGER NOT NULL, `purchasePrice` REAL NOT NULL, `purchaseDate` INTEGER NOT NULL, `quantityType` TEXT NOT NULL, `weightUnit` TEXT, `quantity` REAL NOT NULL, `tags` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `map_markers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `itemType` TEXT NOT NULL, `itemId` INTEGER NOT NULL, `itemDisplayName` TEXT NOT NULL, `posX` REAL NOT NULL, `posY` REAL NOT NULL, `roomLabel` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f4913c97b345fed5eab7b8d9a11292f0')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `items`");
        db.execSQL("DROP TABLE IF EXISTS `digital_items`");
        db.execSQL("DROP TABLE IF EXISTS `pet_categories`");
        db.execSQL("DROP TABLE IF EXISTS `pet_items`");
        db.execSQL("DROP TABLE IF EXISTS `map_markers`");
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
        final HashMap<String, TableInfo.Column> _columnsItems = new HashMap<String, TableInfo.Column>(9);
        _columnsItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("storageDate", new TableInfo.Column("storageDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("expirationDate", new TableInfo.Column("expirationDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("isCleared", new TableInfo.Column("isCleared", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItems.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoItems = new TableInfo("items", _columnsItems, _foreignKeysItems, _indicesItems);
        final TableInfo _existingItems = TableInfo.read(db, "items");
        if (!_infoItems.equals(_existingItems)) {
          return new RoomOpenHelper.ValidationResult(false, "items(com.fridgekeeper.data.local.ItemEntity).\n"
                  + " Expected:\n" + _infoItems + "\n"
                  + " Found:\n" + _existingItems);
        }
        final HashMap<String, TableInfo.Column> _columnsDigitalItems = new HashMap<String, TableInfo.Column>(8);
        _columnsDigitalItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDigitalItems.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDigitalItems.put("purchasePrice", new TableInfo.Column("purchasePrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDigitalItems.put("purchaseDate", new TableInfo.Column("purchaseDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDigitalItems.put("sellPrice", new TableInfo.Column("sellPrice", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDigitalItems.put("sellDate", new TableInfo.Column("sellDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDigitalItems.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDigitalItems.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDigitalItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDigitalItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDigitalItems = new TableInfo("digital_items", _columnsDigitalItems, _foreignKeysDigitalItems, _indicesDigitalItems);
        final TableInfo _existingDigitalItems = TableInfo.read(db, "digital_items");
        if (!_infoDigitalItems.equals(_existingDigitalItems)) {
          return new RoomOpenHelper.ValidationResult(false, "digital_items(com.fridgekeeper.data.local.DigitalItemEntity).\n"
                  + " Expected:\n" + _infoDigitalItems + "\n"
                  + " Found:\n" + _existingDigitalItems);
        }
        final HashMap<String, TableInfo.Column> _columnsPetCategories = new HashMap<String, TableInfo.Column>(4);
        _columnsPetCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetCategories.put("isDefault", new TableInfo.Column("isDefault", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetCategories.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPetCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPetCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPetCategories = new TableInfo("pet_categories", _columnsPetCategories, _foreignKeysPetCategories, _indicesPetCategories);
        final TableInfo _existingPetCategories = TableInfo.read(db, "pet_categories");
        if (!_infoPetCategories.equals(_existingPetCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "pet_categories(com.fridgekeeper.data.local.PetCategoryEntity).\n"
                  + " Expected:\n" + _infoPetCategories + "\n"
                  + " Found:\n" + _existingPetCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsPetItems = new HashMap<String, TableInfo.Column>(11);
        _columnsPetItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("purchasePrice", new TableInfo.Column("purchasePrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("purchaseDate", new TableInfo.Column("purchaseDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("quantityType", new TableInfo.Column("quantityType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("weightUnit", new TableInfo.Column("weightUnit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("quantity", new TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPetItems.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPetItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPetItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPetItems = new TableInfo("pet_items", _columnsPetItems, _foreignKeysPetItems, _indicesPetItems);
        final TableInfo _existingPetItems = TableInfo.read(db, "pet_items");
        if (!_infoPetItems.equals(_existingPetItems)) {
          return new RoomOpenHelper.ValidationResult(false, "pet_items(com.fridgekeeper.data.local.PetItemEntity).\n"
                  + " Expected:\n" + _infoPetItems + "\n"
                  + " Found:\n" + _existingPetItems);
        }
        final HashMap<String, TableInfo.Column> _columnsMapMarkers = new HashMap<String, TableInfo.Column>(8);
        _columnsMapMarkers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMapMarkers.put("itemType", new TableInfo.Column("itemType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMapMarkers.put("itemId", new TableInfo.Column("itemId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMapMarkers.put("itemDisplayName", new TableInfo.Column("itemDisplayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMapMarkers.put("posX", new TableInfo.Column("posX", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMapMarkers.put("posY", new TableInfo.Column("posY", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMapMarkers.put("roomLabel", new TableInfo.Column("roomLabel", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMapMarkers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMapMarkers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMapMarkers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMapMarkers = new TableInfo("map_markers", _columnsMapMarkers, _foreignKeysMapMarkers, _indicesMapMarkers);
        final TableInfo _existingMapMarkers = TableInfo.read(db, "map_markers");
        if (!_infoMapMarkers.equals(_existingMapMarkers)) {
          return new RoomOpenHelper.ValidationResult(false, "map_markers(com.fridgekeeper.data.local.MapMarkerEntity).\n"
                  + " Expected:\n" + _infoMapMarkers + "\n"
                  + " Found:\n" + _existingMapMarkers);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f4913c97b345fed5eab7b8d9a11292f0", "a602213405cc7f0f6cfc0f602c2f77a7");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "items","digital_items","pet_categories","pet_items","map_markers");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `items`");
      _db.execSQL("DELETE FROM `digital_items`");
      _db.execSQL("DELETE FROM `pet_categories`");
      _db.execSQL("DELETE FROM `pet_items`");
      _db.execSQL("DELETE FROM `map_markers`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
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
    _typeConvertersMap.put(ItemDao.class, ItemDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DigitalItemDao.class, DigitalItemDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PetCategoryDao.class, PetCategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PetItemDao.class, PetItemDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MapMarkerDao.class, MapMarkerDao_Impl.getRequiredConverters());
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
  public ItemDao itemDao() {
    if (_itemDao != null) {
      return _itemDao;
    } else {
      synchronized(this) {
        if(_itemDao == null) {
          _itemDao = new ItemDao_Impl(this);
        }
        return _itemDao;
      }
    }
  }

  @Override
  public DigitalItemDao digitalItemDao() {
    if (_digitalItemDao != null) {
      return _digitalItemDao;
    } else {
      synchronized(this) {
        if(_digitalItemDao == null) {
          _digitalItemDao = new DigitalItemDao_Impl(this);
        }
        return _digitalItemDao;
      }
    }
  }

  @Override
  public PetCategoryDao petCategoryDao() {
    if (_petCategoryDao != null) {
      return _petCategoryDao;
    } else {
      synchronized(this) {
        if(_petCategoryDao == null) {
          _petCategoryDao = new PetCategoryDao_Impl(this);
        }
        return _petCategoryDao;
      }
    }
  }

  @Override
  public PetItemDao petItemDao() {
    if (_petItemDao != null) {
      return _petItemDao;
    } else {
      synchronized(this) {
        if(_petItemDao == null) {
          _petItemDao = new PetItemDao_Impl(this);
        }
        return _petItemDao;
      }
    }
  }

  @Override
  public MapMarkerDao mapMarkerDao() {
    if (_mapMarkerDao != null) {
      return _mapMarkerDao;
    } else {
      synchronized(this) {
        if(_mapMarkerDao == null) {
          _mapMarkerDao = new MapMarkerDao_Impl(this);
        }
        return _mapMarkerDao;
      }
    }
  }
}
