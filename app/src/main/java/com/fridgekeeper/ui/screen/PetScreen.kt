package com.fridgekeeper.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgekeeper.ui.components.AddPetItemDialog
import com.fridgekeeper.ui.components.PetItemCard
import com.fridgekeeper.ui.theme.Primary
import com.fridgekeeper.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetScreen(viewModel: PetViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // 0 表示全部
    var filterCategoryId by remember { mutableLongStateOf(0L) }

    val shownItems = if (filterCategoryId == 0L) uiState.items
    else uiState.items.filter { it.categoryId == filterCategoryId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("宠物用品", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "添加宠物用品",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize()) {
            // 分类筛选条
            if (uiState.categories.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = filterCategoryId == 0L,
                            onClick = { filterCategoryId = 0L },
                            label = { Text("全部") }
                        )
                    }
                    items(uiState.categories, key = { it.id }) { category ->
                        FilterChip(
                            selected = filterCategoryId == category.id,
                            onClick = { filterCategoryId = category.id },
                            label = { Text(category.name) }
                        )
                    }
                }
            }

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primary)
                    }
                }
                shownItems.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "暂无宠物用品\n点击右下角 + 添加",
                            fontSize = 16.sp,
                            color = Color(0xFF888888),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(items = shownItems, key = { it.id }) { item ->
                            PetItemCard(item = item)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddPetItemDialog(
            categories = uiState.categories,
            onDismiss = { showAddDialog = false },
            onAddCategory = { name, onResult ->
                viewModel.addCategory(name) { success ->
                    // 新增成功后，从最新分类列表中找到 id 传回
                    if (success) {
                        val newId = viewModel.uiState.value.categories
                            .firstOrNull { it.name == name.trim() }?.id ?: -1L
                        onResult(newId > 0, newId)
                    } else {
                        onResult(false, -1L)
                    }
                }
            },
            onConfirm = { name, categoryId, price, date, qType, unit, qty, tags ->
                viewModel.addItem(name, categoryId, price, date, qType, unit, qty, tags)
                showAddDialog = false
            }
        )
    }
}
