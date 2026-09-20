package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.IndustryEntity

@Dao
interface IndustryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<IndustryEntity>)

    @Query("SELECT * FROM industries")
    suspend fun getAll(): List<IndustryEntity>
}
