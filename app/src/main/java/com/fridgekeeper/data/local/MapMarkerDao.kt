package com.fridgekeeper.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MapMarkerDao {

    @Query("SELECT * FROM map_markers ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<MapMarkerEntity>>

    @Insert
    suspend fun insert(marker: MapMarkerEntity): Long

    @Query("DELETE FROM map_markers WHERE id = :id")
    suspend fun delete(id: Long)
}
