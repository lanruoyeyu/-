package com.fridgekeeper.model

enum class ItemCategory(
    val displayName: String,
    val icon: String
) {
    VEGETABLE_FRUIT("蔬菜水果", "🥬"),
    MEAT_SEAFOOD("肉类海鲜", "🥩"),
    DAIRY("乳制品", "🥛"),
    BEVERAGE("饮料", "🥤"),
    COOKED_FOOD("熟食剩菜", "🍱"),
    OTHER("其他", "📦");

    companion object {
        fun fromString(value: String): ItemCategory =
            entries.find { it.name == value } ?: OTHER
    }
}
