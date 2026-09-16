package ru.practicum.android.diploma.domain.models

data class VacancyDetail(
    val id: String,
    val name: String,
    val descriptionHtml: String,
    val salary: VacancySalary?,
    val address: String,
    val experience: String,
    val schedule: String,
    val employment: String,
    val employer: Employer,
    val areaName: String,
    val skills: List<String>,
    val sharingUrl: String,
    val isFavorite: Boolean
)

data class Employer(
    val name: String,
    val logo: String
)
