package org.sujan.stockflow.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.sujan.stockflow.data.local.SessionManager
import org.sujan.stockflow.data.local.TokenStorage

object HttpClientFactory {
    fun create(tokenStorage: TokenStorage, sessionManager: SessionManager): HttpClient {
        val client = HttpClient {
            install(ContentNegotiation) { json(AppJson) }

            install(HttpTimeout) {
                connectTimeoutMillis = 10_000
                requestTimeoutMillis = 20_000
            }

            install(Logging) {
                level = LogLevel.ALL
                sanitizeHeader { it == HttpHeaders.Authorization }   // keeps the token out of logs
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status.isSuccess()) return@validateResponse

                    val body = runCatching {
                        AppJson.decodeFromString<ApiErrorBody>(response.bodyAsText())
                    }.getOrNull()

                    // 401 on a request that carried a token → session is over.
                    // Exception: INVALID_CREDENTIALS (e.g. wrong current password
                    // in change-password) is a normal error, not an expired session.
                    val sentToken = response.request.headers.contains(HttpHeaders.Authorization)
                    if (response.status == HttpStatusCode.Unauthorized &&
                        sentToken && body?.code != "INVALID_CREDENTIALS"
                    ) {
                        sessionManager.expire()
                    }

                    throw ApiException(response.status.value, body)
                }
            }

            defaultRequest {
                url("http://10.0.2.2:8080")
                contentType(ContentType.Application.Json)
            }
        }

        // Only job left for the interceptor: attach the token
        client.plugin(HttpSend).intercept { request ->
            val isLogin = request.url.encodedPath.endsWith("/auth/login")
            if (!isLogin) {
                tokenStorage.getToken()?.let { request.header(HttpHeaders.Authorization, "Bearer $it") }
            }
            execute(request)
        }

        return client
    }
}