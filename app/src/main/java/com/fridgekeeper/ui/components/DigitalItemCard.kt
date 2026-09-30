package com.fridgekeeper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgekeeper.model.DateUtils
import com.fridgekeeper.model.DigitalItemUiState
import com.fridgekeeper.ui.theme.AlertRed
import com.fridgekeeper.ui.theme.NormalGreen
import com.fridgekeeper.ui.theme.WarningOrange
import java.util.Locale
import kotlin.math.abs

internal fun formatMoney(value: Double): String =
    String.format(Locale.CHINA, "¥%.2f", value)

@Composable
fun DigitalItemCard(
    item: DigitalItemUiState,
    onSell: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "📱", fontSize = 32.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // 名称 + 买入价格
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Text(
                        text = formatMoney(item.purchasePrice),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF00695C)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (!item.isSold) {
                    // 未卖出：每日成本随时间增长
                    Text(
                        text = "买入时间：${DateUtils.formatDate(item.purchaseDate)}",
                        fontSize = 12.sp,
                        color = Color(0xFF888888)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "已持有${item.daysHeld}天 ｜ 每日成本：${formatMoney(item.dailyCost)}/天",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WarningOrange
                        )
                        Surface(
                            onClick = onSell,
                            shape = RoundedCornerShape(20.dp),
                            color = WarningOrange
                        ) {
                            Text(
                                text = "卖出",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                } else {
                    // 已卖出：数据冻结，不再计算每日成本
                    Text(
                        text = "买入：${DateUtils.formatDate(item.purchaseDate)} ｜ 卖出：${DateUtils.formatDate(item.sellDate!!)}",
                        fontSize = 12.sp,
                        color = Color(0xFF888888)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "卖出价：${formatMoney(item.sellPrice!!)}",
                        fontSize = 13.sp,
                        color = Color(0xFF555555)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val profit = item.profit ?: 0.0
                        val profitColor = if (profit >= 0) NormalGreen else AlertRed
                        val sign = if (profit >= 0) "+" else "-"
                        Text(
                            text = "损益：$sign${formatMoney(abs(profit))}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = profitColor
                        )
                        Text(
                            text = "最终成本：${formatMoney(item.dailyCost)}/天",
                            fontSize = 12.sp,
                            color = Color(0xFF888888)
                        )
                    }
                }
            }
        }
    }
}
