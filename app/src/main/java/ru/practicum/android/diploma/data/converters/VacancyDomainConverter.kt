package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.VacancyItemDto
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancySalary

class VacancyDomainConverter {
    fun toDomain(vacancySearchResponse: VacancySearchResponse): VacanciesSearchResult {
        return VacanciesSearchResult(
            vacancies = vacancySearchResponse.result.items.map { it.toVacancyCard() },
            currentPage = vacancySearchResponse.result.page,
            pages = vacancySearchResponse.result.pages,
            found = vacancySearchResponse.result.found
        )
    }

    private fun VacancyItemDto.toVacancyCard() = VacancyCard(
        id = id,
        name = name,
        company = company,
        city = city,
        salary = salary?.toDomain(),
        logo = logo,
    )

    private fun SalaryDto.toDomain() = VacancySalary(from, to, currency)
}
