package org.sujan.stockflow.presentation.auth

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sujan.stockflow.domain.error.AppError
import org.sujan.stockflow.domain.error.asAppError
import org.sujan.stockflow.domain.repository.AuthRepository
import org.sujan.stockflow.domain.validation.PASSWORD_MAX_LENGTH
import org.sujan.stockflow.domain.validation.PasswordChecks
import org.sujan.stockflow.domain.validation.checkPassword
import org.sujan.stockflow.presentation.common.BaseViewModel
import org.sujan.stockflow.presentation.common.userMessage

private const val FIELD_CURRENT = "currentPassword"
private const val FIELD_NEW = "newPassword"

class ChangePasswordViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _state = MutableStateFlow(ChangePasswordState())
    val state = _state.asStateFlow()

    fun onCurrentChange(value: String) = _state.update {
        it.copy(
            currentPassword = value.take(PASSWORD_MAX_LENGTH),
            fieldErrors = it.fieldErrors - FIELD_CURRENT,
            formError = null
        )
    }

    fun onNewChange(value: String) = _state.update {
        it.copy(
            newPassword = value.take(PASSWORD_MAX_LENGTH),
            fieldErrors = it.fieldErrors - FIELD_NEW,
            formError = null
        )
    }

    fun onConfirmChange(value: String) = _state.update {
        it.copy(confirmPassword = value.take(PASSWORD_MAX_LENGTH), formError = null)
    }

    fun toggleCurrentVisible() = _state.update { it.copy(showCurrent = !it.showCurrent) }
    fun toggleNewVisible() = _state.update { it.copy(showNew = !it.showNew) }
    fun toggleConfirmVisible() = _state.update { it.copy(showConfirm = !it.showConfirm) }

    fun submit() {
        val s = _state.value
        if (!s.canSubmit) return

        _state.update { it.copy(isLoading = true, formError = null, fieldErrors = emptyMap()) }

        viewModelScope.launch {
            authRepository.changePassword(s.currentPassword, s.newPassword)
                .onSuccess {
                    _state.update {
                        ChangePasswordState(isSuccess = true)   // clears all typed passwords
                    }
                }
                .onFailure { e ->
                    val error = e.asAppError()
                    _state.update { current ->
                        when {
                            error is AppError.Business && error.code == "INVALID_CURRENT_PASSWORD" ->
                                current.copy(
                                    isLoading = false,
                                    fieldErrors = mapOf(FIELD_CURRENT to error.userMessage())
                                )
                            error is AppError.Business && error.code == "PASSWORD_REUSED" ->
                                current.copy(
                                    isLoading = false,
                                    fieldErrors = mapOf(FIELD_NEW to error.userMessage())
                                )
                            error is AppError.Validation && error.fieldErrors.isNotEmpty() ->
                                current.copy(isLoading = false, fieldErrors = error.fieldErrors)
                            else ->
                                current.copy(isLoading = false, formError = error.userMessage())
                        }
                    }
                }
        }
    }

    fun signOut(onDone: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onDone()
        }
    }
}

data class ChangePasswordState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val showCurrent: Boolean = false,
    val showNew: Boolean = false,
    val showConfirm: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val formError: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
) {
    val checks: PasswordChecks get() = checkPassword(newPassword)
    val confirmMismatch: Boolean
        get() = confirmPassword.isNotEmpty() && confirmPassword != newPassword
    val sameAsCurrent: Boolean
        get() = newPassword.isNotEmpty() && newPassword == currentPassword
    val canSubmit: Boolean
        get() = currentPassword.isNotBlank() &&
                checks.allMet &&
                confirmPassword == newPassword &&
                !sameAsCurrent &&
                !isLoading
}