package com.fridgekeeper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
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
import com.fridgekeeper.model.DateUtils
import com.fridgekeeper.model.PetCategoryUiState
import com.fridgekeeper.model.QuantityType
import com.fridgekeeper.model.WeightUnit
import com.fridgekeeper.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetItemDialog(
    categories: List<PetCategoryUiState>,
    onDismiss: () -> Unit,
    onAddCategory: (name: String, onResult: (Boolean, Long) -> Unit) -> Unit,
    onConfirm: (
        name: String,
        categoryId: Long,
        purchasePrice: Double,
        purchaseDate: Long,
        quantityType: QuantityType,
        weightUnit: WeightUnit,
        quantity: Double,
        tags: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }

    var price by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf(false) }

    var purchaseDate by remember { mutableLongStateOf(DateUtils.todayStartMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // 种类
    var selectedCategoryId by remember(categories) {
        mutableLongStateOf(categories.firstOrNull()?.id ?: 0L)
    }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var showNewCategoryDialog by remember { mutableStateOf(false) }

    // 数量
    var quantityType by remember { mutableStateOf(QuantityType.WEIGHT) }
    var weightUnit by remember { mutableStateOf(WeightUnit.G) }
    var quantity by remember { mutableStateOf("") }
    var quantityError by remember { mutableStateOf(false) }

    // 标签
    var tags by remember { mutableStateOf("") }

    val selectedCategoryName = categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "请选择"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加宠物用品", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
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

                // 种类下拉 + 新增种类
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = categoryMenuExpanded,
                        onExpandedChange = { categoryMenuExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedCategoryName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("种类") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryMenuExpanded,
                            onDismissRequest = { categoryMenuExpanded = false }
                        ) {
                            categories.forEach { category ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategoryId = category.id
                                        categoryMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { showNewCategoryDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("新种类")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 买入价格
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                            price = it
                            priceError = false
                        }
                    },
                    label = { Text("买入价格（元）") },
                    isError = priceError,
                    supportingText = {
                        if (priceError) Text("请输入有效的买入价格", color = Color.Red)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 买入时间（文本按钮式）
                TextButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "买入时间：${DateUtils.formatDate(purchaseDate)}  📅",
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 数量类型：重量 / 个数
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = quantityType == QuantityType.WEIGHT,
                        onClick = { quantityType = QuantityType.WEIGHT },
                        label = { Text("重量") }
                    )
                    FilterChip(
                        selected = quantityType == QuantityType.COUNT,
                        onClick = { quantityType = QuantityType.COUNT },
                        label = { Text("个数") }
                    )

                    if (quantityType == QuantityType.WEIGHT) {
                        Spacer(modifier = Modifier.weight(1f))
                        FilterChip(
                            selected = weightUnit == WeightUnit.G,
                            onClick = { weightUnit = WeightUnit.G },
                            label = { Text("g") }
                        )
                        FilterChip(
                            selected = weightUnit == WeightUnit.KG,
                            onClick = { weightUnit = WeightUnit.KG },
                            label = { Text("kg") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 数量值
                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                            quantity = it
                            quantityError = false
                        }
                    },
                    label = {
                        Text(
                            if (quantityType == QuantityType.WEIGHT) "重量（${weightUnit.label}）"
                            else "个数"
                        )
                    },
                    isError = quantityError,
                    supportingText = {
                        if (quantityError) Text("请输入有效的数量", color = Color.Red)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 标签
                OutlinedTextField(
                    value = tags,
                    onValueChange = { if (it.length <= 30) tags = it },
                    label = { Text("标签（如品牌，多个用逗号分隔）") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    var hasError = false
                    if (name.isBlank()) {
                        nameError = true
                        hasError = true
                    }
                    val priceValue = price.toDoubleOrNull()
                    if (priceValue == null || priceValue <= 0) {
                        priceError = true
                        hasError = true
                    }
                    val quantityValue = quantity.toDoubleOrNull()
                    if (quantityValue == null || quantityValue <= 0) {
                        quantityError = true
                        hasError = true
                    }
                    if (selectedCategoryId == 0L) hasError = true
                    if (!hasError) {
                        onConfirm(
                            name.trim(),
                            selectedCategoryId,
                            priceValue!!,
                            purchaseDate,
                            quantityType,
                            weightUnit,
                            quantityValue!!,
                            tags
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("确认添加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )

    // 买入时间选择器
    if (showDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = purchaseDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let { purchaseDate = it }
                        showDatePicker = false
                    }
                ) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("取消") }
            }
        ) {
            DatePicker(state = state)
        }
    }

    // 新增种类对话框
    if (showNewCategoryDialog) {
        var newCategoryName by remember { mutableStateOf("") }
        var duplicateError by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = { showNewCategoryDialog = false },
            title = { Text("新增种类") },
            text = {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = {
                        if (it.length <= 10) {
                            newCategoryName = it
                            duplicateError = false
                        }
                    },
                    label = { Text("种类名称") },
                    isError = duplicateError,
                    supportingText = {
                        if (duplicateError) Text("该种类已存在", color = Color.Red)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAddCategory(newCategoryName) { success, newId ->
                            if (success) {
                                selectedCategoryId = newId
                                showNewCategoryDialog = false
                            } else {
                                duplicateError = true
                            }
                        }
                    }
                ) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showNewCategoryDialog = false }) { Text("取消") }
            }
        )
    }
}
