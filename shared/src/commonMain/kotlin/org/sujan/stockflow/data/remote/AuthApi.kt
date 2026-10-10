package org.sujan.stockflow.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import org.sujan.stockflow.data.remote.dto.AuthResponseDto
import org.sujan.stockflow.data.remote.dto.ChangePasswordRequestDto
import org.sujan.stockflow.data.remote.dto.LoginRequestDto

class AuthApi(private val client: HttpClient) {
    suspend fun login(request: LoginRequestDto): AuthResponseDto {
        return client.post("/api/auth/login") {
            setBody(request)
        }.body()
    }
    suspend fun changePassword(request: ChangePasswordRequestDto) {
        client.put("/api/auth/change-password") {
            setBody(request)
        }
    }

}
