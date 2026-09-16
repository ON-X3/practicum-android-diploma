package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("industries")
data class IndustryEntity(
    @PrimaryKey
    val id: String,
    val name: String
)
