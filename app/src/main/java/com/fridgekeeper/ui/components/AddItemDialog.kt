package com.fridgekeeper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgekeeper.model.DateUtils
import com.fridgekeeper.model.ItemCategory
import com.fridgekeeper.ui.theme.Primary
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, category: ItemCategory, quantity: Int, storageDate: Long, expirationDate: Long) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(ItemCategory.VEGETABLE_FRUIT) }

    var quantity by remember { mutableStateOf("1") }
    var quantityError by remember { mutableStateOf(false) }

    val todayStart = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    var storageDate by remember { mutableLongStateOf(todayStart) }
    var expirationDate by remember { mutableLongStateOf(todayStart + 7L * 24 * 60 * 60 * 1000) }
    var dateError by remember { mutableStateOf(false) }

    var showStoragePicker by remember { mutableStateOf(false) }
    var showExpirationPicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加物品", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // 物品名称
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        if (it.length <= 20) {
                            name = it
                            nameError = false
                        }
                    },
                    label = { Text("物品名称") },
                    isError = nameError,
                    supportingText = {
                        if (nameError) Text("请输入物品名称", color = Color.Red)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 物品分类
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text(
                            text = "${selectedCategory.icon} ${selectedCategory.displayName}",
                            modifier = Modifier.weight(1f)
                        )
                        Text("▼", fontSize = 12.sp, color = Color.Gray)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ItemCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.icon} ${cat.displayName}") },
                                onClick = {
                                    selectedCategory = cat
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 数量
                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        if (it.all { c -> c.isDigit() } && it.length <= 5) {
                            quantity = it
                            quantityError = false
                        }
                    },
                    label = { Text("数量") },
                    isError = quantityError,
                    supportingText = {
                        if (quantityError) Text("数量至少为1", color = Color.Red)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 存放时间
                OutlinedButton(
                    onClick = { showStoragePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("存放：${DateUtils.formatDate(storageDate)}", modifier = Modifier.weight(1f))
                    Text("📅", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 过期时间
                OutlinedButton(
                    onClick = { showExpirationPicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("过期：${DateUtils.formatDate(expirationDate)}", modifier = Modifier.weight(1f))
                    Text("📅", fontSize = 16.sp)
                }

                if (dateError) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("过期时间不能早于存放时间", color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // 验证
                    var hasError = false
                    if (name.isBlank()) {
                        nameError = true
                        hasError = true
                    }
                    val qty = quantity.toIntOrNull() ?: 0
                    if (qty < 1) {
                        quantityError = true
                        hasError = true
                    }
                    if (expirationDate < storageDate) {
                        dateError = true
                        hasError = true
                    }
                    if (!hasError) {
                        onConfirm(name.trim(), selectedCategory, qty, storageDate, expirationDate)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("确认添加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )

    // 存放时间选择器
    if (showStoragePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = storageDate)
        DatePickerDialog(
            onDismissRequest = { showStoragePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let {
                            storageDate = it
                            if (expirationDate < it) {
                                expirationDate = it + 7L * 24 * 60 * 60 * 1000
                            }
                            dateError = false
                        }
                        showStoragePicker = false
                    }
                ) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showStoragePicker = false }) { Text("取消") }
            }
        ) {
            DatePicker(state = state)
        }
    }

    // 过期时间选择器
    if (showExpirationPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = expirationDate)
        DatePickerDialog(
            onDismissRequest = { showExpirationPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let {
                            expirationDate = it
                            dateError = it < storageDate
                        }
                        showExpirationPicker = false
                    }
                ) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showExpirationPicker = false }) { Text("取消") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
