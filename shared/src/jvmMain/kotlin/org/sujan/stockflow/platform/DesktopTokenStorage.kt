package org.sujan.stockflow.platform

import org.sujan.stockflow.data.local.TokenStorage
import java.util.prefs.Preferences

class DesktopTokenStorage : TokenStorage {
    private val prefs = Preferences.userRoot().node("stockflow")

    override suspend fun saveToken(token: String) {
        prefs.put("auth_token", token)
    }

    override suspend fun getToken(): String? {
        return prefs.get("auth_token", null)
    }

    override suspend fun clearToken() {
        prefs.remove("auth_token")
    }
}
