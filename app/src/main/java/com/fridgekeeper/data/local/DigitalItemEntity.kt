package com.fridgekeeper.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 数码产品实体
 * 卖出价格/卖出时间为空表示尚未卖出
 */
@Entity(tableName = "digital_items")
data class DigitalItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val purchasePrice: Double,
    val purchaseDate: Long,
    val sellPrice: Double? = null,
    val sellDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
