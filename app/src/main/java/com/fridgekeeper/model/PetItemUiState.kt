package com.fridgekeeper.model

import com.fridgekeeper.data.local.PetCategoryEntity
import com.fridgekeeper.data.local.PetItemEntity

data class PetCategoryUiState(
    val id: Long,
    val name: String,
    val isDefault: Boolean
) {
    companion object {
        fun fromEntity(entity: PetCategoryEntity) = PetCategoryUiState(
            id = entity.id,
            name = entity.name,
            isDefault = entity.isDefault
        )
    }
}

data class PetItemUiState(
    val id: Long,
    val name: String,
    val categoryId: Long,
    val categoryName: String,
    val purchasePrice: Double,
    val purchaseDate: Long,
    val quantityType: QuantityType,
    val weightUnit: WeightUnit,
    val quantity: Double,
    val tags: List<String>
) {
    companion object {
        fun fromEntity(entity: PetItemEntity, categoryName: String): PetItemUiState {
            return PetItemUiState(
                id = entity.id,
                name = entity.name,
                categoryId = entity.categoryId,
                categoryName = categoryName,
                purchasePrice = entity.purchasePrice,
                purchaseDate = entity.purchaseDate,
                quantityType = QuantityType.fromName(entity.quantityType),
                weightUnit = WeightUnit.fromName(entity.weightUnit),
                quantity = entity.quantity,
                tags = entity.tags.split(",", "，")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
            )
        }
    }
}

data class PetUiState(
    val items: List<PetItemUiState> = emptyList(),
    val categories: List<PetCategoryUiState> = emptyList(),
    val isLoading: Boolean = true
)
