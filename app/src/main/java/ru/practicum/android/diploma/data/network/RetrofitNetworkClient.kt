package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancySearchRequest

class RetrofitNetworkClient(
    private val searchApi: SearchApi,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_CONNECTION_ERROR_CODE }
        }
        return when (dto) {
            is VacancySearchRequest -> doVacanciesRequest(dto)
            else -> Response().apply { resultCode = BAD_REQUEST_ERROR_CODE }
        }
    }

    private suspend fun doVacanciesRequest(dto: VacancySearchRequest): Response {
        try {
            val resp = searchApi.search(dto.expression)
            return resp.apply { resultCode = OK_CODE }
        } catch (e: HttpException) {
            return Response().apply { resultCode = e.code() }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val capabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    companion object {
        const val NO_CONNECTION_ERROR_CODE = -1
        const val BAD_REQUEST_ERROR_CODE = 400
        const val OK_CODE = 200
    }

}
