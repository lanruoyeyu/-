package com.fridgekeeper.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgekeeper.model.MapMarkerUiState
import com.fridgekeeper.ui.theme.Primary

@Composable
fun RoomMap(
    markers: List<MapMarkerUiState>,
    onTapMarker: (MapMarkerUiState) -> Unit,
    onLongPress: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        // 检测是否点击了某个标注
                        val size = this.size
                        markers.forEach { marker ->
                            val markerPos = Offset(
                                x = marker.posX * size.width,
                                y = marker.posY * size.height
                            )
                            val dist = (offset - markerPos).getDistance()
                            if (dist <= 40f) {
                                onTapMarker(marker)
                                return@detectTapGestures
                            }
                        }
                    },
                    onLongPress = { offset ->
                        val size = this.size
                        val normX = offset.x / size.width
                        val normY = offset.y / size.height
                        if (normX in 0f..1f && normY in 0f..1f) {
                            onLongPress(normX, normY)
                        }
                    }
                )
            }
    ) {
        Canvas(
            modifier = Modifier.fillMaxWidth().height(280.dp)
        ) {
            val w = size.width
            val h = size.height

            // 房间背景
            drawRect(color = Color(0xFFF0F4F8))

            // 房间边框
            drawRect(
                color = Color(0xFFB0BEC5),
                size = size,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
            )

            // 分隔线（模拟房间区域）
            drawLine(
                color = Color(0xFFCFD8DC),
                start = Offset(w * 0.5f, 0f),
                end = Offset(w * 0.5f, h * 0.6f),
                strokeWidth = 3f
            )
            drawLine(
                color = Color(0xFFCFD8DC),
                start = Offset(0f, h * 0.6f),
                end = Offset(w, h * 0.6f),
                strokeWidth = 3f
            )

            // 标注点
            markers.forEach { marker ->
                val pos = Offset(marker.posX * w, marker.posY * h)
                // 外圈
                drawCircle(
                    color = Primary.copy(alpha = 0.3f),
                    radius = 22f,
                    center = pos
                )
                // 内圈
                drawCircle(
                    color = Primary,
                    radius = 14f,
                    center = pos
                )
                // 白色中心点
                drawCircle(
                    color = Color.White,
                    radius = 6f,
                    center = pos
                )
            }
        }

        // 空状态提示
        if (markers.isEmpty()) {
            Text(
                text = "长按地图添加物品标注",
                fontSize = 14.sp,
                color = Color(0xFF888888),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
