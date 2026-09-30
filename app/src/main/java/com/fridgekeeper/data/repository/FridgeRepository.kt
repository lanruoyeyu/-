package com.fridgekeeper.data.repository

import com.fridgekeeper.data.local.AppDatabase
import com.fridgekeeper.data.local.CategoryCount
import com.fridgekeeper.data.local.ItemDao
import com.fridgekeeper.data.local.ItemEntity
import kotlinx.coroutines.flow.Flow

class FridgeRepository(private val itemDao: ItemDao) {

    fun observeActiveItems(): Flow<List<ItemEntity>> = itemDao.observeActiveItems()

    fun observeCategoryCounts(): Flow<List<CategoryCount>> = itemDao.observeCategoryCounts()

    fun observeTotalCount(): Flow<Int?> = itemDao.observeTotalCount()

    suspend fun addItem(item: ItemEntity): Long = itemDao.insert(item)

    suspend fun updateQuantity(id: Long, quantity: Int) {
        itemDao.updateQuantity(id, quantity, System.currentTimeMillis())
    }

    suspend fun clearItem(id: Long) {
        itemDao.markCleared(id, System.currentTimeMillis())
    }

    companion object {
        @Volatile
        private var INSTANCE: FridgeRepository? = null

        fun getRepository(database: AppDatabase): FridgeRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = FridgeRepository(database.itemDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
