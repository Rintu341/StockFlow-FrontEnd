package org.sujan.stockflow.presentation.common

import org.sujan.stockflow.domain.error.AppError

data class UiError(
    val message: String,
    val fieldErrors: Map<String, String> = emptyMap(),
    val canRetry: Boolean = false
)

fun AppError.toUiError() = UiError(
    message = userMessage(),
    fieldErrors = (this as? AppError.Validation)?.fieldErrors.orEmpty(),
    canRetry = isRetryable
)

fun AppError.userMessage(): String = when (this) {
    is AppError.NoConnection -> "No internet connection. Check your network and try again."
    is AppError.Timeout -> "The server is taking too long to respond. Please try again."
    is AppError.SessionExpired -> "Your session has expired. Please log in again."
    is AppError.InvalidCredentials -> serverMessage ?: "Incorrect email or password."
    is AppError.PasswordChangeRequired -> serverMessage ?: "Please change your temporary password to continue."
    is AppError.Forbidden -> "You don't have permission to do that."
    is AppError.Validation -> serverMessage ?: "Please check the information you entered."
    is AppError.NotFound -> serverMessage ?: "We couldn't find what you were looking for."
    is AppError.Business -> serverMessage ?: "That action isn't possible right now."
    is AppError.Server -> "Something went wrong on our side. Please try again in a moment."
    is AppError.Parsing -> "We received an unexpected response. Please try again."
    is AppError.Unknown -> "Something went wrong. Please try again."
}