package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.storage.StorageClient
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.allNull

class FilterRepositoryImpl(private val storage: StorageClient) : FilterRepository {
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

}
