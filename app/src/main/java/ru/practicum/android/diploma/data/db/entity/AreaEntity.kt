package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("area_entity")
data class AreaEntity(
    @PrimaryKey
    val id: Int,
    val parentId: Int?,
    val name: String,
    val areas: String?
)
