package org.sujan.stockflow.presentation.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sujan.stockflow.domain.model.OrderStatus
import org.sujan.stockflow.ui.components.TaskCard
import org.sujan.stockflow.ui.theme.StatusShipped
import org.sujan.stockflow.ui.theme.StatusShippedBg

@Composable
fun MyTasksScreen() {
    val tasks = listOf(
        Task("ORD-1041", "Marco Bellini", 1, "Aug 17", OrderStatus.APPROVED),
        Task("ORD-1038", "Lena Fischer", 4, "Aug 14", OrderStatus.APPROVED),
        Task("ORD-1036", "Hana Kobayashi", 3, "Aug 12", OrderStatus.PACKED)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "My Tasks",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = if (tasks.isEmpty()) "All caught up!" else "${tasks.size} orders need your attention",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (tasks.isEmpty()) {
                EmptyTasksView()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(tasks) { task ->
                        TaskCard(
                            orderId = task.id,
                            customerName = task.customer,
                            itemsCount = task.items,
                            date = task.date,
                            status = task.status,
                            onAction = { /* Handle action */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyTasksView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(StatusShippedBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = StatusShipped,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "All done!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "No orders need action right now",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

data class Task(
    val id: String,
    val customer: String,
    val items: Int,
    val date: String,
    val status: OrderStatus
)
