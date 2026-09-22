package ru.practicum.android.diploma.ui

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

    private var oldLocation: FilterAreaDetails? = null
    private var currentLocation: FilterAreaDetails? = null
    private var shouldApplyNewArea: Boolean = false
    private val _state = MutableLiveData(WorkLocationScreenState())
    val state: LiveData<WorkLocationScreenState> = _state

    init {
        viewModelScope.launch {
            oldLocation = filterInteractor.getFilterParameters().first()?.area
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
        val currentState = _state.value ?: WorkLocationScreenState()
        _state.value = currentState.copy(
            country = null,
            region = null,
            isSelectButtonVisible = false
        )
    }

    fun clearRegion() {
        val currentState = _state.value ?: WorkLocationScreenState()
        _state.value = currentState.copy(
            region = null,
            isSelectButtonVisible = !currentState.country.isNullOrBlank()
        )
        viewModelScope.launch { filterInteractor.updateArea(currentLocation?.copy(region = null)) }
    }

    fun saveLocation() {
        shouldApplyNewArea = true
    }

    override fun onCleared() {
        if (!shouldApplyNewArea) {
            appScope.launch { filterInteractor.updateArea(oldLocation) }
        }
        super.onCleared()
    }
}
