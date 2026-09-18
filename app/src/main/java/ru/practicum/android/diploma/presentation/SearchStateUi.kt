package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.util.ErrorCode

sealed class SearchStateUi {
    object Default : SearchStateUi()
    object Loading : SearchStateUi()
    data class Success(
        val vacancies: List<VacancyCard>,
        val amountOfVacancies: Int,
        val hasNextPage: Boolean
    ) : SearchStateUi()
    object Empty : SearchStateUi()
    object NoMoreItems : SearchStateUi()
    data class Error(val errorCode: ErrorCode?) : SearchStateUi()
}
