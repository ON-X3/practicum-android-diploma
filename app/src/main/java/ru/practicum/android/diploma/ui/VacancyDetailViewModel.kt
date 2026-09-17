package ru.practicum.android.diploma.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource
import ru.practicum.android.diploma.presentation.VacancyDetailState

class VacancyDetailViewModel(
    private val vacancyId: String,
    private val vacancyDetailInteractor: SearchInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val requestStateLiveData = MutableLiveData<VacancyDetailState>()
    val requestState: LiveData<VacancyDetailState> = requestStateLiveData

    init {
        getVacancyDetail(vacancyId)
    }

    private fun getVacancyDetail(vacancyId: String) {
        viewModelScope.launch {
            requestStateLiveData.value = VacancyDetailState.Loading
            when (val result = vacancyDetailInteractor.getVacancyDetail(vacancyId)) {
                is Resource.Success -> {
                    val data = result.data
                    requestStateLiveData.value = if (data != null) {
                        VacancyDetailState.Content(data)
                    } else {
                        VacancyDetailState.Error(ErrorCode.INTERNAL_SERVER_ERROR)
                    }
                }
                is Resource.Error -> {
                    val errorCode = result.errorCode ?: ErrorCode.INTERNAL_SERVER_ERROR
                    requestStateLiveData.value = VacancyDetailState.Error(errorCode)
                }
            }

        }
    }

    fun shareVacancy() {
        val vacancy = requestState.value
        if (vacancy !is VacancyDetailState.Content) return
        sharingInteractor.shareVacancy(vacancy.vacancy.sharingUrl)
    }
}
