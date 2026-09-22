package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailViewModel
import ru.practicum.android.diploma.ui.FavoritesViewModel
import ru.practicum.android.diploma.ui.WorkLocationViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
    viewModel { (vacancyId: String) ->
        VacancyDetailViewModel(vacancyId, get(), get())
    }
    viewModel {
        FavoritesViewModel(get())
    }
    viewModel {
        WorkLocationViewModel(get(), get())
    }
}
