package com.fridgekeeper.data.repository

import com.fridgekeeper.data.local.PetCategoryDao
import com.fridgekeeper.data.local.PetCategoryEntity
import com.fridgekeeper.data.local.PetItemDao
import com.fridgekeeper.data.local.PetItemEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(
    private val categoryDao: PetCategoryDao,
    private val itemDao: PetItemDao
) {
    fun observeCategories(): Flow<List<PetCategoryEntity>> = categoryDao.observeAll()

    fun observeItems(): Flow<List<PetItemEntity>> = itemDao.observeAll()

    /**
     * 新增种类，名称去重。返回新分类 id；若已存在返回 -1
     */
    suspend fun addCategory(name: String, isDefault: Boolean = false): Long {
        val trimmed = name.trim()
        if (trimmed.isEmpty() || categoryDao.countByName(trimmed) > 0) return -1L
        return categoryDao.insert(
            PetCategoryEntity(name = trimmed, isDefault = isDefault)
        )
    }

    suspend fun addItem(item: PetItemEntity): Long = itemDao.insert(item)
}
