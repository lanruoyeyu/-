package com.fridgekeeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fridgekeeper.data.local.ItemEntity
import com.fridgekeeper.data.repository.FridgeRepository
import com.fridgekeeper.model.CategoryDisplay
import com.fridgekeeper.model.FridgeUiState
import com.fridgekeeper.model.ItemCategory
import com.fridgekeeper.model.ItemUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FridgeViewModel(private val repository: FridgeRepository) : ViewModel() {

    val uiState: StateFlow<FridgeUiState> = combine(
        repository.observeActiveItems(),
        repository.observeTotalCount(),
        repository.observeCategoryCounts()
    ) { items, totalCount, categoryCounts ->
        val sortedItems = items.map { ItemUiState.fromEntity(it) }
            .sortedWith(
                compareBy<ItemUiState> { it.remainingDays }
                    .thenBy { it.storageDate }
            )

        val displays = categoryCounts.mapNotNull { cc ->
            val cat = ItemCategory.fromString(cc.category)
            if (cc.count > 0) CategoryDisplay(cat, cc.count) else null
        }

        FridgeUiState(
            items = sortedItems,
            totalCount = totalCount ?: 0,
            categoryCounts = displays,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FridgeUiState(isLoading = true)
    )

    fun addItem(
        name: String,
        category: ItemCategory,
        quantity: Int,
        storageDate: Long,
        expirationDate: Long
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.addItem(
                ItemEntity(
                    name = name,
                    category = category.name,
                    quantity = quantity,
                    storageDate = storageDate,
                    expirationDate = expirationDate,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }
    }

    fun decreaseQuantity(id: Long, currentQuantity: Int) {
        if (currentQuantity <= 0) return
        viewModelScope.launch {
            repository.updateQuantity(id, currentQuantity - 1)
        }
    }

    fun increaseQuantity(id: Long, currentQuantity: Int) {
        viewModelScope.launch {
            repository.updateQuantity(id, currentQuantity + 1)
        }
    }

    fun clearItem(id: Long) {
        viewModelScope.launch {
            repository.clearItem(id)
        }
    }
}
