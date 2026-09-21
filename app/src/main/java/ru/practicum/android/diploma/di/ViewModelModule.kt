package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.CountryViewModel
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.ui.VacancyViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
    viewModel { VacancyViewModel(repository = get()) }
    viewModel { CountryViewModel(filterInteractor = get(),get(named("applicationScope"))) }
}
