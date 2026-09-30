package com.fridgekeeper.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        ItemEntity::class,
        DigitalItemEntity::class,
        PetCategoryEntity::class,
        PetItemEntity::class,
        MapMarkerEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    abstract fun digitalItemDao(): DigitalItemDao

    abstract fun petCategoryDao(): PetCategoryDao

    abstract fun petItemDao(): PetItemDao

    abstract fun mapMarkerDao(): MapMarkerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `digital_items` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`name` TEXT NOT NULL, " +
                        "`purchasePrice` REAL NOT NULL, " +
                        "`purchaseDate` INTEGER NOT NULL, " +
                        "`sellPrice` REAL, " +
                        "`sellDate` INTEGER, " +
                        "`createdAt` INTEGER NOT NULL, " +
                        "`updatedAt` INTEGER NOT NULL)"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                createPetTables(db)
                seedDefaultPetCategories(db)
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `map_markers` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`itemType` TEXT NOT NULL, " +
                        "`itemId` INTEGER NOT NULL, " +
                        "`itemDisplayName` TEXT NOT NULL, " +
                        "`posX` REAL NOT NULL, " +
                        "`posY` REAL NOT NULL, " +
                        "`roomLabel` TEXT, " +
                        "`createdAt` INTEGER NOT NULL)"
                )
            }
        }

        private val SEED_CALLBACK = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                seedDefaultPetCategories(db)
            }
        }

        private fun createPetTables(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `pet_categories` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`name` TEXT NOT NULL, " +
                    "`isDefault` INTEGER NOT NULL, " +
                    "`createdAt` INTEGER NOT NULL)"
            )
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `pet_items` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`name` TEXT NOT NULL, " +
                    "`categoryId` INTEGER NOT NULL, " +
                    "`purchasePrice` REAL NOT NULL, " +
                    "`purchaseDate` INTEGER NOT NULL, " +
                    "`quantityType` TEXT NOT NULL, " +
                    "`weightUnit` TEXT, " +
                    "`quantity` REAL NOT NULL, " +
                    "`tags` TEXT NOT NULL, " +
                    "`createdAt` INTEGER NOT NULL, " +
                    "`updatedAt` INTEGER NOT NULL)"
            )
        }

        private fun seedDefaultPetCategories(db: SupportSQLiteDatabase) {
            val now = System.currentTimeMillis()
            listOf("猫粮", "猫砂", "猫玩具").forEach { name ->
                db.execSQL(
                    "INSERT INTO `pet_categories` (`name`, `isDefault`, `createdAt`) " +
                        "VALUES ('$name', 1, $now)"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fridge_keeper.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .addCallback(SEED_CALLBACK)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
