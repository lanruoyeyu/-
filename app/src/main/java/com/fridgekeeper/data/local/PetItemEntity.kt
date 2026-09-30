package com.fridgekeeper.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 宠物用品实体
 * quantityType：WEIGHT（重量）/ COUNT（个数）
 * weightUnit：G（克）/ KG（千克），仅重量类型有效
 * tags：标签，英文逗号分隔（如品牌）
 */
@Entity(tableName = "pet_items")
data class PetItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val categoryId: Long,
    val purchasePrice: Double,
    val purchaseDate: Long,
    val quantityType: String,
    val weightUnit: String? = null,
    val quantity: Double,
    val tags: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
