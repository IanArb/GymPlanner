package com.ianarbuckle.gymplanner.common

sealed interface ApiResult<out T> {
    data class Success<T>(val value: T) : ApiResult<T>

    data class Failure(val error: Throwable) : ApiResult<Nothing>
}

val ApiResult<*>.isSuccess: Boolean
    get() = this is ApiResult.Success

val ApiResult<*>.isFailure: Boolean
    get() = this is ApiResult.Failure

fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.value

fun ApiResult<*>.exceptionOrNull(): Throwable? = (this as? ApiResult.Failure)?.error

fun <T> ApiResult<T>.getOrThrow(): T =
    when (this) {
        is ApiResult.Success -> value
        is ApiResult.Failure -> throw error
    }

inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> =
    when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(value))
        is ApiResult.Failure -> this
    }

inline fun <T, R> ApiResult<T>.fold(onSuccess: (T) -> R, onFailure: (Throwable) -> R): R =
    when (this) {
        is ApiResult.Success -> onSuccess(value)
        is ApiResult.Failure -> onFailure(error)
    }
