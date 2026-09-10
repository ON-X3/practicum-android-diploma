package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.AreaDao
import ru.practicum.android.diploma.data.db.dao.IndustryDao
import ru.practicum.android.diploma.data.db.dao.VacancyDetailDao
import ru.practicum.android.diploma.data.db.dao.VacancyListDao
import ru.practicum.android.diploma.data.db.entity.AreaEntity
import ru.practicum.android.diploma.data.db.entity.IndustryEntity
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.data.db.entity.VacancyListEntity

@Database(
    version = 2,
    entities = [AreaEntity::class,
        IndustryEntity::class,
        VacancyDetailEntity::class,
        VacancyListEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun areaDao(): AreaDao
    abstract fun industryDao(): IndustryDao
    abstract fun vacancyDao(): VacancyDetailDao
    abstract fun vacancyListDao(): VacancyListDao
}
