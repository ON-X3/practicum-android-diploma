package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("vacancy_table")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val url: String?,
    val salary: String?,
    val address: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val contacts: String?,
    val employer: String?,
    val area: String?,
    val industry: String?,
    val skills: String?
)
