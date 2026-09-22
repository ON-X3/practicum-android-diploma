package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.CountryViewModel
import ru.practicum.android.diploma.presentation.FavoritesViewModel
import ru.practicum.android.diploma.presentation.IndustryViewModel
import ru.practicum.android.diploma.presentation.RegionViewModel
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailViewModel
import ru.practicum.android.diploma.ui.FiltersViewModel
import ru.practicum.android.diploma.ui.WorkLocationViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }
    viewModel {
        RegionViewModel(get(), get(named("applicationScope")))
    }
    viewModel { CountryViewModel(filterInteractor = get(), get(named("applicationScope"))) }
    viewModel { (vacancyId: String) ->
        VacancyDetailViewModel(vacancyId, get(), get(), get())
    }
    viewModel {
        FavoritesViewModel(get())
    }
    viewModel {
        WorkLocationViewModel(get(), get(named("applicationScope")))
    }
    viewModel {
        IndustryViewModel(get())
    }
    viewModel {
        FiltersViewModel(get())
    }
}
