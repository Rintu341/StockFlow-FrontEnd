package org.sujan.stockflow.data.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val AppJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

@Serializable
data class ApiErrorBody(
    val status: Int? = null,
    val code: String? = null,
    val message: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

class ApiException(
    val status: Int,
    val error: ApiErrorBody?
) : Exception("HTTP $status ${error?.code.orEmpty()}")