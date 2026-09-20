package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity

@Dao
interface VacancyDetailDao {
    @Insert(entity = VacancyDetailEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyDetailEntity)

    @Query("SELECT * FROM vacancy_table")
    suspend fun getVacancy(): VacancyDetailEntity

    @Query("SELECT * FROM vacancy_table WHERE id = :vacancyId")
    suspend fun getVacancyById(vacancyId: String): VacancyDetailEntity
}
