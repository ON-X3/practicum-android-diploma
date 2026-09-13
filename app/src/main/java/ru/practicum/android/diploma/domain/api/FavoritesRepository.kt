package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FavoriteVacancy
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.util.Resource

interface FavoritesRepository {
    suspend fun addToFavorite(vacancy: FavoriteVacancy): Resource<Unit>
    suspend fun deleteFromFavoriteById(id: String): Resource<Unit>
    suspend fun isFavorite(id: String): Resource<Boolean>
    suspend fun getVacancyById(id: String): Resource<FavoriteVacancy>
    suspend fun getFavoriteList(): Resource<List<VacancyCard>>
}
