package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.Industry

sealed interface IndustryState {
    data object Loading : IndustryState
    data class Content(val industries: List<Industry>) : IndustryState
    data object Empty : IndustryState
    data object Error : IndustryState
}