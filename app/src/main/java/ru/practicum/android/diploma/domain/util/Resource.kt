package ru.practicum.android.diploma.domain.util

sealed class Resource<T>(val data: T? = null, val errorCode: ErrorCode? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorCode: ErrorCode) : Resource<T>(errorCode = errorCode)
}

enum class ErrorCode {
    INTERNAL_SERVER_ERROR,
    NOT_FOUND,
    NO_INTERNET_CONNECTION,
    LOCAL_DB_ERROR
}
