package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource
import ru.practicum.android.diploma.util.Debouncer
import ru.practicum.android.diploma.util.SingleLiveEvent

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var currentExpression: String = ""
    private var currentFilter: FilterParameters? = null
    private var currentPage: Int = 1
    private var maxPages: Int = 1
    private var lastSuccessAmountOfVacancies: Int = 0
    private val debouncer = Debouncer<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        action = { expression -> searchVacancies(expression) }
    )
    private val vacanciesList = mutableListOf<VacancyCard>()
    private val searchStateUiLiveData = MutableLiveData<SearchStateUi>(SearchStateUi.Default)
    fun observeSearchStateUi(): LiveData<SearchStateUi> = searchStateUiLiveData

    private val isFilterActiveAndChanged = MutableLiveData(Pair(false, false))
    fun observeIsFilterActiveAndChanged(): LiveData<Pair<Boolean, Boolean>> = isFilterActiveAndChanged

    private val errorToastLiveData = SingleLiveEvent<ErrorCode>()
    fun errorToast(): LiveData<ErrorCode> = errorToastLiveData

    init {
        viewModelScope.launch {
            filterInteractor.getFilterParameters().collect {
                val isFilterChanged = it != currentFilter
                if (isFilterChanged) {
                    currentPage = 1
                    vacanciesList.clear()
                }
                currentFilter = it
                isFilterActiveAndChanged.value = Pair(currentFilter != null, isFilterChanged)
                if (currentExpression.isNotEmpty()) {
                    searchWithoutDebounce(currentExpression)
                }
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        if (currentExpression == text) return
        vacanciesList.clear()
        currentExpression = text
        if (text.isBlank()) {
            debouncer.cancel()
            searchStateUiLiveData.value = SearchStateUi.Default
            return
        }
        currentPage = 1
        debouncer.invoke(currentExpression)
    }

    fun loadNextPage() {
        if (searchStateUiLiveData.value !is SearchStateUi.NextPageLoading && currentPage < maxPages) {
            searchStateUiLiveData.value = SearchStateUi.NextPageLoading
            currentPage++
            viewModelScope.launch {
                searchVacancies(currentExpression)
            }
        }
    }

    fun searchWithoutDebounce(expression: String) {
        if (expression.isNotBlank()) {
            debouncer.cancel()
            viewModelScope.launch { searchVacancies(expression) }
        }
    }

    suspend fun searchVacancies(expression: String) {
        if (searchStateUiLiveData.value !is SearchStateUi.NextPageLoading) {
            searchStateUiLiveData.value = SearchStateUi.Loading
        }
        when (val searchResult = searchInteractor.searchVacancies(expression, currentFilter, currentPage)) {
            is Resource.Success -> processSuccess(searchResult)

            is Resource.Error -> processError(searchResult)
        }
    }

    fun processSuccess(searchResult: Resource.Success<VacanciesSearchResult>) {
        lastSuccessAmountOfVacancies = searchResult.data?.found ?: 0
        currentPage = searchResult.data?.currentPage ?: 1
        maxPages = searchResult.data?.pages ?: 1
        val newItems = searchResult.data?.vacancies
        if (newItems.isNullOrEmpty()) {
            if (currentPage == 1) {
                searchStateUiLiveData.value = SearchStateUi.Empty
            }
            return
        }
        vacanciesList.addAll(newItems)
        searchStateUiLiveData.value = SearchStateUi.Success(
            vacanciesList,
            searchResult.data.found,
            maxPages > currentPage
        )
    }

    suspend fun processError(searchResult: Resource.Error<VacanciesSearchResult>) {
        when (searchResult.errorCode) {
            ErrorCode.NO_INTERNET_CONNECTION -> {
                if (currentPage == 1) {
                    searchStateUiLiveData.value = SearchStateUi.NoInternetError
                } else {
                    currentPage--
                    errorToastLiveData.value = ErrorCode.NO_INTERNET_CONNECTION
                    delay(SEARCH_DEBOUNCE_DELAY)
                    searchStateUiLiveData.value = SearchStateUi.Success(
                        vacanciesList,
                        lastSuccessAmountOfVacancies,
                        maxPages > currentPage
                    )
                }
            }

            ErrorCode.NOT_FOUND -> {
                searchStateUiLiveData.value = SearchStateUi.Empty
            }

            else -> {
                if (currentPage == 1) {
                    searchStateUiLiveData.value = SearchStateUi.ServerError
                } else {
                    currentPage--
                    errorToastLiveData.value = ErrorCode.INTERNAL_SERVER_ERROR
                    searchStateUiLiveData.value = SearchStateUi.Success(
                        vacanciesList,
                        lastSuccessAmountOfVacancies,
                        maxPages > currentPage
                    )
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
