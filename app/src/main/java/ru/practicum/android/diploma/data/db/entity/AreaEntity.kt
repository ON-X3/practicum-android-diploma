package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("area_entity")
data class AreaEntity (
    @PrimaryKey
    val id: String,
    val parentId: String?,
    val name: String
    )
