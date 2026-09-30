package com.fridgekeeper.data.repository

import com.fridgekeeper.data.local.DigitalItemDao
import com.fridgekeeper.data.local.DigitalItemEntity
import kotlinx.coroutines.flow.Flow

class DigitalRepository(private val digitalItemDao: DigitalItemDao) {

    fun observeAllItems(): Flow<List<DigitalItemEntity>> = digitalItemDao.observeAllItems()

    suspend fun addItem(item: DigitalItemEntity): Long = digitalItemDao.insert(item)

    suspend fun markSold(id: Long, sellPrice: Double, sellDate: Long, timestamp: Long) {
        digitalItemDao.markSold(id, sellPrice, sellDate, timestamp)
    }
}
