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
            .putString(KEY_TENANT_ID, user.tenantId)
            .putString(KEY_TENANT_NAME, user.tenantName)
            .putBoolean(KEY_MUST_CHANGE, user.mustChangePassword)
            .apply()
    }

    override suspend fun getUser(): User? {
        if (!prefs.contains(KEY_ID)) return null
        val role = prefs.getString(KEY_ROLE, null)
            ?.let { runCatching { Role.valueOf(it) }.getOrNull() } ?: return null

        return User(
            id = prefs.getLong(KEY_ID, 0L),
            email = prefs.getString(KEY_EMAIL, null) ?: return null,
            username = prefs.getString(KEY_USERNAME, null) ?: return null,
            role = role,
            tenantId = prefs.getString(KEY_TENANT_ID, null) ?: return null,
            tenantName = prefs.getString(KEY_TENANT_NAME, null) ?: return null,
            mustChangePassword = prefs.getBoolean(KEY_MUST_CHANGE, false)
        )
    }

    override suspend fun saveTokenExpiry(expiresAtMillis: Long) {
        prefs.edit().putLong(KEY_EXPIRY, expiresAtMillis).apply()
    }

    override suspend fun getTokenExpiry(): Long? =
        if (prefs.contains(KEY_EXPIRY)) prefs.getLong(KEY_EXPIRY, 0L) else null

    override suspend fun clear() {
        prefs.edit().clear().apply()
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