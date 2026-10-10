package org.sujan.stockflow.data.local

import org.sujan.stockflow.domain.model.User

interface SessionStorage {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    suspend fun saveTokenExpiry(expiresAtMillis: Long)
    suspend fun getTokenExpiry(): Long?
    suspend fun clear()
}