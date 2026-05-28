package com.nikhilgreenbot.fitznipins.domain.model

// ─────────────────────────────────────────────
// Sealed error hierarchy — presented to the user
// ─────────────────────────────────────────────

sealed interface UserFacingError {
    data object Offline : UserFacingError
    data object Timeout : UserFacingError
    data class Server(val code: Int, val message: String?) : UserFacingError
    data class Validation(val field: String, val reason: String) : UserFacingError
    data object NotFound : UserFacingError
    data object Unknown : UserFacingError
    data object PermissionDenied : UserFacingError

    fun userMessage(): String = when (this) {
        Offline         -> "You're offline. Showing cached data."
        Timeout         -> "Request timed out. Please try again."
        is Server       -> "Something went wrong on our end (${this.code})."
        is Validation   -> "Invalid ${this.field}: ${this.reason}"
        NotFound        -> "We couldn't find what you were looking for."
        Unknown         -> "An unexpected error occurred. Please try again."
        PermissionDenied -> "Camera permission is required to identify pins."
    }
}

// Thin wrapper so repositories never leak raw Throwable to ViewModels
sealed class FitzNiResult<out T> {
    data class Success<T>(val data: T) : FitzNiResult<T>()
    data class Error(val error: UserFacingError) : FitzNiResult<Nothing>()

    val isSuccess get() = this is Success
    val isError   get() = this is Error

    fun getOrNull(): T? = (this as? Success)?.data
    fun errorOrNull(): UserFacingError? = (this as? Error)?.error
}

fun <T> Result<T>.toFitzNiResult(): FitzNiResult<T> = fold(
    onSuccess = { FitzNiResult.Success(it) },
    onFailure = { throwable ->
        val mapped = when {
            throwable is java.net.UnknownHostException -> UserFacingError.Offline
            throwable is java.net.SocketTimeoutException -> UserFacingError.Timeout
            throwable.message?.contains("404") == true -> UserFacingError.NotFound
            else -> UserFacingError.Unknown
        }
        FitzNiResult.Error(mapped)
    }
)
