package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetail

interface FavoritesInteractor {
    suspend fun addToFavorite(vacancy: VacancyDetail)
    suspend fun deleteFromFavoriteById(id: String)
    suspend fun isFavorite(id: String): Boolean
    suspend fun getVacancyById(id: String): VacancyDetail?
    suspend fun getFavoriteList(): Flow<List<VacancyCard>>
}
