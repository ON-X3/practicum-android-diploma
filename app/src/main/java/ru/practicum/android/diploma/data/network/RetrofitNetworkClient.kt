package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.FilterAreasRequest
import ru.practicum.android.diploma.data.dto.FilterAreasResponse
import ru.practicum.android.diploma.data.dto.FilterIndustriesRequest
import ru.practicum.android.diploma.data.dto.FilterIndustriesResponse
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancySearchRequest

class RetrofitNetworkClient(
    private val searchApi: SearchApi,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NetworkClient.NO_CONNECTION_ERROR_CODE }
        }
        return when (dto) {
            is VacancySearchRequest -> doVacanciesRequest(dto)
            is FilterAreasRequest -> doFilterAreasRequest()
            is FilterIndustriesRequest -> doFilterIndustriesRequest()
            else -> Response().apply { resultCode = NetworkClient.BAD_REQUEST_ERROR_CODE }
        }
    }

    private suspend fun doVacanciesRequest(dto: VacancySearchRequest): Response {
        try {
            val resp = searchApi.search(
                expression = dto.expression,
                page = dto.page,
                area = dto.area,
                industry = dto.industry,
                salary = dto.salary,
                onlyWithSalary = dto.onlyWithSalary,
            )
            return resp.apply { resultCode = NetworkClient.OK_CODE }
        } catch (e: HttpException) {
            return Response().apply { resultCode = e.code() }
        }
    }

    private suspend fun doFilterAreasRequest(): Response {
        try {
            val areas = searchApi.getFilterAreas()
            return FilterAreasResponse(areas).apply { resultCode = NetworkClient.OK_CODE }
        } catch (e: HttpException) {
            return Response().apply { resultCode = e.code() }
        }
    }

    private suspend fun doFilterIndustriesRequest(): Response {
        try {
            val industries = searchApi.getFilterIndustries()
            return FilterIndustriesResponse(industries).apply { resultCode = NetworkClient.OK_CODE }
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

}
