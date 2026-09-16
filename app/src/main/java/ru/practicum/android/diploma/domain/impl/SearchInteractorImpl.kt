package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.Resource

class SearchInteractorImpl(
    private val searchRepository: SearchRepository
) : SearchInteractor {
    override suspend fun searchVacancies(
        expression: String,
        filter: FilterParameters?,
        page: Int
    ): Resource<VacanciesSearchResult> {
        return searchRepository.searchVacancies(expression, filter, page)
    }

    override suspend fun getVacancyDetail(id: String): Resource<VacancyDetail> {
        return searchRepository.getVacancyDetail(id)
    }
}