package ru.practicum.android.diploma.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.util.Resource
import ru.practicum.android.diploma.util.Debouncer

class IndustryViewModel(
    private val interactor: FilterInteractor
) : ViewModel() {

    private var allIndustries: List<Industry> = emptyList()
    private var searchQuery: String = ""
    private var selectedIndustryId: Int? = null

    val selectedId: Int? get() = selectedIndustryId

    private val searchDebouncer = Debouncer<Unit>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        action = { updateFilteredList() }
    )

    private val _state = MutableStateFlow<IndustryState>(IndustryState.Loading)
    val state: StateFlow<IndustryState> = _state.asStateFlow()

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            _state.value = IndustryState.Loading
            when (val result = interactor.getFilterIndustries()) {
                is Resource.Success -> {
                    allIndustries = result.data.orEmpty()
                    selectedIndustryId = interactor
                        .getFilterParameters()
                        .first()
                        ?.industry
                        ?.industryId
                    updateFilteredList()
                }
                is Resource.Error -> _state.value = IndustryState.Error
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        if (query == searchQuery) return
        searchQuery = query
        searchDebouncer.invoke(Unit)
    }

    private fun updateFilteredList() {
        val filtered = if (searchQuery.isBlank()) {
            allIndustries
        } else {
            allIndustries.filter { it.industryName.contains(searchQuery, ignoreCase = true) }
        }

        _state.value = if (filtered.isEmpty()) {
            IndustryState.Empty
        } else {
            IndustryState.Content(filtered)
        }
    }

    fun onSearchSubmitted() {
        searchDebouncer.cancel()
        updateFilteredList()
    }

    fun onIndustrySelected(industry: Industry) {
        selectedIndustryId = industry.industryId
        viewModelScope.launch { interactor.updateIndustry(industry) }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 300L
    }
}
