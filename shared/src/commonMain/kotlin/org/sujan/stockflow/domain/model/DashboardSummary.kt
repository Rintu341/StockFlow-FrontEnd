package org.sujan.stockflow.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class DashboardSummary(
    val totalProducts: Long,
    val pendingOrders: Long,
    val teamMembers: Long?,          // null → caller isn't an admin
    val recentOrders: List<RecentOrderSummary>
)

data class RecentOrderSummary @OptIn(ExperimentalTime::class) constructor(
    val id: Long,
    val customerName: String?,
    val itemsCount: Int,
    val createdAt: Instant,
    val status: OrderStatus
)