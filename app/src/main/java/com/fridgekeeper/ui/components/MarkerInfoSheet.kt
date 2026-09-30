package com.fridgekeeper.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgekeeper.model.MapMarkerUiState
import com.fridgekeeper.ui.theme.AlertRed
import com.fridgekeeper.ui.theme.Primary

@Composable
fun MarkerInfoSheet(
    marker: MapMarkerUiState,
    infoText: String,
    onDismiss: () -> Unit,
    onRemove: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = marker.itemType.icon, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = marker.itemDisplayName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = marker.itemType.label,
                        fontSize = 13.sp,
                        color = Color(0xFF888888)
                    )
                }
            }
        },
        text = {
            Text(
                text = infoText,
                fontSize = 15.sp,
                color = Color(0xFF333333),
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onRemove) {
                Text("移除标注", color = AlertRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭", color = Primary)
            }
        }
    )
}
