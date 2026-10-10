package org.sujan.stockflow.platform

import org.sujan.stockflow.data.local.SessionStorage
import org.sujan.stockflow.domain.model.Role
import org.sujan.stockflow.domain.model.User
import platform.Foundation.NSUserDefaults

class IosSessionStorage : SessionStorage {
    private val defaults = NSUserDefaults.standardUserDefaults

    override suspend fun saveUser(user: User) {
        defaults.setObject(user.id.toString(), KEY_ID)
        defaults.setObject(user.email, KEY_EMAIL)
        defaults.setObject(user.username, KEY_USERNAME)
        defaults.setObject(user.role.name, KEY_ROLE)
        defaults.setObject(user.tenantId, KEY_TENANT_ID)
        defaults.setObject(user.tenantName, KEY_TENANT_NAME)
        defaults.setBool(user.mustChangePassword, KEY_MUST_CHANGE)
    }

    override suspend fun getUser(): User? {
        val id = defaults.stringForKey(KEY_ID)?.toLongOrNull() ?: return null
        val role = defaults.stringForKey(KEY_ROLE)
            ?.let { runCatching { Role.valueOf(it) }.getOrNull() } ?: return null

        return User(
            id = id,
            email = defaults.stringForKey(KEY_EMAIL) ?: return null,
            username = defaults.stringForKey(KEY_USERNAME) ?: return null,
            role = role,
            tenantId = defaults.stringForKey(KEY_TENANT_ID) ?: return null,
            tenantName = defaults.stringForKey(KEY_TENANT_NAME) ?: return null,
            mustChangePassword = defaults.boolForKey(KEY_MUST_CHANGE)
        )
    }

    override suspend fun saveTokenExpiry(expiresAtMillis: Long) {
        defaults.setObject(expiresAtMillis.toString(), KEY_EXPIRY)
    }

    override suspend fun getTokenExpiry(): Long? =
        defaults.stringForKey(KEY_EXPIRY)?.toLongOrNull()

    override suspend fun clear() {
        listOf(
            KEY_ID, KEY_EMAIL, KEY_USERNAME, KEY_ROLE,
            KEY_TENANT_ID, KEY_TENANT_NAME, KEY_MUST_CHANGE, KEY_EXPIRY
        ).forEach { defaults.removeObjectForKey(it) }
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