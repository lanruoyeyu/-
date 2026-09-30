package com.fridgekeeper.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgekeeper.model.MapMarkerUiState
import com.fridgekeeper.model.ModuleStats
import com.fridgekeeper.model.PickerItem
import com.fridgekeeper.ui.components.ItemPickerDialog
import com.fridgekeeper.ui.components.MarkerInfoSheet
import com.fridgekeeper.ui.components.RoomMap
import com.fridgekeeper.ui.components.StatsCard
import com.fridgekeeper.ui.theme.Primary
import com.fridgekeeper.viewmodel.StatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(viewModel: StatsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val markers by viewModel.markers.collectAsState()

    var pickerPos by remember { mutableStateOf<Pair<Float, Float>?>(null) }
    var pickerItems by remember { mutableStateOf<List<PickerItem>>(emptyList()) }
    var selectedMarker by remember { mutableStateOf<MapMarkerUiState?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("物品统计", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Primary)
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                StatsCard(icon = "📦", title = "冰箱物品", stats = uiState.fridgeStats)
                StatsCard(icon = "📱", title = "数码产品", stats = uiState.digitalStats)
                StatsCard(icon = "🐾", title = "宠物用品", stats = uiState.petStats)
                StatsCard(
                    icon = "📊",
                    title = "总计",
                    stats = ModuleStats(
                        itemCount = uiState.fridgeStats.itemCount + uiState.digitalStats.itemCount + uiState.petStats.itemCount,
                        totalCost = uiState.digitalStats.totalCost + uiState.petStats.totalCost,
                        dailyCost = uiState.totalDailyCost,
                        hasPrice = true
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "房间地图",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                RoomMap(
                    markers = markers,
                    onTapMarker = { marker -> selectedMarker = marker },
                    onLongPress = { x, y ->
                        pickerPos = Pair(x, y)
                        viewModel.buildPickerItems { items -> pickerItems = items }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (markers.isNotEmpty()) {
                    Text(
                        text = "已标注物品",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF555555),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    markers.forEach { marker ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = marker.itemType.icon, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = marker.itemDisplayName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF212121)
                                )
                                Text(
                                    text = marker.itemType.label,
                                    fontSize = 12.sp,
                                    color = Color(0xFF888888)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    pickerPos?.let { pos ->
        ItemPickerDialog(
            items = pickerItems,
            onDismiss = { pickerPos = null },
            onPick = { item ->
                viewModel.addMarker(
                    itemType = item.itemType,
                    itemId = item.itemId,
                    displayName = item.displayName,
                    posX = pos.first,
                    posY = pos.second
                )
                pickerPos = null
            }
        )
    }

    selectedMarker?.let { marker ->
        val infoText = "${marker.itemType.icon} ${marker.itemDisplayName}\n来源：${marker.itemType.label}"
        MarkerInfoSheet(
            marker = marker,
            infoText = infoText,
            onDismiss = { selectedMarker = null },
            onRemove = {
                viewModel.deleteMarker(marker.id)
                selectedMarker = null
            }
        )
    }
}
