package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.data.dto.FilterAreasRequest
import ru.practicum.android.diploma.data.dto.FilterAreasResponse
import ru.practicum.android.diploma.data.dto.FilterIndustriesRequest
import ru.practicum.android.diploma.data.dto.FilterIndustriesResponse
import ru.practicum.android.diploma.data.dto.toDomainFilterArea
import ru.practicum.android.diploma.data.dto.toDomainIndustry
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.storage.StorageClient
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.allNull
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource

class FilterRepositoryImpl(
    private val storage: StorageClient,
    private val networkClient: NetworkClient,
    private val shouldShowLocalitiesInRegions: Boolean
) :
    FilterRepository {
    override suspend fun updateArea(area: FilterAreaDetails?) {
        updateFilter { it.copy(area = area) }
    }

    override suspend fun updateIndustry(industry: Industry?) {
        updateFilter { it.copy(industry = industry) }
    }

    override suspend fun updateSalary(salary: Int?) {
        updateFilter { it.copy(salary = salary) }
    }

    override suspend fun updateOnlyWithSalary(onlyWithSalary: Boolean) {
        updateFilter { it.copy(onlyWithSalary = onlyWithSalary) }
    }

    private suspend fun updateFilter(transform: (FilterParameters) -> FilterParameters) {
        withContext(Dispatchers.IO) {
            val currentFilter = storage.getFilterParameters().first() ?: FilterParameters()
            val updatedFilter = transform(currentFilter)

            if (updatedFilter.allNull()) {
                storage.clearFilterParameters()
            } else {
                storage.saveFilterParameters(updatedFilter)
            }
        }

    }

    override suspend fun clearFilterParameters() {
        storage.clearFilterParameters()
    }

    override fun getFilterParameters(): Flow<FilterParameters?> = storage.getFilterParameters()

    override suspend fun getFilterCountries(): Resource<List<FilterArea>> {
        val response = networkClient.doRequest(FilterAreasRequest())
        return if (response.resultCode == NetworkClient.OK_CODE) {
            val countries: MutableList<FilterArea> = mutableListOf()
            (response as FilterAreasResponse).areas.forEach {
                if (it.parentId == null) countries.add(it.toDomainFilterArea())
            }
            Resource.Success(countries)
        } else {
            Resource.Error(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun getFilterRegions(countryId: Int?): Resource<List<FilterArea>> {
        val response = networkClient.doRequest(FilterAreasRequest())
        return if (response.resultCode == NetworkClient.OK_CODE) {
            val regions: MutableList<FilterArea> = mutableListOf()
            val currentCountry: AreaDTO? = (response as FilterAreasResponse).areas.find { it.id == countryId }
            when (shouldShowLocalitiesInRegions) {
                true -> {
                    if (currentCountry != null) {
                        findRegionsAndLocalities(listOf(currentCountry), regions)
                    } else {
                        findRegionsAndLocalities(response.areas, regions)
                    }
                }

                else -> {
                    if (currentCountry != null) {
                        findRegions(listOf(currentCountry), regions)
                    } else {
                        findRegions(response.areas, regions)
                    }
                }
            }
            Resource.Success(regions.sortedBy { it.name.lowercase() })
        } else {
            Resource.Error(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override suspend fun getFilterIndustries(): Resource<List<Industry>> {
        val response = networkClient.doRequest(FilterIndustriesRequest())
        return if (response.resultCode == NetworkClient.OK_CODE) {
            Resource.Success((response as FilterIndustriesResponse).industries.map { it.toDomainIndustry() })
        } else {
            Resource.Error(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    private fun findRegions(areas: List<AreaDTO>, regionsList: MutableList<FilterArea>) {
        areas.forEach {
            if (it.parentId != null && it.areas.isNotEmpty()) {
                regionsList.add(it.toDomainFilterArea())
            }
            if (it.areas.isNotEmpty()) {
                findRegions(it.areas, regionsList)
            }
        }
    }

    private fun findRegionsAndLocalities(areas: List<AreaDTO>, regionsList: MutableList<FilterArea>) {
        areas.forEach {
            if (it.parentId != null) {
                regionsList.add(it.toDomainFilterArea())
            }
            if (it.areas.isNotEmpty()) {
                findRegionsAndLocalities(it.areas, regionsList)
            }
        }
    }

}
