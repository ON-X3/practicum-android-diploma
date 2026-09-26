package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterAreaDetails

class WorkLocationViewModel(private val filterInteractor: FilterInteractor, private val appScope: CoroutineScope) :
    ViewModel() {

    private var currentLocation: FilterAreaDetails? = null
    private val _state = MutableLiveData(WorkLocationScreenState())
    val state: LiveData<WorkLocationScreenState> = _state

    init {
        viewModelScope.launch {
            filterInteractor.getFilterParameters().collect {
                currentLocation = it?.area
                setCountry(it?.area?.country?.countryName)
                setRegion(it?.area?.region?.regionName)
            }
        }
    }

    fun setCountry(countryName: String?) {
        val currentState = _state.value ?: WorkLocationScreenState()
        val newState = currentState.copy(
            country = countryName,
            isSelectButtonVisible = !countryName.isNullOrBlank() || !currentState.region.isNullOrBlank()
        )
        _state.value = newState
    }

    fun setRegion(regionName: String?) {
        val currentState = _state.value ?: WorkLocationScreenState()
        val newState = currentState.copy(
            region = regionName,
            isSelectButtonVisible = !currentState.country.isNullOrBlank() || !regionName.isNullOrBlank()
        )
        _state.value = newState
    }

    fun clearCountry() {
        viewModelScope.launch { filterInteractor.updateArea(null) }
    }

    fun clearRegion() {
        viewModelScope.launch { filterInteractor.updateArea(currentLocation?.copy(region = null)) }
    }
}
