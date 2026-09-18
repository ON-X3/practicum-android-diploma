package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.util.ErrorCode

sealed class SearchStateUi {
    object Default : SearchStateUi()
    object Loading : SearchStateUi()
    object Success : SearchStateUi()
    object Empty : SearchStateUi()
    object NoMoreItems : SearchStateUi()
    data class Error(val errorCode: ErrorCode?) : SearchStateUi()
}
