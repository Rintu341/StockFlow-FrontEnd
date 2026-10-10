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
        prefs.put(KEY_TENANT_ID, user.tenantId)
        prefs.put(KEY_TENANT_NAME, user.tenantName)
        prefs.putBoolean(KEY_MUST_CHANGE, user.mustChangePassword)
        prefs.flush()
    }

    override suspend fun getUser(): User? {
        if (prefs.get(KEY_ID, null) == null) return null
        val role = prefs.get(KEY_ROLE, null)
            ?.let { runCatching { Role.valueOf(it) }.getOrNull() } ?: return null

        return User(
            id = prefs.getLong(KEY_ID, 0L),
            email = prefs.get(KEY_EMAIL, null) ?: return null,
            username = prefs.get(KEY_USERNAME, null) ?: return null,
            role = role,
            tenantId = prefs.get(KEY_TENANT_ID, null) ?: return null,
            tenantName = prefs.get(KEY_TENANT_NAME, null) ?: return null,
            mustChangePassword = prefs.getBoolean(KEY_MUST_CHANGE, false)
        )
    }

    override suspend fun saveTokenExpiry(expiresAtMillis: Long) {
        prefs.putLong(KEY_EXPIRY, expiresAtMillis)
        prefs.flush()
    }

    override suspend fun getTokenExpiry(): Long? =
        if (prefs.get(KEY_EXPIRY, null) != null) prefs.getLong(KEY_EXPIRY, 0L) else null

    override suspend fun clear() {
        listOf(
            KEY_ID, KEY_EMAIL, KEY_USERNAME, KEY_ROLE,
            KEY_TENANT_ID, KEY_TENANT_NAME, KEY_MUST_CHANGE, KEY_EXPIRY
        ).forEach { prefs.remove(it) }
        prefs.flush()
    }

    private companion object {
        const val KEY_ID = "user_id"
        const val KEY_EMAIL = "user_email"
        const val KEY_USERNAME = "user_username"
        const val KEY_ROLE = "user_role"
        const val KEY_TENANT_ID = "user_tenant_id"
        const val KEY_TENANT_NAME = "user_tenant_name"
        const val KEY_MUST_CHANGE = "user_must_change_password"
        const val KEY_EXPIRY = "token_expires_at"
    }
}