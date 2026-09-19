package ru.practicum.android.diploma.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.domain.util.Resource

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _state = MutableLiveData<FavoritesScreenState>()
    val state: LiveData<FavoritesScreenState> = _state

    fun getFavorites() {
        _state.value = FavoritesScreenState.Loading
        viewModelScope.launch {
            when (val result = favoritesInteractor.getFavoriteList()) {
                is Resource.Success -> {
                    val list = result.data.orEmpty()
                    if (list.isEmpty()) {
                        _state.postValue(FavoritesScreenState.Empty)
                    } else {
                        _state.postValue(FavoritesScreenState.Content(list))
                    }
                }
                is Resource.Error -> {
                    _state.postValue(FavoritesScreenState.Error)
                }
            }
        }
    }
}
