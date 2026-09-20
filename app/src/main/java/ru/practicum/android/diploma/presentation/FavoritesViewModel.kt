package ru.practicum.android.diploma.presentation

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

    init {
        getFavorites()
    }

    private fun getFavorites() {
        _state.value = FavoritesScreenState.Loading
        viewModelScope.launch {
            favoritesInteractor.getFavoriteList().collect {
                when (it) {
                    is Resource.Success -> {
                        val list = it.data.orEmpty()
                        if (list.isEmpty()) {
                            _state.value = FavoritesScreenState.Empty
                        } else {
                            _state.value = FavoritesScreenState.Content(list)
                        }
                    }
                    is Resource.Error -> {
                        _state.value = FavoritesScreenState.Error
                    }
                }
            }
        }
    }
}
