package ru.practicum.android.diploma.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource

sealed interface MainState {
    data object Default : MainState
    data object Loading : MainState
    data class Content(val data: VacanciesSearchResult) : MainState
    data object Empty : MainState
    data object NoInternet : MainState
    data object ServerError : MainState
}

class MainViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Default)
    val state: StateFlow<MainState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private var latestSearchText: String? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) return
        latestSearchText = changedText

        searchJob?.cancel()

        if (changedText.isBlank()) {
            _state.value = MainState.Default
            return
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            performSearch(changedText)
        }
    }

    private suspend fun performSearch(text: String) {
        _state.value = MainState.Loading

        val result = searchInteractor.searchVacancies(expression = text, page = 0)
        when (result) {
            is Resource.Success -> {
                val data = result.data
                if (data == null || data.vacancies.isEmpty()) {
                    _state.value = MainState.Empty
                } else {
                    _state.value = MainState.Content(data)
                }
            }
            is Resource.Error -> {
                when (result.errorCode) {
                    ErrorCode.NO_INTERNET_CONNECTION -> {
                        _state.value = MainState.NoInternet
                    }
                    ErrorCode.NOT_FOUND -> {
                        _state.value = MainState.Empty
                    }
                    else -> {
                        _state.value = MainState.ServerError
                    }
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
