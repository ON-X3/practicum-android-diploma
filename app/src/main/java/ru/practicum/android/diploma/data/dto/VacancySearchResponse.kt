package ru.practicum.android.diploma.data.dto

class VacancySearchResponse (
    val resultCount: Int,
    val result: List<VacancyListDTO>
): Response() {}
