package ru.practicum.android.diploma.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterAreaDetails
import ru.practicum.android.diploma.domain.models.Industry

class FiltersViewModel(
    private val filtersInteractor: FilterInteractor
) : ViewModel() {
    private val filtersLiveData = MutableLiveData<FiltersState>()
    val filtersStateLiveData : LiveData<FiltersState> = filtersLiveData

    fun getFiltersCurrentState(){
        viewModelScope.launch {
            filtersInteractor.getFilterParameters().collect {
                filtersValue -> filtersValue?.let{
                    filtersLiveData.postValue(
                        FiltersViewModel.FiltersState(
                            area = filtersValue.area,
                            industry = filtersValue.industry,
                            salary = filtersValue.salary,
                            onlyWithSalary = filtersValue.onlyWithSalary
                        )
                    )
                }
            }

        }
    }

    data class FiltersState(
        val area: FilterAreaDetails?,
        val industry: Industry?,
        val salary: Int?,
        val onlyWithSalary: Boolean
    )
}


