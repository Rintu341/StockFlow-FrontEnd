package org.sujan.stockflow.data.repository

import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Instant
import org.sujan.stockflow.data.remote.DashboardApi
import org.sujan.stockflow.data.remote.dto.DashboardSummaryDto
import org.sujan.stockflow.data.remote.dto.RecentOrderDto
import org.sujan.stockflow.data.remote.safeApiCall
import org.sujan.stockflow.domain.model.DashboardSummary
import org.sujan.stockflow.domain.model.OrderStatus
import org.sujan.stockflow.domain.model.RecentOrderSummary
import org.sujan.stockflow.domain.repository.DashboardRepository
import kotlin.time.ExperimentalTime

class DashboardRepositoryImpl(
    private val api: DashboardApi
) : DashboardRepository {

    override suspend fun getSummary(): Result<DashboardSummary> =
        safeApiCall { api.getSummary().toDomain() }
}

private fun DashboardSummaryDto.toDomain() = DashboardSummary(
    totalProducts = totalProducts,
    pendingOrders = pendingOrders,
    teamMembers = teamMembers,
    recentOrders = recentOrders.map { it.toDomain() }
)

@OptIn(ExperimentalTime::class)
private fun RecentOrderDto.toDomain() = RecentOrderSummary(
    id = id,
    customerName = customerName,
    itemsCount = itemsCount,
    createdAt = Instant.parse(createdAt),
    // An unknown status from a newer backend shouldn't crash the whole dashboard
    status = OrderStatus.entries.firstOrNull { it.name == status } ?: OrderStatus.PENDING
)