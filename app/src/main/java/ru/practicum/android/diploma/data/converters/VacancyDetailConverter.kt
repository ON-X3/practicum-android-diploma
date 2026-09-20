package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.data.dto.AddressDTO
import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.data.dto.EmployerDTO
import ru.practicum.android.diploma.data.dto.IdNameDTO
import ru.practicum.android.diploma.data.dto.SalaryDTO
import ru.practicum.android.diploma.data.dto.VacancyDetailDTO
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.models.VacancySalary

class VacancyDetailConverter {

    fun toEntity(vacancy: VacancyDetail): VacancyDetailEntity =
        VacancyDetailEntity(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.descriptionHtml,
            url = vacancy.url,
            salaryFrom = vacancy.salary?.from,
            salaryTo = vacancy.salary?.to,
            salaryCurrency = vacancy.salary?.currency,
            address = vacancy.address,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            employerName = vacancy.employer.name,
            employerLogo = vacancy.employer.logo,
            area = vacancy.areaName,
            industry = vacancy.industryName,
            skills = vacancy.skills.joinToString(separator = "|"),
        )

    fun toDomain(dto: VacancyDetailDTO): VacancyDetail {
        return VacancyDetail(
            id = dto.id,
            name = dto.name,
            descriptionHtml = dto.description.orEmpty(),
            salary = dto.salary.toDomain(),
            address = dto.address.raw(),
            experience = dto.experience.name(),
            schedule = dto.schedule.name(),
            employment = dto.employment.name(),
            employer = dto.employer.toDomain(),
            areaName = dto.area.name(),
            industryName = dto.industry.name(),
            skills = dto.skills.orEmpty(),
            url = dto.url.orEmpty(),
            isFavorite = false
        )
    }

    fun toEntity(dto: VacancyDetailDTO): VacancyDetailEntity {
        return VacancyDetailEntity(
            id = dto.id,
            name = dto.name,
            description = dto.description.orEmpty(),
            salaryFrom = dto.salary?.from,
            salaryTo = dto.salary?.to,
            salaryCurrency = dto.salary?.currency,
            address = dto.address.raw(),
            experience = dto.experience.name(),
            schedule = dto.schedule.name(),
            employment = dto.employment.name(),
            employerName = dto.employer.name(),
            employerLogo = dto.employer.logo(),
            area = dto.area.name(),
            industry = dto.industry.name(),
            skills = dto.skills.orEmpty().joinToString(separator = "|"),
            url = dto.url.orEmpty()
        )
    }

    fun toDomain(entity: VacancyDetailEntity): VacancyDetail {
        return VacancyDetail(
            id = entity.id,
            name = entity.name,
            descriptionHtml = entity.description,
            salary = entity.toDomainSalary(),
            address = entity.address ?: entity.area,
            experience = entity.experience,
            schedule = entity.schedule,
            employment = entity.employment,
            employer = Employer(entity.employerName, entity.employerLogo),
            areaName = entity.area,
            industryName = entity.industry,
            skills = entity.skills.split("|")
                .filter { it.isNotBlank() },
            url = entity.url,
            isFavorite = true
        )
    }

    private fun SalaryDTO?.toDomain(): VacancySalary? =
        this?.let { VacancySalary(it.from, it.to, it.currency) }

    private fun EmployerDTO?.toDomain(): Employer = Employer(
        name = this?.name.orEmpty(),
        logo = this?.logo.orEmpty()
    )

    private fun AddressDTO?.raw(): String = this?.raw.orEmpty()

    private fun IdNameDTO?.name(): String = this?.name.orEmpty()

    private fun AreaDTO?.name(): String = this?.name.orEmpty()

    private fun EmployerDTO?.name(): String = this?.name.orEmpty()

    private fun EmployerDTO?.logo(): String = this?.logo.orEmpty()

    private fun VacancyDetailEntity.toDomainSalary(): VacancySalary? {
        if (salaryFrom == null && salaryTo == null && salaryCurrency == null) return null
        return VacancySalary(salaryFrom, salaryTo, salaryCurrency)
    }
}
