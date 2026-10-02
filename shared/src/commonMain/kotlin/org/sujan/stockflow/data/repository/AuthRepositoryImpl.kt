package org.sujan.stockflow.data.repository

import org.sujan.stockflow.data.local.SessionStorage
import org.sujan.stockflow.data.local.TokenStorage
import org.sujan.stockflow.data.remote.AuthApi
import org.sujan.stockflow.data.remote.dto.LoginRequestDto
import org.sujan.stockflow.domain.model.Role
import org.sujan.stockflow.domain.model.User
import org.sujan.stockflow.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequestDto(email, password))
            tokenStorage.saveToken(response.token)
            
            val user = User(
                id = response.id,
                email = response.email,
                username = response.username,
                role = Role.valueOf(response.role),
                tenantId = response.tenantId
            )
            sessionStorage.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        tokenStorage.clearToken()
        sessionStorage.clearUser()
    }
    override suspend fun getSessionUser(): User? = sessionStorage.getUser()

}
