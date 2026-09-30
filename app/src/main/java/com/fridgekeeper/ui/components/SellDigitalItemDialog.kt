package com.fridgekeeper.ui.components

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
import com.fridgekeeper.model.DigitalItemUiState
import com.fridgekeeper.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellDigitalItemDialog(
    item: DigitalItemUiState,
    onDismiss: () -> Unit,
    onConfirm: (sellPrice: Double, sellDate: Long) -> Unit
) {
    var price by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf(false) }

    var sellDate by remember { mutableLongStateOf(DateUtils.todayStartMillis()) }
    var dateError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("卖出 - ${item.name}", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "买入价格：${formatMoney(item.purchasePrice)} ｜ 已持有${item.daysHeld}天",
                    fontSize = 13.sp,
                    color = Color(0xFF888888)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 卖出价格
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                            price = it
                            priceError = false
                        }
                    },
                    label = { Text("卖出价格（元）") },
                    isError = priceError,
                    supportingText = {
                        if (priceError) Text("请输入有效的卖出价格", color = Color.Red)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 卖出时间
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "卖出时间：${DateUtils.formatDate(sellDate)}",
                        modifier = Modifier.weight(1f)
                    )
                    Text("📅", fontSize = 16.sp)
                }

                if (dateError) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("卖出时间不能早于买入时间", color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    var hasError = false
                    val priceValue = price.toDoubleOrNull()
                    if (priceValue == null || priceValue <= 0) {
                        priceError = true
                        hasError = true
                    }
                    if (sellDate < item.purchaseDate) {
                        dateError = true
                        hasError = true
                    }
                    if (!hasError) {
                        onConfirm(priceValue!!, sellDate)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("确认卖出")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )

    if (showDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = sellDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let {
                            sellDate = it
                            dateError = it < item.purchaseDate
                        }
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
}
