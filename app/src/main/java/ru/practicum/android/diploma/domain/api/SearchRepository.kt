package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.Resource

interface SearchRepository {
    suspend fun searchVacancies(
        expression: String,
        filter: FilterParameters? = null,
        page: Int = 1
    ): Resource<VacanciesSearchResult>

    suspend fun getVacancyDetail(id: String): Resource<VacancyDetail>
}
