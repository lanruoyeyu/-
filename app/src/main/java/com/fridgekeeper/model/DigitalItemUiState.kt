package com.fridgekeeper.model

import com.fridgekeeper.data.local.DigitalItemEntity

data class DigitalItemUiState(
    val id: Long,
    val name: String,
    val purchasePrice: Double,
    val purchaseDate: Long,
    val sellPrice: Double?,
    val sellDate: Long?,
    val isSold: Boolean,
    val daysHeld: Int,
    val dailyCost: Double,
    val profit: Double?
) {
    companion object {
        fun fromEntity(entity: DigitalItemEntity): DigitalItemUiState {
            val isSold = entity.sellPrice != null && entity.sellDate != null
            // 未卖出按当日计算；卖出后按卖出日冻结
            val endDate = if (isSold) entity.sellDate!! else System.currentTimeMillis()
            val daysHeld = DateUtils.calculateDaysHeld(entity.purchaseDate, endDate)
            val dailyCost = entity.purchasePrice / daysHeld
            val profit = if (isSold) entity.sellPrice!! - entity.purchasePrice else null
            return DigitalItemUiState(
                id = entity.id,
                name = entity.name,
                purchasePrice = entity.purchasePrice,
                purchaseDate = entity.purchaseDate,
                sellPrice = entity.sellPrice,
                sellDate = entity.sellDate,
                isSold = isSold,
                daysHeld = daysHeld,
                dailyCost = dailyCost,
                profit = profit
            )
        }
    }
}

data class DigitalUiState(
    val items: List<DigitalItemUiState> = emptyList(),
    val isLoading: Boolean = true
)
