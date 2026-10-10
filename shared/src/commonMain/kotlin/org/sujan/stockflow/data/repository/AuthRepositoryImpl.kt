package org.sujan.stockflow.data.repository

import org.sujan.stockflow.data.local.SessionStorage
import org.sujan.stockflow.data.local.TokenStorage
import org.sujan.stockflow.data.remote.AuthApi
import org.sujan.stockflow.data.remote.dto.ChangePasswordRequestDto
import org.sujan.stockflow.data.remote.dto.LoginRequestDto
import org.sujan.stockflow.data.remote.safeApiCall
import org.sujan.stockflow.data.util.currentTimeMillis
import org.sujan.stockflow.domain.model.Role
import org.sujan.stockflow.domain.model.User
import org.sujan.stockflow.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    /*override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequestDto(email, password))

            val user = User(
                id = response.id,
                email = response.email,
                username = response.username,
                role = Role.valueOf(response.role),
                tenantId = response.tenantId,
                tenantName = response.tenantName,
                mustChangePassword = response.mustChangePassword
            )

            tokenStorage.saveToken(response.token)
            sessionStorage.saveUser(user)
            sessionStorage.saveTokenExpiry(currentTimeMillis() + response.expiresIn)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }*/
    override suspend fun login(email: String, password: String): Result<User> = safeApiCall {
        val response = api.login(LoginRequestDto(email, password))
        val user = User(
            id = response.id,
            email = response.email,
            username = response.username,
            role = Role.valueOf(response.role),
            tenantId = response.tenantId,
            tenantName = response.tenantName,
            mustChangePassword = response.mustChangePassword
        )
        tokenStorage.saveToken(response.token)
        sessionStorage.saveUser(user)
        sessionStorage.saveTokenExpiry(currentTimeMillis() + response.expiresIn)
        user
    }

    override suspend fun logout() {
        tokenStorage.clearToken()
        sessionStorage.clear()
    }

    override suspend fun getSessionUser(): User? {
        val expiresAt = sessionStorage.getTokenExpiry()
        val hasToken = tokenStorage.getToken() != null

        if (expiresAt == null || !hasToken || currentTimeMillis() >= expiresAt) {
            logout()
            return null
        }
        return sessionStorage.getUser()
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Result<Unit> = safeApiCall {
        api.changePassword(ChangePasswordRequestDto(currentPassword, newPassword))
        clearPasswordChangeFlag()
    }
    private suspend fun clearPasswordChangeFlag() {
        sessionStorage.getUser()?.let {
            sessionStorage.saveUser(it.copy(mustChangePassword = false))
        }
    }
}