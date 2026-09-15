package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.models.VacancySalary

class VacancyDetailConverter {

    fun toEntity(vacancy: VacancyDetail): VacancyDetailEntity =
        VacancyDetailEntity(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.descriptionHtml,
            url = vacancy.sharingUrl,
            salaryFrom = vacancy.salary?.from,
            salaryTo = vacancy.salary?.to,
            salaryCurrency = vacancy.salary?.currency,
            address = vacancy.address,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            contacts = null,
            employerName = vacancy.employer.name,
            employerLogo = vacancy.employer.logo,
            area = vacancy.areaName,
            industry = null,
            skills = vacancy.skills.joinToString(separator = "|"),
        )

    fun toDomain(entity: VacancyDetailEntity?): VacancyDetail? =
        entity?.let {
            VacancyDetail(
                id = it.id,
                name = it.name,
                descriptionHtml = it.description,
                salary = if (it.salaryFrom != null || it.salaryTo != null || it.salaryCurrency != null) {
                    VacancySalary(
                        from = it.salaryFrom,
                        to = it.salaryTo,
                        currency = it.salaryCurrency,
                    )
                } else {
                    null
                },
                address = it.address.orEmpty(),
                experience = it.experience.orEmpty(),
                schedule = it.schedule.orEmpty(),
                employment = it.employment.orEmpty(),
                employer = Employer(
                    name = it.employerName.orEmpty(),
                    logo = it.employerLogo.orEmpty(),
                ),
                areaName = it.area.orEmpty(),
                skills = it.skills
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    .orEmpty(),
                sharingUrl = it.url.orEmpty(),
                isFavorite = true,
            )
        }
}