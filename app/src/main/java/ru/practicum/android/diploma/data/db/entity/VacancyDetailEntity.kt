package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyDetailEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val url: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,
    val address: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val contacts: String?,
    val employerName: String?,
    val employerLogo: String?,
    val area: String?,
    val industry: String?,
    val skills: String?,
)
