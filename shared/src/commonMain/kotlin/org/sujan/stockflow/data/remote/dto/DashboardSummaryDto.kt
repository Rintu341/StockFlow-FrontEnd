package org.sujan.stockflow.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DashboardSummaryDto(
    val totalProducts: Long,
    val pendingOrders: Long,
    val teamMembers: Long? = null,
    val recentOrders: List<RecentOrderDto> = emptyList()
)

@Serializable
data class RecentOrderDto(
    val id: Long,
    val customerName: String? = null,
    val itemsCount: Int,
    val createdAt: String,
    val status: String
)