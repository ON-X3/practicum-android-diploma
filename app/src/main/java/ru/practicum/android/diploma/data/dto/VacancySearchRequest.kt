package ru.practicum.android.diploma.data.dto

data class VacancySearchRequest(
    val expression: String,
    val page: Int,
    val area: Int? = null,
    val industry: Int? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean? = null,
)
