package org.sujan.stockflow.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sujan.stockflow.domain.model.User
import org.sujan.stockflow.domain.repository.AuthRepository

class SettingsViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val user = authRepository.getSessionUser()
            _state.update { it.copy(user = user, isSignedOut = user == null) }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.logout()
            _state.update { it.copy(isSignedOut = true) }
        }
    }
}

data class SettingsState(
    val user: User? = null,
    val isSignedOut: Boolean = false
)