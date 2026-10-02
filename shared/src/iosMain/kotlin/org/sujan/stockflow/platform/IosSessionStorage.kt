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
        defaults.setObject(user.tenantId, KEY_TENANT)
    }

    override suspend fun getUser(): User? {
        val id = defaults.stringForKey(KEY_ID)?.toLongOrNull() ?: return null
        val email = defaults.stringForKey(KEY_EMAIL) ?: return null
        val username = defaults.stringForKey(KEY_USERNAME) ?: return null
        val tenantId = defaults.stringForKey(KEY_TENANT) ?: return null
        val role = defaults.stringForKey(KEY_ROLE)
            ?.let { runCatching { Role.valueOf(it) }.getOrNull() }
            ?: return null

        return User(id, email, username, role, tenantId)
    }

    override suspend fun clearUser() {
        listOf(KEY_ID, KEY_EMAIL, KEY_USERNAME, KEY_ROLE, KEY_TENANT)
            .forEach { defaults.removeObjectForKey(it) }
    }

    private companion object {
        const val KEY_ID = "user_id"
        const val KEY_EMAIL = "user_email"
        const val KEY_USERNAME = "user_username"
        const val KEY_ROLE = "user_role"
        const val KEY_TENANT = "user_tenant"
    }
}