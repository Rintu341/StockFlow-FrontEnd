package org.sujan.stockflow.platform

import org.sujan.stockflow.data.local.TokenStorage
import platform.Foundation.NSUserDefaults

class IosTokenStorage : TokenStorage {
    private val defaults = NSUserDefaults.standardUserDefaults

    override suspend fun saveToken(token: String) {
        defaults.setObject(token, "auth_token")
    }

    override suspend fun getToken(): String? {
        return defaults.stringForKey("auth_token")
    }

    override suspend fun clearToken() {
        defaults.removeObjectForKey("auth_token")
    }
}
