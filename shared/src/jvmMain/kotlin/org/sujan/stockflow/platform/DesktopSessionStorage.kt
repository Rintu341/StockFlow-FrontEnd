package org.sujan.stockflow.platform

import java.util.prefs.Preferences
import org.sujan.stockflow.data.local.SessionStorage
import org.sujan.stockflow.domain.model.Role
import org.sujan.stockflow.domain.model.User

class DesktopSessionStorage : SessionStorage {
    private val prefs = Preferences.userRoot().node("stockflow")

    override suspend fun saveUser(user: User) {
        prefs.putLong(KEY_ID, user.id)
        prefs.put(KEY_EMAIL, user.email)
        prefs.put(KEY_USERNAME, user.username)
        prefs.put(KEY_ROLE, user.role.name)
        prefs.put(KEY_TENANT, user.tenantId)
        prefs.flush()
    }

    override suspend fun getUser(): User? {
        if (prefs.get(KEY_ID, null) == null) return null

        val email = prefs.get(KEY_EMAIL, null) ?: return null
        val username = prefs.get(KEY_USERNAME, null) ?: return null
        val tenantId = prefs.get(KEY_TENANT, null) ?: return null
        val role = prefs.get(KEY_ROLE, null)
            ?.let { runCatching { Role.valueOf(it) }.getOrNull() }
            ?: return null

        return User(
            id = prefs.getLong(KEY_ID, 0L),
            email = email,
            username = username,
            role = role,
            tenantId = tenantId
        )
    }

    override suspend fun clearUser() {
        listOf(KEY_ID, KEY_EMAIL, KEY_USERNAME, KEY_ROLE, KEY_TENANT)
            .forEach { prefs.remove(it) }
        prefs.flush()
    }

    private companion object {
        const val KEY_ID = "user_id"
        const val KEY_EMAIL = "user_email"
        const val KEY_USERNAME = "user_username"
        const val KEY_ROLE = "user_role"
        const val KEY_TENANT = "user_tenant"
    }
}