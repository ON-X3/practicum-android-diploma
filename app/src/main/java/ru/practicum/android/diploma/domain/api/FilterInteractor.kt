package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Industry

interface FilterInteractor {

    suspend fun updateArea(area: FilterAreaDetails)
    suspend fun updateIndustry(industry: Industry)
    suspend fun updateSalary(salary: Int)
    suspend fun updateOnlyWithSalary(onlyWithSalary: Boolean)
    suspend fun clearFilterParameters()
    fun getFilterParameters(): Flow<FilterParameters>

}
