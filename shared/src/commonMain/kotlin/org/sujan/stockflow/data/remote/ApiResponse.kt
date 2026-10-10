package org.sujan.stockflow.data.remote

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

//class ApiException(val code: Int, message: String) : Exception(message)

/*
suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
    if (!status.isSuccess()) {
        throw ApiException(status.value, "Request failed with code ${status.value}")
    }
    return body()
}*/
