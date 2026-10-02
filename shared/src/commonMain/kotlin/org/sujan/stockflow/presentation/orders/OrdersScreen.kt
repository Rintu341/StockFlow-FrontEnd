package org.sujan.stockflow.presentation.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.sujan.stockflow.domain.model.OrderStatus
import org.sujan.stockflow.ui.components.OrderCard

@Composable
fun OrdersScreen(
    onCreateOrder: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Pending", "Approved", "Shipped", "Rejected")

    val allOrders = listOf(
        OrderData("ORD-1042", "Priya Sharma", 3, "Aug 18", OrderStatus.PENDING),
        OrderData("ORD-1041", "Marco Bellini", 1, "Aug 17", OrderStatus.APPROVED),
        OrderData("ORD-1040", "Yuki Tanaka", 5, "Aug 16", OrderStatus.SHIPPED),
        OrderData("ORD-1039", "Amara Osei", 2, "Aug 15", OrderStatus.REJECTED),
        OrderData("ORD-1038", "Lena Fischer", 4, "Aug 14", OrderStatus.APPROVED),
        OrderData("ORD-1037", "Carlos Rivera", 2, "Aug 13", OrderStatus.PENDING)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateOrder,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(100.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Order")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Orders",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${allOrders.size} total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = null,
                        shape = RoundedCornerShape(100.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                val filteredOrders = if (selectedFilter == "All") {
                    allOrders
                } else {
                    allOrders.filter { it.status.name.lowercase() == selectedFilter.lowercase() }
                }
                
                items(filteredOrders) { order ->
                    OrderCard(
                        orderId = order.id,
                        customerName = order.customer,
                        itemsCount = order.items,
                        date = order.date,
                        status = order.status
                    )
                }
            }
        }
    }
}

data class OrderData(
    val id: String,
    val customer: String,
    val items: Int,
    val date: String,
    val status: OrderStatus
)
