package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
    viewModel { (vacancyId: String) ->
        VacancyDetailViewModel(vacancyId, get(), get())
    }
}
