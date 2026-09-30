package com.fridgekeeper.model

import com.fridgekeeper.data.local.MapMarkerEntity

data class MapMarkerUiState(
    val id: Long,
    val itemType: MarkerItemType,
    val itemId: Long,
    val itemDisplayName: String,
    val posX: Float,
    val posY: Float,
    val roomLabel: String?
) {
    companion object {
        fun fromEntity(entity: MapMarkerEntity): MapMarkerUiState {
            return MapMarkerUiState(
                id = entity.id,
                itemType = MarkerItemType.fromName(entity.itemType),
                itemId = entity.itemId,
                itemDisplayName = entity.itemDisplayName,
                posX = entity.posX,
                posY = entity.posY,
                roomLabel = entity.roomLabel
            )
        }
    }
}

enum class MarkerItemType(val label: String, val icon: String) {
    FRIDGE("冰箱", "📦"),
    DIGITAL("数码", "📱"),
    PET("宠物", "🐾");

    companion object {
        fun fromName(name: String): MarkerItemType =
            entries.firstOrNull { it.name == name } ?: FRIDGE
    }
}
