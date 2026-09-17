package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
            storage.getFilterParameters().collect {
                when {
                    it == null && area == null -> {}
                    it == null && area != null -> storage.saveFilterParameters(FilterParameters(area = area))
                    it != null -> {
                        val newParams = it.copy(area = area)
                        if (newParams.allNull()) {
                            storage.clearFilterParameters()
                        } else {
                            storage.saveFilterParameters(newParams)
                        }
                    }
                }
            }
        }
    }

    override suspend fun updateIndustry(industry: Industry?) {
        withContext(Dispatchers.IO) {
            storage.getFilterParameters().collect {
                when {
                    it == null && industry == null -> {}
                    it == null && industry != null -> storage.saveFilterParameters(
                        FilterParameters(industry = industry)
                    )

                    it != null -> {
                        val newParams = it.copy(industry = industry)
                        if (newParams.allNull()) {
                            storage.clearFilterParameters()
                        } else {
                            storage.saveFilterParameters(newParams)
                        }
                    }
                }
            }
        }
    }

    override suspend fun updateSalary(salary: Int?) {
        withContext(Dispatchers.IO) {
            storage.getFilterParameters().collect {
                when {
                    it == null && salary == null -> {}
                    it == null && salary != null -> storage.saveFilterParameters(FilterParameters(salary = salary))
                    it != null -> {
                        val newParams = it.copy(salary = salary)
                        if (newParams.allNull()) {
                            storage.clearFilterParameters()
                        } else {
                            storage.saveFilterParameters(newParams)
                        }
                    }
                }
            }
        }
    }

    override suspend fun updateOnlyWithSalary(onlyWithSalary: Boolean) {
        withContext(Dispatchers.IO) {
            storage.getFilterParameters().collect {
                when {
                    it == null && !onlyWithSalary -> {}
                    it == null && onlyWithSalary -> storage.saveFilterParameters(
                        FilterParameters(onlyWithSalary = true)
                    )

                    it != null -> {
                        val newParams = it.copy(onlyWithSalary = onlyWithSalary)
                        if (newParams.allNull()) {
                            storage.clearFilterParameters()
                        } else {
                            storage.saveFilterParameters(newParams)
                        }
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
