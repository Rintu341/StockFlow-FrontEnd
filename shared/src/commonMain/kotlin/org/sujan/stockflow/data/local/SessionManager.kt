package org.sujan.stockflow.data.local

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SessionManager(
    private val tokenStorage: TokenStorage,
    private val sessionStorage: SessionStorage
) {
    private val _expired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val expired: SharedFlow<Unit> = _expired.asSharedFlow()

    suspend fun expire() {
        tokenStorage.clearToken()
        sessionStorage.clear()
        _expired.tryEmit(Unit)
    }
}