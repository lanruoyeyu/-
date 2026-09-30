package com.fridgekeeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fridgekeeper.data.local.PetItemEntity
import com.fridgekeeper.data.repository.PetRepository
import com.fridgekeeper.model.PetItemUiState
import com.fridgekeeper.model.PetUiState
import com.fridgekeeper.model.PetCategoryUiState
import com.fridgekeeper.model.QuantityType
import com.fridgekeeper.model.WeightUnit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetViewModel(private val repository: PetRepository) : ViewModel() {

    val uiState: StateFlow<PetUiState> = combine(
        repository.observeItems(),
        repository.observeCategories()
    ) { items, categories ->
        val categoryNames = categories.associate { it.id to it.name }
        PetUiState(
            items = items.map {
                PetItemUiState.fromEntity(
                    it,
                    categoryNames[it.categoryId] ?: "未分类"
                )
            },
            categories = categories.map { PetCategoryUiState.fromEntity(it) },
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PetUiState(isLoading = true)
    )

    /**
     * 新增种类。成功返回 true，重名返回 false
     */
    fun addCategory(name: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.addCategory(name)
            onResult(id > 0)
        }
    }

    fun addItem(
        name: String,
        categoryId: Long,
        purchasePrice: Double,
        purchaseDate: Long,
        quantityType: QuantityType,
        weightUnit: WeightUnit,
        quantity: Double,
        tags: String
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.addItem(
                PetItemEntity(
                    name = name,
                    categoryId = categoryId,
                    purchasePrice = purchasePrice,
                    purchaseDate = purchaseDate,
                    quantityType = quantityType.name,
                    weightUnit = if (quantityType == QuantityType.WEIGHT) weightUnit.name else null,
                    quantity = quantity,
                    tags = tags,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }
    }
}
