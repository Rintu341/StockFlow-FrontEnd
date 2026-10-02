package org.sujan.stockflow.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.sujan.stockflow.domain.model.OrderStatus
import org.sujan.stockflow.ui.theme.StatusAdmin
import org.sujan.stockflow.ui.theme.StatusShipped

@Composable
fun TaskCard(
    orderId: String,
    customerName: String,
    itemsCount: Int,
    date: String,
    status: OrderStatus,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 0.5.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = orderId,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$customerName • $itemsCount items • $date",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
                StatusPill(status = status)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            val actionText = when (status) {
                OrderStatus.APPROVED -> "Mark as Packed"
                OrderStatus.PACKED -> "Mark as Shipped"
                else -> ""
            }
            
            val actionColor = when (status) {
                OrderStatus.APPROVED -> StatusAdmin
                OrderStatus.PACKED -> StatusShipped
                else -> MaterialTheme.colorScheme.primary
            }

            if (actionText.isNotEmpty()) {
                Button(
                    onClick = onAction,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = actionColor)
                ) {
                    Text(
                        text = actionText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
