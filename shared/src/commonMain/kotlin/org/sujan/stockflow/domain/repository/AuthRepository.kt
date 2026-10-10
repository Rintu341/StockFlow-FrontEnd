package org.sujan.stockflow.domain.repository

import org.sujan.stockflow.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    suspend fun getSessionUser(): User?
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>

}
