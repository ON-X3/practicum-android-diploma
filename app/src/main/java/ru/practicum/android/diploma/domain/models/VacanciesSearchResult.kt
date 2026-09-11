package ru.practicum.android.diploma.domain.models

data class VacanciesSearchResult(
    val vacancies: List<VacancyCard>,
    val currentPage: Int,
    val pages: Int
)
