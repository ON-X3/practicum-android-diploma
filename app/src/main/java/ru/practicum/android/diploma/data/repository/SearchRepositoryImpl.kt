package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.converters.VacancyDetailConverter
import ru.practicum.android.diploma.data.converters.VacancyDomainConverter
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.BAD_REQUEST_ERROR_CODE
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.NO_CONNECTION_ERROR_CODE
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient.Companion.OK_CODE
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyDomainConverter: VacancyDomainConverter,
    private val vacancyDetailConverter: VacancyDetailConverter
) : SearchRepository {
    override suspend fun searchVacancies(
        expression: String,
        filter: FilterParameters?,
        page: Int
    ): Resource<VacanciesSearchResult> {
        val request = VacancySearchRequest(
            expression = expression,
            page = page,
            area = filter?.area,
            industry = filter?.industry,
            salary = filter?.salary,
            onlyWithSalary = filter?.onlyWithSalary,
        )

        val response = networkClient.doRequest(request)

        return when (response.resultCode) {
            OK_CODE -> Resource.Success(vacancyDomainConverter.toDomain(response as VacancySearchResponse))
            NO_CONNECTION_ERROR_CODE -> Resource.Error(ErrorCode.NO_INTERNET_CONNECTION)
            BAD_REQUEST_ERROR_CODE -> Resource.Error(ErrorCode.BAD_REQUEST)
            else -> Resource.Error(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun getVacancyDetail(id: String): Resource<VacancyDetail> {
        val request = id
        val response = networkClient.doRequest(request)

        return when (response.resultCode) {
            OK_CODE -> {
                val dto = (response as? VacancyDetailResponse)?.items
                if (dto == null) {
                    Resource.Error(ErrorCode.INTERNAL_SERVER_ERROR)
                } else {
                    Resource.Success(vacancyDetailConverter.toDomain(dto))
                }
            }
            NO_CONNECTION_ERROR_CODE -> Resource.Error(ErrorCode.NO_INTERNET_CONNECTION)
            BAD_REQUEST_ERROR_CODE -> Resource.Error(ErrorCode.BAD_REQUEST)
            else -> Resource.Error(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }
}
