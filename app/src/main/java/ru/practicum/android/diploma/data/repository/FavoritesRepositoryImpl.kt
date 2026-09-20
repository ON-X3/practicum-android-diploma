package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.converters.VacancyCardConverter
import ru.practicum.android.diploma.data.converters.VacancyDetailConverter
import ru.practicum.android.diploma.data.db.dao.VacancyDetailDao
import ru.practicum.android.diploma.domain.api.FavoritesRepository
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.util.ErrorCode
import ru.practicum.android.diploma.domain.util.Resource

class FavoritesRepositoryImpl(
    private val vacancyDetailDao: VacancyDetailDao,
    private val vacancyDetailConverter: VacancyDetailConverter,
    private val vacancyCardConverter: VacancyCardConverter
) : FavoritesRepository {
    override suspend fun addToFavorite(vacancy: VacancyDetail) {
        vacancyDetailDao.insertVacancy(vacancyDetailConverter.toEntity(vacancy))
    }

    override suspend fun deleteFromFavoriteById(id: String) {
        vacancyDetailDao.deleteVacancyById(id)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return vacancyDetailDao.isFavorite(id)
    }

    override suspend fun getVacancyById(id: String): VacancyDetail? {
        return vacancyDetailConverter.toDomain(vacancyDetailDao.getVacancyById(id))
    }

    override fun getFavoriteList(): Flow<Resource<List<VacancyCard>>> =
        vacancyDetailDao.getVacancyList().map { entities ->
            Resource.Success(entities.map { entity ->
                vacancyCardConverter.toVacancyCard(entity)
            })
        }
            .catch {
                Resource.Error<Resource<List<VacancyCard>>>(ErrorCode.LOCAL_DB_ERROR)
            }
}
