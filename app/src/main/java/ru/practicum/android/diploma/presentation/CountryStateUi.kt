package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.FilterArea

sealed class CountryStateUi {
    data class Content(val countries: List<FilterArea>) : CountryStateUi()
    object Error : CountryStateUi()
    object Loading : CountryStateUi()
}
