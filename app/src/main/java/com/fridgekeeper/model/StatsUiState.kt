package com.fridgekeeper.model

import com.fridgekeeper.data.local.DigitalItemEntity
import com.fridgekeeper.data.local.ItemEntity
import com.fridgekeeper.data.local.PetCategoryEntity
import com.fridgekeeper.data.local.PetItemEntity
import java.util.Locale

data class StatsUiState(
    val fridgeStats: ModuleStats,
    val digitalStats: ModuleStats,
    val petStats: ModuleStats,
    val totalDailyCost: Double,
    val isLoading: Boolean = true
)

data class ModuleStats(
    val itemCount: Int,
    val totalCost: Double,
    val dailyCost: Double,
    val hasPrice: Boolean
)

data class PickerItem(
    val itemType: MarkerItemType,
    val itemId: Long,
    val displayName: String,
    val subtitle: String
)

object StatsCalculator {

    fun fridgeStats(items: List<ItemEntity>): ModuleStats {
        val count = items.sumOf { it.quantity }
        // 冰箱物品无买入价格字段
        return ModuleStats(count, 0.0, 0.0, hasPrice = false)
    }

    fun digitalStats(items: List<DigitalItemEntity>): ModuleStats {
        val active = items.filter { it.sellPrice == null || it.sellDate == null }
        val totalCost = active.sumOf { it.purchasePrice }
        val now = System.currentTimeMillis()
        val dailyCost = active.sumOf { item ->
            val days = DateUtils.calculateDaysHeld(item.purchaseDate, now)
            item.purchasePrice / days
        }
        return ModuleStats(active.size, totalCost, dailyCost, hasPrice = true)
    }

    fun petStats(
        items: List<PetItemEntity>,
        categories: List<PetCategoryEntity>
    ): ModuleStats {
        val now = System.currentTimeMillis()
        val totalCost = items.sumOf { it.purchasePrice }
        val avgDays = if (items.isNotEmpty()) {
            items.sumOf { DateUtils.calculateDaysHeld(it.purchaseDate, now).toDouble() } / items.size
        } else 0.0
        val dailyCost = if (avgDays > 0) totalCost / avgDays else 0.0
        return ModuleStats(items.size, totalCost, dailyCost, hasPrice = true)
    }

    fun buildPickerItems(
        fridgeItems: List<ItemEntity>,
        digitalItems: List<DigitalItemEntity>,
        petItems: List<PetItemEntity>,
        petCategories: List<PetCategoryEntity>
    ): List<PickerItem> {
        val result = mutableListOf<PickerItem>()

        fridgeItems.forEach { item ->
            val remaining = DateUtils.calculateRemainingDays(item.expirationDate)
            result.add(PickerItem(
                itemType = MarkerItemType.FRIDGE,
                itemId = item.id,
                displayName = item.name,
                subtitle = "数量${item.quantity} | 剩余${remaining}天"
            ))
        }

        digitalItems.forEach { item ->
            val isSold = item.sellPrice != null && item.sellDate != null
            val days = if (isSold) {
                DateUtils.calculateDaysHeld(item.purchaseDate, item.sellDate!!)
            } else {
                DateUtils.calculateDaysHeld(item.purchaseDate, System.currentTimeMillis())
            }
            result.add(PickerItem(
                itemType = MarkerItemType.DIGITAL,
                itemId = item.id,
                displayName = item.name,
                subtitle = if (isSold) "已卖出 | ${days}天"
                           else "持有${days}天 | ¥${String.format(Locale.CHINA, "%.2f", item.purchasePrice / days)}/天"
            ))
        }

        val catMap = petCategories.associate { it.id to it.name }
        petItems.forEach { item ->
            val cat = catMap[item.categoryId] ?: "未分类"
            result.add(PickerItem(
                itemType = MarkerItemType.PET,
                itemId = item.id,
                displayName = item.name,
                subtitle = "$cat | ¥${String.format(Locale.CHINA, "%.2f", item.purchasePrice)}"
            ))
        }

        return result
    }
}
