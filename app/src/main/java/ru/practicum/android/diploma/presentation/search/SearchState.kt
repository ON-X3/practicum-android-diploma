package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.domain.models.VacanciesSearchResult

sealed interface SearchState {
    data object Default : SearchState
    data object Loading : SearchState
    data class Content(val data: VacanciesSearchResult) : SearchState
    data object Empty : SearchState
    data object NoInternet : SearchState
    data object ServerError : SearchState
}
