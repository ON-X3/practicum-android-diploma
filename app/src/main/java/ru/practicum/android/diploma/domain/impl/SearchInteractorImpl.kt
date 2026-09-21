package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FavoritesRepository
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val favoritesRepository: FavoritesRepository
) : SearchInteractor {
    override suspend fun searchVacancies(
        expression: String,
        filter: FilterParameters?,
        page: Int
    ): Resource<VacanciesSearchResult> {
        return searchRepository.searchVacancies(expression, filter, page)
    }

    override suspend fun getVacancyDetail(id: String): Resource<VacancyDetail> {
        val res = searchRepository.getVacancyDetail(id)
        return if (res is Resource.Success) {
            res
        } else {
            when (res.errorCode) {
                ErrorCode.NOT_FOUND -> {
                    favoritesRepository.deleteFromFavoriteById(id)
                    res
                }

                else -> {
                    if (favoritesRepository.isFavorite(id)) {
                        favoritesRepository.getVacancyById(id)
                    } else {
                        res
                    }
                }
            }
        }
    }
}
