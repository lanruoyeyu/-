package com.fridgekeeper.model

import com.fridgekeeper.data.local.ItemEntity

data class ItemUiState(
    val id: Long,
    val name: String,
    val category: ItemCategory,
    val quantity: Int,
    val storageDate: Long,
    val expirationDate: Long,
    val remainingDays: Int,
    val isExpired: Boolean,
    val isWarning: Boolean,
    val isCleared: Boolean
) {
    companion object {
        fun fromEntity(entity: ItemEntity): ItemUiState {
            val remaining = DateUtils.calculateRemainingDays(entity.expirationDate)
            return ItemUiState(
                id = entity.id,
                name = entity.name,
                category = ItemCategory.fromString(entity.category),
                quantity = entity.quantity,
                storageDate = entity.storageDate,
                expirationDate = entity.expirationDate,
                remainingDays = remaining,
                isExpired = DateUtils.isExpired(remaining),
                isWarning = DateUtils.isWarning(remaining),
                isCleared = entity.isCleared
            )
        }
    }
}

data class FridgeUiState(
    val items: List<ItemUiState> = emptyList(),
    val totalCount: Int = 0,
    val categoryCounts: List<CategoryDisplay> = emptyList(),
    val isLoading: Boolean = true
)

data class CategoryDisplay(
    val category: ItemCategory,
    val count: Int
)
