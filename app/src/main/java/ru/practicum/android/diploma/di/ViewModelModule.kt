package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.CountryViewModel
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailViewModel
import ru.practicum.android.diploma.ui.FavoritesViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
    viewModel { CountryViewModel(filterInteractor = get(),get(named("applicationScope"))) }
    viewModel { (vacancyId: String) ->
        VacancyDetailViewModel(vacancyId, get(), get())
    }
    viewModel {
        FavoritesViewModel(get())
    }
}
