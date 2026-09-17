package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.FilterAreasRequest
import ru.practicum.android.diploma.data.dto.FilterAreasResponse
import ru.practicum.android.diploma.data.dto.FilterIndustriesRequest
import ru.practicum.android.diploma.data.dto.FilterIndustriesResponse
import ru.practicum.android.diploma.data.dto.toDomainFilterArea
import ru.practicum.android.diploma.data.dto.toDomainFilterIndustry
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.storage.StorageClient
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.allNull
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource

class FilterRepositoryImpl(private val storage: StorageClient, private val networkClient: NetworkClient) :
    FilterRepository {
    override suspend fun updateArea(area: FilterAreaDetails?) {
        withContext(Dispatchers.IO) {
            val currentFilter = storage.getFilterParameters().first()
            when {
                currentFilter == null && area == null -> {}
                currentFilter == null && area != null -> storage.saveFilterParameters(FilterParameters(area = area))
                currentFilter != null -> {
                    val newParams = currentFilter.copy(area = area)
                    if (newParams.allNull()) {
                        storage.clearFilterParameters()
                    } else {
                        storage.saveFilterParameters(newParams)
                    }
                }
            }

        }
    }

    override suspend fun updateIndustry(industry: Industry?) {
        withContext(Dispatchers.IO) {
            val currentFilter = storage.getFilterParameters().first()
            when {
                currentFilter == null && industry == null -> {}
                currentFilter == null && industry != null -> storage.saveFilterParameters(
                    FilterParameters(industry = industry)
                )

                currentFilter != null -> {
                    val newParams = currentFilter.copy(industry = industry)
                    if (newParams.allNull()) {
                        storage.clearFilterParameters()
                    } else {
                        storage.saveFilterParameters(newParams)
                    }
                }
            }
        }
    }

    override suspend fun updateSalary(salary: Int?) {
        withContext(Dispatchers.IO) {
            val currentFilter = storage.getFilterParameters().first()
            when {
                currentFilter == null && salary == null -> {}
                currentFilter == null && salary != null -> storage.saveFilterParameters(
                    FilterParameters(salary = salary)
                )
                currentFilter != null -> {
                    val newParams = currentFilter.copy(salary = salary)
                    if (newParams.allNull()) {
                        storage.clearFilterParameters()
                    } else {
                        storage.saveFilterParameters(newParams)
                    }
                }
            }
        }
    }

    override suspend fun updateOnlyWithSalary(onlyWithSalary: Boolean) {
        withContext(Dispatchers.IO) {
            val currentFilter = storage.getFilterParameters().first()
            when {
                currentFilter == null && !onlyWithSalary -> {}
                currentFilter == null && onlyWithSalary -> storage.saveFilterParameters(
                    FilterParameters(onlyWithSalary = true)
                )

                currentFilter != null -> {
                    val newParams = currentFilter.copy(onlyWithSalary = onlyWithSalary)
                    if (newParams.allNull()) {
                        storage.clearFilterParameters()
                    } else {
                        storage.saveFilterParameters(newParams)
                    }
                }
            }
        }
    }

    override suspend fun clearFilterParameters() {
        storage.clearFilterParameters()
    }

    override fun getFilterParameters(): Flow<FilterParameters?> = storage.getFilterParameters()

    override suspend fun getFilterAreas(): Resource<List<FilterArea>> {
        val response = networkClient.doRequest(FilterAreasRequest())
        return if (response.resultCode == NetworkClient.OK_CODE) {
            Resource.Success((response as FilterAreasResponse).areas.map { it.toDomainFilterArea() })
        } else {
            Resource.Error(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun getFilterIndustries(): Resource<List<FilterIndustry>> {
        val response = networkClient.doRequest(FilterIndustriesRequest())
        return if (response.resultCode == NetworkClient.OK_CODE) {
            Resource.Success((response as FilterIndustriesResponse).industries.map { it.toDomainFilterIndustry() })
        } else {
            Resource.Error(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

}
