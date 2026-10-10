package org.sujan.stockflow.domain.repository

import org.sujan.stockflow.domain.model.DashboardSummary

interface DashboardRepository {
    suspend fun getSummary(): Result<DashboardSummary>
}