package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.Resource

interface FavoritesInteractor {
    suspend fun addToFavorite(vacancy: VacancyDetail)
    suspend fun deleteFromFavoriteById(id: String)
    suspend fun getFavoriteList(): Flow<Resource<List<VacancyCard>>>
}
