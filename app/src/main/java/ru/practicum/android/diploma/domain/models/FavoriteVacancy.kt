package ru.practicum.android.diploma.domain.models

data class FavoriteVacancy(
    val id: String,
    val vacancyName: String,
    val companyName: String,
    val description: String,
    val salary: VacancySalary?,
    val address: String,
    val experience: String,
    val schedule: String,
    val employment: String,
    val areaName: String,
    val skills: List<String>,
    val shareLink: String,
    val addedAt: Long
)
