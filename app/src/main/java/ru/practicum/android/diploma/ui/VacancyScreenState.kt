package ru.practicum.android.diploma.ui

sealed interface VacancyScreenState {
    object Loading: VacancyScreenState
    object ServerError: VacancyScreenState

}
