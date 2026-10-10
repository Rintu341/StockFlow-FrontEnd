package org.sujan.stockflow.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sujan.stockflow.data.local.SessionManager
import org.sujan.stockflow.domain.repository.AuthRepository

class AppStartViewModel(
    private val authRepository: AuthRepository,
    sessionManager: SessionManager
) : ViewModel() {

    val sessionExpired = sessionManager.expired

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val user = authRepository.getSessionUser()
            _startDestination.value = when {
                user == null -> Screen.Login.route
                user.mustChangePassword -> Screen.ForceChangePassword.route
                else -> Screen.Main.route
            }
        }
    }
}