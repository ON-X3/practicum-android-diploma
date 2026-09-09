package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.AreaEntity

@Dao
interface AreaDao {
    @Insert(entity = AreaEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AreaEntity>)

    @Query("SELECT * FROM area_entity")
    suspend fun getAll(): List<AreaEntity>

    @Query("SELECT * FROM area_entity WHERE parentId IS NULL")
    suspend fun getRootRegions(): List<AreaEntity>

    @Query("SELECT * FROM area_entity WHERE parentId = :parentId")
    suspend fun getChildren(parentId: String): List<AreaEntity>
}
