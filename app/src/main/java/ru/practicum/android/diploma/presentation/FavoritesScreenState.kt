package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.VacancyCard

sealed interface FavoritesScreenState {
    object Loading : FavoritesScreenState
    object Empty : FavoritesScreenState
    data class Content(val vacancies: List<VacancyCard>) : FavoritesScreenState
    object Error : FavoritesScreenState
}
