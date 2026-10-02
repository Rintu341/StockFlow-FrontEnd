package org.sujan.stockflow.platform

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.sujan.stockflow.data.local.TokenStorage

class AndroidTokenStorage(context: Context) : TokenStorage {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "stockflow_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun saveToken(token: String) {
        sharedPrefs.edit().putString("auth_token", token).apply()
    }

    override suspend fun getToken(): String? {
        return sharedPrefs.getString("auth_token", null)
    }

    override suspend fun clearToken() {
        sharedPrefs.edit().remove("auth_token").apply()
    }
}
