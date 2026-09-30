package com.fridgekeeper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgekeeper.model.MarkerItemType
import com.fridgekeeper.model.PickerItem

@Composable
fun ItemPickerDialog(
    items: List<PickerItem>,
    onDismiss: () -> Unit,
    onPick: (PickerItem) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = MarkerItemType.entries
    val filtered = items.filter { it.itemType == tabs[selectedTab] }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择物品标注", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text("${tab.icon} ${tab.label}") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                ) {
                    if (filtered.isEmpty()) {
                        item {
                            Text(
                                text = "暂无${tabs[selectedTab].label}物品",
                                color = Color(0xFF888888),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    items(filtered, key = { "${it.itemType.name}_${it.itemId}" }) { item ->
                        Surface(
                            onClick = { onPick(item) },
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = item.itemType.icon, fontSize = 28.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = item.displayName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF212121)
                                    )
                                    Text(
                                        text = item.subtitle,
                                        fontSize = 13.sp,
                                        color = Color(0xFF888888)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}
