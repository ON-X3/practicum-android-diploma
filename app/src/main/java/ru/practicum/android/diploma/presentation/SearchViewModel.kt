package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.util.Resource
import ru.practicum.android.diploma.util.Debouncer

class SearchViewModel(
    val searchInteractor: SearchInteractor
) : ViewModel() {
    private var currentExpression: String = ""
    private var currentFilter: FilterParameters? = null
    private var currentPage: Int = 1
    private var maxPages: Int = 1
    private var isLoading = false
    private val debouncer = Debouncer<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        action = { expression -> searchVacancies(expression) }
    )
    private val vacanciesList = mutableListOf<VacancyCard>()
    private var isNextPageLoading: Boolean = false
    private val searchStateUiLiveData = MutableLiveData<SearchStateUi>(SearchStateUi.Default)
    fun observeSearchStateUi(): LiveData<SearchStateUi> = searchStateUiLiveData

    private val isFilterActive = MutableLiveData(false)
    fun observeIsFilterActive(): LiveData<Boolean> = isFilterActive


    fun updateFilter(filter: FilterParameters?) {
        currentPage = 1
        currentFilter = filter
        viewModelScope.launch {
            searchVacancies(currentExpression)
        }
    }
    fun onSearchTextChanged(text: String) {
        currentExpression = text
        if (text.isEmpty()) {
            debouncer.cancel()
            searchStateUiLiveData.value = SearchStateUi.Default
            return
        }
        currentPage = 1
        debouncer.invoke(currentExpression)
    }
    fun loadNextPage() {
        if (!isLoading && currentPage < maxPages) {
            isNextPageLoading = true
            currentPage++
            viewModelScope.launch {
                searchVacancies(currentExpression)
            }
        }
    }
    suspend fun searchVacancies(expression: String) {
        isLoading = true
        if (!isNextPageLoading) {
            searchStateUiLiveData.value = SearchStateUi.Loading
        }
        when (val searchResult = searchInteractor.searchVacancies(expression, currentFilter, currentPage)) {
            is Resource.Success -> {
                currentPage = searchResult.data?.currentPage ?: 1
                maxPages = searchResult.data?.pages ?: 1
                val newItems = searchResult.data?.vacancies
                if (newItems.isNullOrEmpty()) {
                    if (currentPage == 1) {
                        searchStateUiLiveData.value = SearchStateUi.Empty
                    } else {
                        searchStateUiLiveData.value = SearchStateUi.NoMoreItems
                    }
                    isLoading = false
                    return
                }
                vacanciesList.addAll(newItems)
                searchStateUiLiveData.value = SearchStateUi.Success(vacanciesList, searchResult.data.found, maxPages > currentPage)
                isLoading = false
                isNextPageLoading = false
            }
            is Resource.Error -> {
                searchStateUiLiveData.value = SearchStateUi.Error(searchResult.errorCode)
                isLoading = false
                isNextPageLoading = false
            }
        }
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
