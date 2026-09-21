package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.Resource

interface FavoritesRepository {
    suspend fun addToFavorite(vacancy: VacancyDetail)
    suspend fun deleteFromFavoriteById(id: String)
    suspend fun isFavorite(id: String): Boolean
    suspend fun getVacancyById(id: String): VacancyDetail?
    fun getFavoriteList(): Flow<Resource<List<VacancyCard>>>
}
