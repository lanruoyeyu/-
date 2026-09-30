package com.fridgekeeper.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 宠物用品种类（预置猫粮/猫砂/猫玩具，可由用户新增）
 */
@Entity(tableName = "pet_categories")
data class PetCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
