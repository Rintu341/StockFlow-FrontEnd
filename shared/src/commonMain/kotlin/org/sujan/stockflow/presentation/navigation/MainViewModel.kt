package org.sujan.stockflow.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sujan.stockflow.domain.model.User
import org.sujan.stockflow.domain.repository.AuthRepository

class MainViewModel(
    authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val user = authRepository.getSessionUser()
            _state.value = if (user == null) MainState(isSignedOut = true) else MainState(user = user)
        }
    }
}

data class MainState(
    val user: User? = null,
    val isSignedOut: Boolean = false
)