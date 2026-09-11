package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.util.Resource

interface SearchInteractor {
    suspend fun searchVacancies(
        expression: String,
        filter: FilterParameters? = null,
        page: Int = 1
    ): Resource<VacanciesSearchResult>
}
