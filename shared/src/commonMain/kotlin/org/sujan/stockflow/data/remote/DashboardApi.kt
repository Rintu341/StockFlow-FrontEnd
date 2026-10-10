package org.sujan.stockflow.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import org.sujan.stockflow.data.remote.dto.DashboardSummaryDto

//class ApiException(val code: Int, message: String) : Exception(message)

class DashboardApi(private val client: HttpClient) {
    /*suspend fun getSummary(): DashboardSummaryDto {
        val response = client.get("/api/dashboard/summary")
        if (!response.status.isSuccess()) {
            throw ApiException(response.status.value, "Request failed: ${response.status.value}")
        }
        return response.body()
    }*/
    suspend fun getSummary(): DashboardSummaryDto =
        client.get("/api/dashboard/summary").body()
}