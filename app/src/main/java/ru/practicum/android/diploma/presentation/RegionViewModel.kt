package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.CountryArea
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.models.RegionArea
import ru.practicum.android.diploma.domain.util.Resource
import ru.practicum.android.diploma.util.Debouncer

class RegionViewModel(
    private val filterInteractor: FilterInteractor,
    private val appScope: CoroutineScope
) : ViewModel() {

    private val countries: MutableList<FilterArea> = mutableListOf()
    private val regions: MutableList<FilterArea> = mutableListOf()
    private val filteredRegions: MutableList<FilterArea> = mutableListOf()
    private var filterExpression: String = ""
    private var currentCountry: CountryArea? = null
    private var filterDebouncer = Debouncer<Unit>(
        FILTER_DEBOUNCE_DELAY,
        viewModelScope,
        { _ -> filterRegions() }
    )
    private val _state = MutableLiveData<RegionState>()
    fun state(): LiveData<RegionState> = _state

    init {
        viewModelScope.launch {
            currentCountry = filterInteractor.getFilterParameters().first()?.area?.country
            getFilterAreas()
        }
    }

    private suspend fun getFilterAreas() {
        _state.value = RegionState.Loading
        val regionsRes = filterInteractor.getFilterRegions(currentCountry?.countryId)
        val countriesRes = if (currentCountry == null) {
            filterInteractor.getFilterCountries()
        } else {
            Resource.Success(
                listOf(
                    FilterArea(currentCountry!!.countryId, null, currentCountry!!.countryName)
                )
            )
        }
        if (regionsRes is Resource.Success && countriesRes is Resource.Success) {
            countries.addAll(countriesRes.data!!)
            regions.addAll(regionsRes.data!!)
            if (regions.isEmpty()) {
                _state.value = RegionState.Error
            } else {
                filterDebouncer.invoke(Unit)
            }
        } else {
            _state.value = RegionState.Error
        }
    }

    private fun filterRegions() {
        filteredRegions.clear()
        filteredRegions.addAll(regions.filter { it.name.contains(filterExpression, true) })
        if (filteredRegions.isEmpty()) {
            _state.value = RegionState.Empty
        } else {
            _state.value = RegionState.Content(filteredRegions.map {
                it.name
            })
        }

    }

    fun onSearchTextChanged(text: String) {
        if (text == filterExpression) {
            return
        }
        filterExpression = text
        filterDebouncer.invoke(Unit)
    }

    fun onAreaClick(name: String) {
        val selectedArea = filteredRegions.find { it.name == name }!!
        val country = findCountryOf(selectedArea)
        appScope.launch {
            filterInteractor.updateArea(
                FilterAreaDetails(
                    CountryArea(country.id, country.name),
                    RegionArea(selectedArea.id, selectedArea.name)
                )
            )
        }
    }

    fun findCountryOf(region: FilterArea): FilterArea {
        var country: FilterArea?
        var currentIterationRegion = region
        while (true) {
            country = countries.find { it.id == currentIterationRegion.parentId }
            if (country != null) {
                break
            } else {
                currentIterationRegion = regions.find { it.id == currentIterationRegion.parentId }!!
            }
        }
        return country
    }

    companion object {
        const val FILTER_DEBOUNCE_DELAY = 0L
    }
}
