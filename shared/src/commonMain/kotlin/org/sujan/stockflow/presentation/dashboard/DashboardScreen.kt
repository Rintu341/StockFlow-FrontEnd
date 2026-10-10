package org.sujan.stockflow.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import org.sujan.stockflow.ui.components.OrderCard
import org.sujan.stockflow.ui.components.RoleBadge
import org.sujan.stockflow.ui.components.StatCard
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.remember
import org.sujan.stockflow.presentation.common.FullScreenError
import org.sujan.stockflow.presentation.common.ObserveEvents

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    onViewAllOrders: () -> Unit = {},
    onSignedOut: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    ObserveEvents(viewModel.events, snackbarHostState)

    LaunchedEffect(state.isSignedOut) {
        if (state.isSignedOut) onSignedOut()
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background, snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            state.error != null && !state.hasLoaded ->
                FullScreenError(
                    error = state.error!!,
                    onRetry = {viewModel.retry()},
                    modifier = Modifier.padding(paddingValues)
                )

            else -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { viewModel.refresh() },
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            HeaderSection(state)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        /*// A refresh failed but we still have older data to show
                        state.error?.let { message ->
                            item {
                                Text(
                                    text = message,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }*/

                        item {
                            StatCard(
                                title = "Total Products",
                                value = state.totalProducts,
                                icon = Icons.Default.Inventory,
                                trend = "In your inventory"
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

                        state.teamMembers?.let { count ->
                            item {
                                StatCard(
                                    title = "Team Members",
                                    value = count,
                                    icon = Icons.Default.Group,
                                    trend = "In your business"
                                )
                            }
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

                        if (state.recentOrders.isEmpty()) {
                            item {
                                Text(
                                    text = "No orders yet.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            items(state.recentOrders, key = { it.id }) { order ->
                                OrderCard(
                                    orderId = order.id,
                                    customerName = order.customer,
                                    itemsCount = order.items,
                                    date = order.date,
                                    status = order.status
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(24.dp)) }
                    }
                }
            }
        }
    }
}

/*@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Try again") }
    }
}*/

@Composable
fun HeaderSection(state: DashboardState) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.dateLabel,                       // was hardcoded "WED, AUG 20"
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            state.user?.role?.let { RoleBadge(role = it) }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${state.greeting}, ${state.user?.username ?: "User"}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "👋", fontSize = 24.sp)
        }

        Text(
            text = "${state.user?.tenantName ?: ""} • ${state.user?.role?.name?.lowercase()?.replaceFirstChar { it.uppercase() }} view",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}