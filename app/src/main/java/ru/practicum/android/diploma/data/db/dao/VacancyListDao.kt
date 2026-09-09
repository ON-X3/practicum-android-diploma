package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.VacancyListEntity

@Dao
interface VacancyListDao {
    @Insert(entity = VacancyListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vacancies: List<VacancyListEntity>)

    @Query("SELECT * FROM vacancy_list")
    suspend fun getAll(): List<VacancyListEntity>
}
