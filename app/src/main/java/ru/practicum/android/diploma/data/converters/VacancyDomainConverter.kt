package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.SalaryDTO
import ru.practicum.android.diploma.data.dto.VacancyCardDto
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancySalary

class VacancyDomainConverter {
    fun toDomain(vacancySearchResponse: VacancySearchResponse): VacanciesSearchResult {
        return VacanciesSearchResult(
            vacancies = vacancySearchResponse.items.map { it.toVacancyCard() },
            currentPage = vacancySearchResponse.page,
            pages = vacancySearchResponse.pages,
            found = vacancySearchResponse.found
        )
    }

    private fun VacancyCardDto.toVacancyCard() = VacancyCard(
        id = id,
        name = name,
        company = company,
        city = city,
        salary = salary?.toDomain(),
        logo = logo,
    )

    private fun SalaryDTO.toDomain() = VacancySalary(from, to, currency)
}
