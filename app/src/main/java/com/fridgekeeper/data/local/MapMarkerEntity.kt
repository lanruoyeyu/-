package com.fridgekeeper.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 地图标记实体：记录物品在房间地图上的存放位置
 */
@Entity(tableName = "map_markers")
data class MapMarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemType: String,
    val itemId: Long,
    val itemDisplayName: String,
    val posX: Float,
    val posY: Float,
    val roomLabel: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
