package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.CountryArea
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.util.Resource

class CountryViewModel(val filterInteractor: FilterInteractor, val appScope: CoroutineScope) : ViewModel() {
    private val countryStateUiLiveData = MutableLiveData<CountryStateUi>()
    fun observeCountryStateUi(): LiveData<CountryStateUi> = countryStateUiLiveData
    init {
        getCountries()
    }
    private fun getCountries() {
        viewModelScope.launch {
            countryStateUiLiveData.value = CountryStateUi.Loading
            when (val result = filterInteractor.getFilterCountries()) {
                is Resource.Success -> {
                    countryStateUiLiveData.value = CountryStateUi.Content(result.data!!)
                }
                
                is Resource.Error -> countryStateUiLiveData.value = CountryStateUi.Error
            }
        }
    }
    fun saveCountry(country: FilterArea) {
        appScope.launch {
            filterInteractor.updateArea(
                FilterAreaDetails(
                    country = CountryArea(country.id, country.name),
                    region = null
                )
            )
        }
    }
}
