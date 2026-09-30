package com.fridgekeeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fridgekeeper.data.local.MapMarkerEntity
import com.fridgekeeper.data.repository.StatsRepository
import com.fridgekeeper.model.MapMarkerUiState
import com.fridgekeeper.model.MarkerItemType
import com.fridgekeeper.model.PickerItem
import com.fridgekeeper.model.StatsCalculator
import com.fridgekeeper.model.StatsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StatsViewModel(private val repository: StatsRepository) : ViewModel() {

    private val refreshTick = flow {
        while (true) {
            emit(Unit)
            delay(60 * 60 * 1000L)
        }
    }

    val uiState: StateFlow<StatsUiState> = combine(
        repository.observeFridgeItems(),
        repository.observeDigitalItems(),
        repository.observePetItems(),
        repository.observePetCategories(),
        refreshTick
    ) { fridge, digital, pet, petCats, _ ->
        val fridgeStats = StatsCalculator.fridgeStats(fridge)
        val digitalStats = StatsCalculator.digitalStats(digital)
        val petStats = StatsCalculator.petStats(pet, petCats)
        StatsUiState(
            fridgeStats = fridgeStats,
            digitalStats = digitalStats,
            petStats = petStats,
            totalDailyCost = digitalStats.dailyCost + petStats.dailyCost,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatsUiState(
            fridgeStats = com.fridgekeeper.model.ModuleStats(0, 0.0, 0.0, false),
            digitalStats = com.fridgekeeper.model.ModuleStats(0, 0.0, 0.0, true),
            petStats = com.fridgekeeper.model.ModuleStats(0, 0.0, 0.0, true),
            totalDailyCost = 0.0,
            isLoading = true
        )
    )

    val markers: StateFlow<List<MapMarkerUiState>> =
        repository.observeMarkers()
            .map { list -> list.map { MapMarkerUiState.fromEntity(it) } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun buildPickerItems(onResult: (List<PickerItem>) -> Unit) {
        viewModelScope.launch {
            // 读取当前快照数据
            val fridge = repository.observeFridgeItems()
            val digital = repository.observeDigitalItems()
            val pet = repository.observePetItems()
            val petCats = repository.observePetCategories()
            // 使用 combine 获取当前值
            combine(fridge, digital, pet, petCats) { f, d, p, c ->
                StatsCalculator.buildPickerItems(f, d, p, c)
            }.let { flow ->
                flow.collect { items ->
                    onResult(items)
                    return@collect
                }
            }
        }
    }

    fun addMarker(
        itemType: MarkerItemType,
        itemId: Long,
        displayName: String,
        posX: Float,
        posY: Float
    ) {
        viewModelScope.launch {
            repository.addMarker(
                MapMarkerEntity(
                    itemType = itemType.name,
                    itemId = itemId,
                    itemDisplayName = displayName,
                    posX = posX,
                    posY = posY
                )
            )
        }
    }

    fun deleteMarker(id: Long) {
        viewModelScope.launch {
            repository.deleteMarker(id)
        }
    }
}
