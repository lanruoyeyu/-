package com.fridgekeeper.model

/**
 * 宠物用品买入数量类型
 */
enum class QuantityType(val label: String) {
    WEIGHT("重量"),
    COUNT("个数");

    companion object {
        fun fromName(name: String): QuantityType =
            entries.firstOrNull { it.name == name } ?: COUNT
    }
}

/**
 * 重量单位
 */
enum class WeightUnit(val label: String) {
    G("g"),
    KG("kg");

    companion object {
        fun fromName(name: String?): WeightUnit =
            entries.firstOrNull { it.name == name } ?: G
    }
}
