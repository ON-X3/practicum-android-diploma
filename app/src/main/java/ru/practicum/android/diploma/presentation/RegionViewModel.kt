package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.CountryArea
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.models.RegionArea
import ru.practicum.android.diploma.domain.util.Resource
import ru.practicum.android.diploma.util.Debouncer

class RegionViewModel(private val filterInteractor: FilterInteractor) : ViewModel() {

    private val allAreas: MutableList<FilterArea> = mutableListOf()
    private val filteredRegions: MutableList<FilterArea> = mutableListOf()
    private var filterExpression: String = ""
    private var currentCountryId: Int? = null
    private var filterDebouncer = Debouncer<Unit>(
        FILTER_DEBOUNCE_DELAY,
        viewModelScope,
        { _ -> filterRegions() }
    )
    private val _state = MutableLiveData<RegionState>()
    fun state(): LiveData<RegionState> = _state

    init {
        viewModelScope.launch {
            currentCountryId = filterInteractor.getFilterParameters().first()?.area?.country?.countryId
            getFilterAreas()
        }
    }

    suspend fun getFilterAreas() {
        _state.value = RegionState.Loading
        val res = filterInteractor.getFilterAreas()
        if (res is Resource.Success) {
            allAreas.addAll(res.data!!)
            if (allAreas.isEmpty()) {
                _state.value = RegionState.Error
            } else {
                filterByCountry()
                filterDebouncer.invoke(Unit)
            }
        } else {
            _state.value = RegionState.Error
        }
    }

    private fun filterByCountry() {
        if (currentCountryId != null) {
            val currentCountry = allAreas.find { it.id == currentCountryId }
            if (currentCountry != null) {
                allAreas.clear()
                allAreas.add(currentCountry)
            }
        }
    }

    private fun filterRegions() {
        filteredRegions.clear()
        allAreas.forEach { country ->
            filteredRegions.addAll(country.areas.filter {
                it.name.contains(filterExpression, true)
            })
        }
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
        val selectedArea = filteredRegions.find { it.name == name }
        val country = allAreas.find { it.id == selectedArea!!.parentId }
        viewModelScope.launch {
            filterInteractor.updateArea(
                FilterAreaDetails(
                    CountryArea(country!!.id, country.name),
                    RegionArea(selectedArea!!.id, selectedArea.name)
                )
            )

        }
    }

    companion object {
        const val FILTER_DEBOUNCE_DELAY = 0L
    }
}
