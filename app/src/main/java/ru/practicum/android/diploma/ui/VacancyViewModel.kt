package ru.practicum.android.diploma.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.util.Resource

class VacancyViewModel(
    private val repository: SearchRepository
) : ViewModel() {

    private val _state = MutableLiveData<VacancyScreenState>()
    val state: LiveData<VacancyScreenState> = _state

    fun loadVacancy(vacancyId: String) {
        _state.value = VacancyScreenState.Loading
        viewModelScope.launch {
            when (repository.getVacancyDetail(vacancyId)) {
                is Resource.Error -> _state.postValue(VacancyScreenState.ServerError)
                is Resource.Success -> Unit
            }
        }
    }
}
