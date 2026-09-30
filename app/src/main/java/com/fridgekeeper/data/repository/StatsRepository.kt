package com.fridgekeeper.data.repository

import com.fridgekeeper.data.local.ItemDao
import com.fridgekeeper.data.local.DigitalItemDao
import com.fridgekeeper.data.local.PetCategoryDao
import com.fridgekeeper.data.local.PetItemDao
import com.fridgekeeper.data.local.MapMarkerDao
import com.fridgekeeper.data.local.MapMarkerEntity
import kotlinx.coroutines.flow.Flow

class StatsRepository(
    private val itemDao: ItemDao,
    private val digitalItemDao: DigitalItemDao,
    private val petCategoryDao: PetCategoryDao,
    private val petItemDao: PetItemDao,
    private val mapMarkerDao: MapMarkerDao
) {
    fun observeFridgeItems() = itemDao.observeActiveItems()
    fun observeDigitalItems() = digitalItemDao.observeAllItems()
    fun observePetItems() = petItemDao.observeAll()
    fun observePetCategories() = petCategoryDao.observeAll()
    fun observeMarkers(): Flow<List<MapMarkerEntity>> = mapMarkerDao.observeAll()

    suspend fun addMarker(marker: MapMarkerEntity): Long = mapMarkerDao.insert(marker)

    suspend fun deleteMarker(id: Long) = mapMarkerDao.delete(id)
}
