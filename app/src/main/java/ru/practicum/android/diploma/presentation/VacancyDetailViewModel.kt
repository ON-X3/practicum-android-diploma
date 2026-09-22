package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource

class VacancyDetailViewModel(
    private val vacancyId: String,
    private val searchInteractor: SearchInteractor,
    private val sharingInteractor: SharingInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private val _state = MutableLiveData<VacancyDetailState>()
    val state: LiveData<VacancyDetailState> = _state

    init {
        getVacancyDetail(vacancyId)
    }

    private fun getVacancyDetail(vacancyId: String) {
        viewModelScope.launch {
            _state.value = VacancyDetailState.Loading
            when (val result = searchInteractor.getVacancyDetail(vacancyId)) {
                is Resource.Success -> {
                    val data = result.data
                    _state.value = if (data != null) {
                        VacancyDetailState.Content(data)
                    } else {
                        VacancyDetailState.Error(ErrorCode.INTERNAL_SERVER_ERROR)
                    }
                }

                is Resource.Error -> {
                    val errorCode = result.errorCode ?: ErrorCode.INTERNAL_SERVER_ERROR
                    _state.value = VacancyDetailState.Error(errorCode)
                }
            }

        }
    }

    fun onFavoriteButtonClick() {
        if (_state.value !is VacancyDetailState.Content) return
        val isFavorite = (_state.value as VacancyDetailState.Content).vacancy.isFavorite
        _state.value =
            VacancyDetailState.Content(
                (_state.value as VacancyDetailState.Content)
                    .vacancy.copy(isFavorite = !isFavorite)
            )
        viewModelScope.launch {
            if (isFavorite) {
                favoritesInteractor.deleteFromFavoriteById(vacancyId)
            } else {
                favoritesInteractor.addToFavorite((_state.value as VacancyDetailState.Content).vacancy)
            }
        }
    }

    fun shareVacancy() {
        val vacancy = state.value
        if (vacancy !is VacancyDetailState.Content) return
        sharingInteractor.shareVacancy(vacancy.vacancy.url)
    }
}
