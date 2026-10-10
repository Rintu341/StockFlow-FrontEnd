package org.sujan.stockflow.presentation.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.sujan.stockflow.domain.error.AppError
import org.sujan.stockflow.domain.error.asAppError

sealed interface UiEvent {
    data class ShowMessage(val text: String) : UiEvent
}

abstract class BaseViewModel : ViewModel() {
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    protected fun showMessage(text: String) {
        _events.trySend(UiEvent.ShowMessage(text))
    }

    /** For failures of actions (approve, delete, refresh...). */
    protected fun showError(error: Throwable) {
        val appError = error.asAppError()
        if (appError is AppError.SessionExpired) return   // handled globally: user is sent to Login
        showMessage(appError.userMessage())
    }
}