package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response

    companion object {
        const val NO_CONNECTION_ERROR_CODE = -1
        const val BAD_REQUEST_ERROR_CODE = 400
        const val OK_CODE = 200
        const val INTERNAL_SERVER_ERROR_CODE = 500
    }
}
