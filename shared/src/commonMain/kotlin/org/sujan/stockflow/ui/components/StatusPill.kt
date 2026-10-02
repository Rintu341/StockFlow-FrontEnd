package org.sujan.stockflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sujan.stockflow.domain.model.OrderStatus
import org.sujan.stockflow.ui.theme.*

@Composable
fun StatusPill(status: OrderStatus) {
    val (backgroundColor, textColor) = when (status) {
        OrderStatus.PENDING -> StatusPendingBg to StatusPending
        OrderStatus.APPROVED -> StatusApprovedBg to StatusApproved
        OrderStatus.PACKED -> StatusApprovedBg to StatusApproved
        OrderStatus.SHIPPED -> StatusShippedBg to StatusShipped
        OrderStatus.REJECTED -> StatusRejectedBg to StatusRejected
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.name.lowercase().replaceFirstChar { it.uppercase() },
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}
