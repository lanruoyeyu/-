package com.fridgekeeper.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PetCategoryDao {

    @Query("SELECT * FROM pet_categories ORDER BY CASE WHEN isDefault = 1 THEN 0 ELSE 1 END ASC, id ASC")
    fun observeAll(): Flow<List<PetCategoryEntity>>

    @Query("SELECT COUNT(*) FROM pet_categories WHERE name = :name")
    suspend fun countByName(name: String): Int

    @Insert
    suspend fun insert(category: PetCategoryEntity): Long
}
