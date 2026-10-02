package org.sujan.stockflow.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import org.sujan.stockflow.ui.components.OrderCard
import org.sujan.stockflow.ui.components.RoleBadge
import org.sujan.stockflow.ui.components.StatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    onViewAllOrders: () -> Unit = {},
    onSignedOut: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    if (state.isSignedOut) {
        onSignedOut()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                HeaderSection(state)
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                StatCard(
                    title = "Total Products",
                    value = state.totalProducts,
                    icon = Icons.Default.Inventory,
                    trend = "+4 this week"
                )
            }

            item {
                StatCard(
                    title = "Pending Orders",
                    value = state.pendingOrders,
                    icon = Icons.Default.Schedule,
                    trend = "Awaiting review"
                )
            }

            item {
                StatCard(
                    title = "Team Members",
                    value = state.teamMembers,
                    icon = Icons.Default.Group,
                    trend = "2 active now"
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT ORDERS",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = onViewAllOrders) {
                        Text(
                            text = "View all →",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            items(state.recentOrders) { order ->
                OrderCard(
                    orderId = order.id,
                    customerName = order.customer,
                    itemsCount = order.items,
                    date = order.date,
                    status = order.status
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun HeaderSection(state: DashboardState) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "WED, AUG 20", // Hardcoded for demo/screenshot match
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            state.user?.role?.let { RoleBadge(role = it) }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Good morning, ${state.user?.username ?: "User"}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "👋", fontSize = 24.sp)
        }
        
        Text(
            text = "Acme Supplies • ${state.user?.role?.name?.lowercase()?.replaceFirstChar { it.uppercase() }} view",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}
