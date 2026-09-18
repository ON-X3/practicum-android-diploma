package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterParameters

interface FilterInteractor {

    fun getFilter(): FilterParameters?

    fun saveFilter(filter: FilterParameters)

    fun clearFilter()
}