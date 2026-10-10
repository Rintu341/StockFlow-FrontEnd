package org.sujan.stockflow.data.remote

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.serialization.ContentConvertException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.io.IOException      // Ktor 2.x: io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import org.sujan.stockflow.domain.error.AppError

fun Throwable.toAppError(): AppError = when (this) {
    is AppError -> this
    is ApiException -> toAppError()
    is HttpRequestTimeoutException,
    is ConnectTimeoutException,
    is SocketTimeoutException -> AppError.Timeout(this)
    is ContentConvertException,
    is SerializationException -> AppError.Parsing(this)
    is IOException -> AppError.NoConnection(this)
    else -> AppError.Unknown(this)
}

private fun ApiException.toAppError(): AppError {
    val message = error?.message
    return when {
        error?.code == "INVALID_CREDENTIALS" -> AppError.InvalidCredentials(message)
        error?.code == "PASSWORD_CHANGE_REQUIRED" -> AppError.PasswordChangeRequired(message)
        status == 401 -> AppError.SessionExpired()
        status == 403 -> AppError.Forbidden(message)
        status == 404 -> AppError.NotFound(message)
        error?.code == "VALIDATION_FAILED" ->
            AppError.Validation(message, error.fieldErrors)
        status in 400..499 -> AppError.Business(error?.code, message)
        status in 500..599 -> AppError.Server(status)
        else -> AppError.Unknown(this)
    }
}

/** The single choke point: every repository call goes through this. */
suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e                                   // never swallow coroutine cancellation
    } catch (e: Exception) {
        Result.failure(e.toAppError())            // add crash reporting / logging here later
    }