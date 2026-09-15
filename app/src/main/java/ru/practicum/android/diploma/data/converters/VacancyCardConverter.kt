package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancySalary

class VacancyCardConverter {
    fun toVacancyCard(entity: VacancyDetailEntity): VacancyCard {
        return VacancyCard(
                id = entity.id,
                name = entity.name,
                company = entity.employerName,
                city = entity.area,
                salary = if (entity.salaryFrom != null || entity.salaryTo != null || entity.salaryCurrency != null) {
                    VacancySalary(
                        from = entity.salaryFrom,
                        to = entity.salaryTo,
                        currency = entity.salaryCurrency,
                    )
                } else {
                    null
                },
                logo = entity.employerLogo
            )
        }
}