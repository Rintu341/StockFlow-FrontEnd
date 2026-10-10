package org.sujan.stockflow.domain.error

sealed class AppError(
    val serverMessage: String? = null,
    cause: Throwable? = null
) : Exception(serverMessage, cause) {

    // Connectivity
    class NoConnection(cause: Throwable? = null) : AppError(cause = cause)
    class Timeout(cause: Throwable? = null) : AppError(cause = cause)

    // Auth
    class SessionExpired : AppError()
    class InvalidCredentials(serverMessage: String? = null) : AppError(serverMessage)
    class PasswordChangeRequired(serverMessage: String? = null) : AppError(serverMessage)
    class Forbidden(serverMessage: String? = null) : AppError(serverMessage)

    // Request problems
    class Validation(
        serverMessage: String? = null,
        val fieldErrors: Map<String, String> = emptyMap()
    ) : AppError(serverMessage)
    class NotFound(serverMessage: String? = null) : AppError(serverMessage)
    class Business(val code: String?, serverMessage: String?) : AppError(serverMessage)

    // Failures
    class Server(val httpStatus: Int) : AppError()
    class Parsing(cause: Throwable) : AppError(cause = cause)
    class Unknown(cause: Throwable? = null) : AppError(cause = cause)

    val isRetryable: Boolean
        get() = this is NoConnection || this is Timeout || this is Server || this is Unknown
}

fun Throwable.asAppError(): AppError = this as? AppError ?: AppError.Unknown(this)