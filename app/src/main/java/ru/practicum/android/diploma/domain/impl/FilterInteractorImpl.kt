package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Industry

class FilterInteractorImpl(private val filterRepository: FilterRepository) : FilterInteractor {
    override suspend fun updateArea(area: FilterAreaDetails?) {
        filterRepository.updateArea(area)
    }

    override suspend fun updateIndustry(industry: Industry?) {
        filterRepository.updateIndustry(industry)
    }

    override suspend fun updateSalary(salary: Int?) {
        filterRepository.updateSalary(salary)
    }

    override suspend fun updateOnlyWithSalary(onlyWithSalary: Boolean) {
        filterRepository.updateOnlyWithSalary(onlyWithSalary)
    }

    override suspend fun clearFilterParameters() {
        filterRepository.clearFilterParameters()
    }

    override fun getFilterParameters(): Flow<FilterParameters?> = filterRepository.getFilterParameters()
}
