package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("vacancy_list")
data class VacancyListEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val company: String,
    val city: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?,
    val logo: String?
)
