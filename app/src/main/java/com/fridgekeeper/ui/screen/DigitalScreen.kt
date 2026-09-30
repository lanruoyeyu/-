package com.fridgekeeper.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgekeeper.model.DigitalItemUiState
import com.fridgekeeper.ui.components.AddDigitalItemDialog
import com.fridgekeeper.ui.components.DigitalItemCard
import com.fridgekeeper.ui.components.SellDigitalItemDialog
import com.fridgekeeper.ui.theme.Primary
import com.fridgekeeper.viewmodel.DigitalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalScreen(viewModel: DigitalViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var sellTarget by remember { mutableStateOf<DigitalItemUiState?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("数码产品", color = Color.White, fontWeight = FontWeight.Bold) },
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
                    contentDescription = "添加数码产品",
                    tint = Color.White
                )
            }
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
        } else if (uiState.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无数码产品\n点击右下角 + 添加",
                    fontSize = 16.sp,
                    color = Color(0xFF888888),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = uiState.items,
                    key = { it.id }
                ) { item ->
                    DigitalItemCard(
                        item = item,
                        onSell = { sellTarget = item }
                    )
                }
            }
        }
    }

    // 添加数码产品对话框
    if (showAddDialog) {
        AddDigitalItemDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, price, date ->
                viewModel.addItem(name, price, date)
                showAddDialog = false
            }
        )
    }

    // 卖出对话框
    sellTarget?.let { target ->
        SellDigitalItemDialog(
            item = target,
            onDismiss = { sellTarget = null },
            onConfirm = { price, date ->
                viewModel.sellItem(target.id, price, date)
                sellTarget = null
            }
        )
    }
}
