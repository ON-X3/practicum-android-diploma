package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.ErrorCode

sealed interface VacancyDetailState {
    data object Loading : VacancyDetailState
    data class Content(val vacancy: VacancyDetail) : VacancyDetailState
    data class Error(val errorCode: ErrorCode) : VacancyDetailState
}
