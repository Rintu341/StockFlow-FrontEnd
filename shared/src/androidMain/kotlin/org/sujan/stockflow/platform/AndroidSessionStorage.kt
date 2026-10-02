package org.sujan.stockflow.platform

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.sujan.stockflow.data.local.SessionStorage
import org.sujan.stockflow.domain.model.Role
import org.sujan.stockflow.domain.model.User

class AndroidSessionStorage(context: Context) : SessionStorage {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "stockflow_session_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun saveUser(user: User) {
        prefs.edit()
            .putLong(KEY_ID, user.id)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_USERNAME, user.username)
            .putString(KEY_ROLE, user.role.name)
            .putString(KEY_TENANT, user.tenantId)
            .apply()
    }

    override suspend fun getUser(): User? {
        if (!prefs.contains(KEY_ID)) return null

        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        val username = prefs.getString(KEY_USERNAME, null) ?: return null
        val tenantId = prefs.getString(KEY_TENANT, null) ?: return null
        val role = prefs.getString(KEY_ROLE, null)
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
        prefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_ID = "user_id"
        const val KEY_EMAIL = "user_email"
        const val KEY_USERNAME = "user_username"
        const val KEY_ROLE = "user_role"
        const val KEY_TENANT = "user_tenant"
    }
}