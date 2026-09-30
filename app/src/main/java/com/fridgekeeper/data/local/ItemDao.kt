package com.fridgekeeper.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items WHERE isCleared = 0 ORDER BY expirationDate ASC")
    fun observeActiveItems(): Flow<List<ItemEntity>>

    @Query("SELECT category, SUM(quantity) as count FROM items WHERE isCleared = 0 GROUP BY category")
    fun observeCategoryCounts(): Flow<List<CategoryCount>>

    @Query("SELECT SUM(quantity) FROM items WHERE isCleared = 0")
    fun observeTotalCount(): Flow<Int?>

    @Insert
    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: ItemEntity)

    @Query("UPDATE items SET quantity = :quantity, updatedAt = :timestamp WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int, timestamp: Long)

    @Query("UPDATE items SET isCleared = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun markCleared(id: Long, timestamp: Long)
}
