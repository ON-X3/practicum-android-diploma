package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.domain.models.FavoriteVacancy
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.util.Resource
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.ui.FavoritesViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        FavoritesViewModel(
            favoritesInteractor = object : FavoritesInteractor {
                override suspend fun addToFavorite(vacancy: FavoriteVacancy): Resource<Unit> {
                    return Resource.Success(Unit)
                }

                override suspend fun deleteFromFavoriteById(id: String): Resource<Unit> {
                    return Resource.Success(Unit)
                }

                override suspend fun isFavorite(id: String): Resource<Boolean> {
                    return Resource.Success(false)
                }

                override suspend fun getVacancyById(id: String): Resource<FavoriteVacancy> {
                    throw NotImplementedError("TODO")
                }

                override suspend fun getFavoriteList(): Resource<List<VacancyCard>> {
                    return Resource.Success(emptyList())
                }
            }
        )
    }
}
