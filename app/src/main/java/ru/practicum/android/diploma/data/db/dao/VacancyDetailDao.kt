package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity

@Dao
interface VacancyDetailDao {
    @Insert(entity = VacancyDetailEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyDetailEntity)

    @Query("SELECT * FROM vacancy_table")
    fun getVacancyList(): Flow<List<VacancyDetailEntity>>

    @Query("SELECT * FROM vacancy_table WHERE id = :vacancyId")
    suspend fun getVacancyById(vacancyId: String): VacancyDetailEntity?

    @Query("DELETE FROM vacancy_table WHERE id = :vacancyId")
    suspend fun deleteVacancyById(vacancyId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM vacancy_table WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean
}
