package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("vacancy_table")
data class VacancyDetailEntity(
    @PrimaryKey val id: String,
    val name: String,
    val descriptionHtml: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,
    val address: String,
    val experience: String,
    val schedule: String,
    val employment: String,
    val employerName: String,
    val employerLogo: String,
    val areaName: String,
    val industryName: String,
    val contactName: String,
    val contactEmail: String,
    val contactPhones: List<String>,
    val skills: List<String>,
    val url: String,
    val isFavorite: Boolean
)
