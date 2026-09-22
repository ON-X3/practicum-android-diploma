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
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.Debouncer

class FiltersViewModel(
    private val filtersInteractor: FilterInteractor,
    private val appScope: CoroutineScope
) : ViewModel() {
    private var oldFilterParameters: FilterParameters? = null
    private var shouldUpdateFilterParameters = false
    private val updateSalaryDebouncer = Debouncer<Int?>(SALARY_UPDATING_DELAY, appScope) {salary -> updateSalary(salary)}
    private val filtersLiveData = MutableLiveData<FiltersState>()
    val filtersStateLiveData : LiveData<FiltersState> = filtersLiveData


    init {
        viewModelScope.launch { oldFilterParameters = filtersInteractor.getFilterParameters().first() }
        getFiltersCurrentState()
    }

    fun getFiltersCurrentState() {
        viewModelScope.launch {
            filtersInteractor.getFilterParameters().collect { filtersValue ->
                if (filtersValue != null) {
                    filtersLiveData.postValue(
                        FiltersState(
                            area = filtersValue.area,
                            industry = filtersValue.industry,
                            salary = filtersValue.salary,
                            onlyWithSalary = filtersValue.onlyWithSalary
                        )
                    )
                } else {
                    filtersLiveData.value = FiltersState(null, null, null, false)
                }
            }

        }
    }

    fun onSalaryChanged(salary: Int?) {
        if (salary == filtersLiveData.value?.salary) return
        updateSalaryDebouncer.invoke(salary)
    }

    private fun updateSalary(salary: Int?) {
        if (salary == filtersLiveData.value!!.salary) return
        viewModelScope.launch {
            filtersInteractor.updateSalary(salary)
        }
    }

    fun updateWithSalary(onlyWithSalary: Boolean) {
        viewModelScope.launch {
            filtersInteractor.updateOnlyWithSalary(onlyWithSalary)
        }
    }

    fun onApplyFiltersClick() {
        shouldUpdateFilterParameters = true
    }

    fun onDropFiltersClick() {
        shouldUpdateFilterParameters = true
        updateSalaryDebouncer.cancel()
        appScope.launch { filtersInteractor.clearFilterParameters() }
    }

    override fun onCleared() {
        if (!shouldUpdateFilterParameters) {
            updateSalaryDebouncer.cancel()
            appScope.launch {
                filtersInteractor.updateArea(oldFilterParameters?.area)
                filtersInteractor.updateIndustry(oldFilterParameters?.industry)
                filtersInteractor.updateSalary(oldFilterParameters?.salary)
                filtersInteractor.updateOnlyWithSalary(oldFilterParameters?.onlyWithSalary ?: false)
            }
        }
        super.onCleared()
    }

    data class FiltersState(
        val area: FilterAreaDetails? = null,
        val industry: Industry? = null,
        val salary: Int? = null,
        val onlyWithSalary: Boolean = false
    )

    companion object {
        const val SALARY_UPDATING_DELAY = 2000L
    }
}


