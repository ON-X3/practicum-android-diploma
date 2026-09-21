package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.domain.api.FavoritesRepository
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.Resource

class FavoritesInteractorImpl(
    private val repository: FavoritesRepository
) : FavoritesInteractor {
    override suspend fun addToFavorite(vacancy: VacancyDetail) {
        repository.addToFavorite(vacancy)
    }

    override suspend fun deleteFromFavoriteById(id: String) {
        repository.deleteFromFavoriteById(id)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return repository.isFavorite(id)
    }

    override suspend fun getVacancyById(id: String): VacancyDetail? {
        return repository.getVacancyById(id)
    }

    override suspend fun getFavoriteList(): Flow<Resource<List<VacancyCard>>> {
        return repository.getFavoriteList()
    }
}
