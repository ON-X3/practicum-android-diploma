package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.VacancyCard

sealed class SearchStateUi {
    object Default : SearchStateUi()
    object Loading : SearchStateUi()

    data class Success(
        val vacancies: List<VacancyCard>,
        val amountOfVacancies: Int,
        val hasNextPage: Boolean
    ) : SearchStateUi()
    object NextPageLoading: SearchStateUi()
    object Empty : SearchStateUi()
    object NoInternetError : SearchStateUi()
    object ServerError : SearchStateUi()
}
