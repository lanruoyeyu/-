package com.fridgekeeper.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgekeeper.model.DateUtils
import com.fridgekeeper.model.ItemUiState
import com.fridgekeeper.ui.theme.AlertRed
import com.fridgekeeper.ui.theme.AlertRedBg
import com.fridgekeeper.ui.theme.ClearedGray
import com.fridgekeeper.ui.theme.ClearedGrayBg
import com.fridgekeeper.ui.theme.NormalGreen
import com.fridgekeeper.ui.theme.WarningOrange
import com.fridgekeeper.ui.theme.WarningOrangeBg
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ItemCard(
    item: ItemUiState,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (item.quantity == 0) {
        ClearedItemCard(item = item, onClear = onClear, modifier = modifier)
        return
    }
    SwipeableItemCard(
        item = item,
        onDecrease = onDecrease,
        onIncrease = onIncrease,
        modifier = modifier
    )
}

@Composable
private fun SwipeableItemCard(
    item: ItemUiState,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val actionWidthPx = with(density) { 140.dp.toPx() }
    val scope = rememberCoroutineScope()

    val offsetX = remember { Animatable(0f) }
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // 背景操作层
        Row(
            modifier = Modifier
                .matchParentSize()
                .background(
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDecrease,
                modifier = Modifier
                    .size(40.dp)
                    .background(AlertRed, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = "消耗",
                    tint = Color.White
                )
            }

            Text(
                text = "${item.quantity}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(
                onClick = onIncrease,
                modifier = Modifier
                    .size(40.dp)
                    .background(NormalGreen, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "加仓",
                    tint = Color.White
                )
            }
        }

        // 前景卡片层
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            isExpanded = offsetX.value < -actionWidthPx / 2
                            scope.launch {
                                if (isExpanded) {
                                    offsetX.animateTo(-actionWidthPx, tween(200))
                                } else {
                                    offsetX.animateTo(0f, tween(200))
                                }
                            }
                        },
                        onDragCancel = {
                            isExpanded = offsetX.value < -actionWidthPx / 2
                            scope.launch {
                                if (isExpanded) {
                                    offsetX.animateTo(-actionWidthPx, tween(200))
                                } else {
                                    offsetX.animateTo(0f, tween(200))
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        scope.launch {
                            offsetX.snapTo(
                                (offsetX.value + dragAmount).coerceIn(-actionWidthPx, 0f)
                            )
                        }
                    }
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    item.isExpired -> AlertRedBg
                    item.isWarning -> WarningOrangeBg
                    else -> Color.White
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            ItemCardContent(item)
        }
    }
}

@Composable
private fun ItemCardContent(item: ItemUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.category.icon,
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.isExpired) AlertRed else Color(0xFF212121)
                )
                Text(
                    text = "x${item.quantity}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (item.isExpired) AlertRed else Color(0xFF616161)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFF0F0F0)
            ) {
                Text(
                    text = item.category.displayName,
                    fontSize = 11.sp,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "存放：${DateUtils.formatDate(item.storageDate)}",
                fontSize = 12.sp,
                color = Color(0xFF888888)
            )
            Text(
                text = "过期：${DateUtils.formatDate(item.expirationDate)}",
                fontSize = 12.sp,
                color = Color(0xFF888888)
            )

            Spacer(modifier = Modifier.height(4.dp))

            val remainingText = when {
                item.isExpired -> "已过期"
                item.remainingDays == 1 -> "保质期剩余：1天"
                else -> "保质期剩余：${item.remainingDays}天"
            }
            val remainingColor = when {
                item.isExpired -> AlertRed
                item.isWarning -> WarningOrange
                else -> NormalGreen
            }
            Text(
                text = remainingText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = remainingColor
            )
        }
    }
}

@Composable
private fun ClearedItemCard(
    item: ItemUiState,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ClearedGrayBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.category.icon,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ClearedGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "数量：0  |  已耗尽",
                    fontSize = 12.sp,
                    color = ClearedGray
                )
            }

            Surface(
                onClick = onClear,
                shape = RoundedCornerShape(20.dp),
                color = ClearedGray
            ) {
                Text(
                    text = "已清空",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
