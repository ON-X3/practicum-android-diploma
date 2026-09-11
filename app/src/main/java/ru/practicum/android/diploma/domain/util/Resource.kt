package ru.practicum.android.diploma.domain.util

sealed class Resource<T>(val data: T? = null, val errorCode: ErrorCode? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorCode: ErrorCode) : Resource<T>(errorCode = errorCode)
}

enum class ErrorCode(val code: Int) {
    INTERNAL_SERVER_ERROR(500),
    NOT_FOUND(404),
    NO_INTERNET_CONNECTION(-1),
    LOCAL_DB_ERROR(1)
}
