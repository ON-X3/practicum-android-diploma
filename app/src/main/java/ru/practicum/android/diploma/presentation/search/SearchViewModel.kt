package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Default)
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private var latestSearchText: String? = null

    fun searchDebounce(changedText: String, filter: FilterParameters? = null) {
        if (latestSearchText == changedText) return
        latestSearchText = changedText

        searchJob?.cancel()

        if (changedText.isBlank()) {
            _state.value = SearchState.Default
            return
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            performSearch(changedText, filter)
        }
    }

    private suspend fun performSearch(expression: String, filter: FilterParameters?) {
        _state.value = SearchState.Loading

        val result = searchInteractor.searchVacancies(expression, filter, page = 0)
        when (result) {
            is Resource.Success -> {
                val data = result.data
                if (data == null || data.vacancies.isEmpty()) {
                    _state.value = SearchState.Empty
                } else {
                    _state.value = SearchState.Content(data)
                }
            }
            is Resource.Error -> {
                when (result.errorCode) {
                    ErrorCode.NO_INTERNET_CONNECTION -> {
                        _state.value = SearchState.NoInternet
                    }
                    ErrorCode.NOT_FOUND -> {
                        _state.value = SearchState.Empty
                    }
                    else -> {
                        _state.value = SearchState.ServerError
                    }
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
