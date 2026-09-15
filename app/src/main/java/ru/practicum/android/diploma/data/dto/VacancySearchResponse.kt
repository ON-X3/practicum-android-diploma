package ru.practicum.android.diploma.data.dto

class VacancySearchResponse(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyCardDto>
) : Response()
