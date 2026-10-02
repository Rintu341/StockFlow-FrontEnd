package org.sujan.stockflow.data.local

import org.sujan.stockflow.domain.model.User

interface SessionStorage {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    suspend fun clearUser()
}